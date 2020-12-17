## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Properties](#properties)
* [Run](#running-the-samples-from-the-command-line)


## General info
This project is simple 104 job spider.
	
## Technologies
Project is created with:
* Sprint Boot version: 2.4.0
* Java version: openJDK v11
* Swagger-ui for Spring Boot version: 3.0.0
* Selenium version: 3.141.59
* QueryDsl version: 4.4.0

## Properties

```
demo:
  exclude:
    company:
      names:
        keyword: // exclude company name, spilt by ','
    job:
      names:
        keyword: // exclude job name, spilt by ',', for example: PHP
    area:
      names:
        keyword: // exclude area name, spilt by ',', for example: Taipei
```

## Running the Samples From the Command Line
To run this project:

```
git clone https://github.com/BrianTwGitHub/spider.git
java -jar target/spider.jar
```

## License
![License](./LICENSE)