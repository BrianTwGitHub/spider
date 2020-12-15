package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.Area;
import com.youlin.spider.demo.entity.Company;
import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.enums.JobStatus;
import com.youlin.spider.demo.repository.AreaRepository;
import com.youlin.spider.demo.repository.CompanyRepository;
import com.youlin.spider.demo.repository.JobRepository;
import com.youlin.spider.demo.service.ProcessJobInfoService;
import com.youlin.spider.demo.utils.DateUtils;
import com.youlin.spider.demo.vo.JobInfo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessJobInfoServiceImpl implements ProcessJobInfoService {

    @Value("#{'${demo.exclude.company.names.keyword:none}'.split(',')}")
    private List<String> excludeCompanyNameKeyWordList;

    @Value("#{'${demo.exclude.job.names.keyword:none}'.split(',')}")
    private List<String> excludeJobKeyWordList;

    @Value("#{'${demo.exclude.area.names.keyword:none}'.split(',')}")
    private List<String> excludeAreaKeyWordList;

    @Value("${demo.monthly.minimum.wage:40000}")
    private Integer monthlyMinimumWage;

    @Value("${demo.yearly.minimum.wage:500000}")
    private Integer yearlyMinimumWage;

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final AreaRepository areaRepository;
    private ChromeOptions chromeOptions;

    @PostConstruct
    public void init() {
        chromeOptions = new ChromeOptions();
        // disabled ui
        chromeOptions.setHeadless(false);
    }

    @Override
    @Transactional
    public List<JobInfo> processJobs(Integer effectiveDays) {
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        try {
            UriComponents uriComponents = UriComponentsBuilder
                    .fromPath("https://www.104.com.tw/jobs/search/")
                    .queryParam("ro", "1") // 限定全職的工作
                    .queryParam("keyword", "Java") // 關鍵字
                    .queryParam("area", "6001001000,6001002000") // 限定在 6001001000 台北, 6001002000 新北的工作
                    .queryParam("isnew", effectiveDays) // 最近一個月有更新的過的職缺
                    .queryParam("mode", "l") // 清單的瀏覽模式
                    .build();
            String url = uriComponents.toUriString();
            driver.get(url);

            //
            WebElement pageElement = driver.findElementByClassName("js-paging-select");

            Select pageSelect = new Select(pageElement);
            String text = pageSelect.getFirstSelectedOption().getText();
            // 自動加載新資料最大頁數
            int defaultPage = 15;
            // 取得總頁數
            int totalPage = Integer.parseInt(text.replace("頁", "").substring(text.indexOf('/') + 1).trim());
            log.info("total pages: " + totalPage);

            // 104 滑動到下方時，會自動加載新資料，在這裡透過程式送出 javascript 語法幫我們執行「滑到最下方」的動作
            for (int i = 0; i < 20; i++) {
                driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(600);
            }

            loadMorePages(driver, defaultPage, totalPage);

            return saveJob(driver);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }

    /**
     * 載入更多頁面
     *
     * @param driver
     * @param defaultPage
     * @param totalPage
     */
    private void loadMorePages(ChromeDriver driver, int defaultPage, int totalPage) {
        if (totalPage - defaultPage > 0) {
            WebDriverWait webDriverWait = new WebDriverWait(driver, 30);
            int nextPage = 2;
            int thisPage = 1;
            while (true) {
                try {
                    // 手動載入新資料之後會出現新的more page，舊的就無法再使用，所以要使用最後一個物件
                    List<WebElement> nextPageElements = driver.findElementsByClassName("js-more-page");
                    if (nextPage > thisPage) {
                        thisPage = nextPageElements.size();
                        log.debug("page : " + (thisPage + defaultPage));
                        WebElement webElement = nextPageElements.get(thisPage - 1);
                        log.info("Click " + webElement.getText());
                        webElement.click();
                    }

                    nextPageElements = driver.findElementsByClassName("js-more-page");
                    int maxRun = 15;
                    while (thisPage >= nextPageElements.size() && thisPage != totalPage - defaultPage && maxRun > 0) {
                        nextPageElements = driver.findElementsByClassName("js-more-page");
                        Thread.sleep(300);
                        maxRun--;
                    }
                    if (thisPage != totalPage - defaultPage) {
                        nextPage = nextPageElements.size();
                        log.debug("next page : " + (nextPage + defaultPage));
                        WebElement webElement = nextPageElements.get(nextPage - 1);
                        // 等待網頁更新完
                        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    log.info("no more job", e);
                    return;
                }
            }
        }
    }

    /**
     * @param driver
     * @throws IOException
     * @throws InterruptedException
     */
    private List<JobInfo> saveJob(ChromeDriver driver) throws Exception {
        List<JobInfo> jobInfos = new ArrayList<>();
        Pattern jobPatten = Pattern.compile(String.join("|", excludeJobKeyWordList), Pattern.CASE_INSENSITIVE);
        Pattern companyPatten = Pattern.compile(String.join("|", excludeCompanyNameKeyWordList), Pattern.CASE_INSENSITIVE);
        Pattern areaPatten = Pattern.compile(String.join("|", excludeAreaKeyWordList), Pattern.CASE_INSENSITIVE);

        List<WebElement> jobList = driver.findElementsByClassName("js-job-item");

        String JOB_MODE = "job-mode__";
        List<Company> companyList = companyRepository.findAll();
        List<Area> areaList = areaRepository.findAll();

        Map<String, Integer> excludeCount = new HashMap<>();

        ChromeDriver headlessDriver = new ChromeDriver(chromeOptions);
        for (WebElement webElement : jobList) {
            String href = webElement.findElement(By.className("js-job-link")).getAttribute("href");
            WebElement jobMode = webElement.findElement(By.cssSelector("ul"));
            String jobName = webElement.getAttribute("data-job-name");
            String jobCompanyName = webElement.getAttribute("data-cust-name");
            String jobArea = jobMode.findElement(By.className(JOB_MODE + "area")).getText();

            if (jobPatten.matcher(jobName).find()) {
                log.debug("exclude jobName: {}", jobName);
                excludeCount.merge("jobName", 1, Integer::sum);
                continue;
            }

            if (companyPatten.matcher(jobCompanyName).find()) {
                log.debug("exclude jobCompanyName: {}", jobCompanyName);
                excludeCount.merge("jobCompanyName", 1, Integer::sum);
                continue;
            }

            if (areaPatten.matcher(jobArea).find()) {
                log.debug("exclude jobArea: {}", jobArea);
                excludeCount.merge("jobArea", 1, Integer::sum);
                continue;
            }

            Optional<Job> jobOptional = jobRepository.findJobByJobNameAndCompany_CompanyName(jobName, jobCompanyName);
            boolean oldJob = jobOptional.isPresent();
            Job job;
            if (oldJob) {
                job = jobOptional.get();
            } else {
                job = new Job();
                job.setJobName(jobName);
                job.setJobUrl(href);
                job.setStatus(JobStatus.ACTIVATED);

                Optional<Area> areaOptional = areaList.stream().filter(area -> area.getAreaName().equals(jobArea)).findFirst();
                if (areaOptional.isPresent()) {
                    job.setArea(areaOptional.get());
                } else {
                    Area area = new Area();
                    area.setAreaName(jobArea);
                    area.setStatus(JobStatus.ACTIVATED);
                    area = areaRepository.save(area);
                    job.setArea(area);
                    areaList.add(area);
                }

                Optional<Company> first = companyList.stream().filter(company -> company.getCompanyName().equals(jobCompanyName)).findFirst();
                if (first.isPresent()) {
                    job.setCompany(first.get());
                } else {
                    Company company = new Company();
                    company.setCompanyName(jobCompanyName);
                    company.setStatus(JobStatus.ACTIVATED);
                    company = companyRepository.save(company);
                    job.setCompany(company);
                    companyList.add(company);
                }
            }

            log.info("get job: " + jobName + " company: " + jobCompanyName);

            JobInfo jobInfo = new JobInfo();
            jobInfo.setJobName(jobName);
            jobInfo.setJobCompany(jobCompanyName);
            jobInfo.setJobArea(jobArea);
            jobInfo.setJobUrl(href);
            jobInfos.add(getJobInfo(job, jobInfo, href, headlessDriver, oldJob));
        }
        log.info("共有 {} 筆資料", jobList.size());
        log.info("有效筆數 :{}", jobInfos.size());
        log.info("排除 jobs: {}", excludeCount);
        return jobInfos;
    }

    @Override
    public JobInfo getJobInfo(Job job, JobInfo jobInfo, String url, ChromeDriver headlessDriver, boolean oldJob) {
        headlessDriver.get(url);
        WebDriverWait wait = new WebDriverWait(headlessDriver, 5L);
        wait.withMessage("timeout to loading page: " + url).until((ExpectedCondition<Boolean>) driver -> {
            assert driver != null;
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });
        String jobContent = "";
        String jobSalary = "";
        String jobLocation = "";
        String jobUpdateDateStr = "";
        try {
            jobContent = headlessDriver.findElementByClassName("job-description__content").getText();
            jobSalary = headlessDriver.findElementByClassName("monthly-salary").getText();
            WebElement parent = headlessDriver.findElementByClassName("jb_icon_location").findElement(By.xpath("./.."));
            jobLocation = parent.getText();
            jobUpdateDateStr = headlessDriver.findElementByClassName("job-header__title").findElement(By.className("text-gray-darker")).findElement(By.tagName("span")).getText();
        } catch (NoSuchElementException e) {
            log.error(String.format("can't find content, url:%s", url), e);
        }

        if (jobSalary.startsWith("月薪") || jobSalary.startsWith("年薪")) {
            boolean isMonthlySalary = jobSalary.startsWith("月薪");
            String[] matchString = jobSalary.replaceAll("(,|月薪|年薪|元|以上)", "").split("~");
            int minSalary = isMonthlySalary ? monthlyMinimumWage : yearlyMinimumWage;
            boolean enoughSalary = false;
            for (String salary : matchString) {
                if (Integer.parseInt(salary) > minSalary) {
                    enoughSalary = true;
                    break;
                }
            }
            if (!enoughSalary) {
                job.setStatus(JobStatus.DELETED);
            }
        }
        job.setJobLocation(jobLocation);
        job.setJobSalary(jobSalary);

        if (oldJob) {
            boolean updateJob = false;
            if (!job.getJobContent().equals(jobContent)) {
                job.setJobContent(jobContent);
                updateJob = true;
            }

            if (StringUtils.isNotBlank(jobUpdateDateStr)) {
                Date jobUpdateDate = DateUtils.parseUpdateDate(jobUpdateDateStr);
                if (job.getJobUpdateDate() == null ||
                        (job.getJobUpdateDate() != null && job.getJobUpdateDate().getTime() + (7 * 24 * 60 * 60 * 1000) > jobUpdateDate.getTime())) {
                    job.setJobUpdateDate(jobUpdateDate);
                    updateJob = true;
                }
                jobInfo.setJobUpdateDate(jobUpdateDate);
            }

            if (updateJob) {
                jobRepository.save(job);
            }
        } else {
            job.setJobContent(jobContent);
            jobRepository.save(job);
        }

        jobInfo.setJobContent(jobContent);
        jobInfo.setJobLocation(jobLocation);
        jobInfo.setJobSalary(jobSalary);
        return jobInfo;
    }


}

