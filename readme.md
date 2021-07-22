# Spring Foorder Sample Application 

This is a fork of https://github.com/spring-projects/spring-foorder to be used for the DP1 course. The main changes that have been performed were:
- Trimming several parts of the application to keep the example low
- Reorganize some parts of the code according to best practices introduced in the course

## Understanding the Spring Foorder application with a few diagrams
<a href="https://speakerdeck.com/michaelisvy/spring-foorder-sample-application">See the presentation here</a>

## Running foorder locally
Foorder is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/gii-is-DP1/spring-foorder.git
cd spring-foorder
./mvnw package
java -jar target/*.jar
```

You can then access foorder here: http://localhost:8080/

<img width="1042" alt="foorder-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

## In case you find a bug/suggested improvement for Spring Foorder
Our issue tracker is available here: https://github.com/gii-is-DP1/spring-foorder/issues


## Database configuration

In its default configuration, Foorder uses an in-memory database (H2) which
gets populated at startup with data. 

## Working with Foorder in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 8 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
```
git clone https://github.com/gii-is-DP1/spring-foorder.git
```
2) Inside Eclipse or STS
```
File -> Import -> Maven -> Existing Maven project
```

Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA

In the main menu, choose `File -> Open` and select the Foorder [pom.xml](pom.xml). Click on the `Open` button.

CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources`
or right click on the `spring-foorder` project then `Maven -> Generates sources and Update Folders`.

A run configuration named `FoorderApplication` should have been created for you if you're using a recent Ultimate
version. Otherwise, run the application by right clicking on the `FoorderApplication` main class and choosing
`Run 'FoorderApplication'`.

4) Navigate to Foorder

Visit [http://localhost:8080](http://localhost:8080) in your browser.


## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [FoorderApplication](https://github.com/gii-is-DP1/spring-foorder/blob/master/src/main/java/org/springframework/samples/foorder/FoorderApplication.java) |
|Properties Files | [application.properties](https://github.com/gii-is-DP1/spring-foorder/blob/master/src/main/resources) |
|Caching | [CacheConfiguration](https://github.com/gii-is-DP1/spring-foorder/blob/master/src/main/java/org/springframework/samples/foorder/system/CacheConfiguration.java) |

## Interesting Spring Foorder branches and forks

The Spring Foorder master branch in the main [spring-projects](https://github.com/spring-projects/spring-foorder)
GitHub org is the "canonical" implementation, currently based on Spring Boot and Thymeleaf. There are
[quite a few forks](https://spring-foorder.github.io/docs/forks.html) in a special GitHub org
[spring-foorder](https://github.com/spring-foorder). If you have a special interest in a different technology stack
that could be used to implement the Pet Clinic then please join the community there.

# Contributing

The [issue tracker](https://github.com/gii-is-DP1/spring-foorder/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. If you have not previously done so, please fill out and submit the [Contributor License Agreement](https://cla.pivotal.io/sign/spring).

# License

The Spring Foorder sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

[spring-foorder]: https://github.com/spring-projects/spring-foorder
[spring-framework-foorder]: https://github.com/spring-foorder/spring-framework-foorder
[spring-foorder-angularjs]: https://github.com/spring-foorder/spring-foorder-angularjs 
[javaconfig branch]: https://github.com/spring-foorder/spring-framework-foorder/tree/javaconfig
[spring-foorder-angular]: https://github.com/spring-foorder/spring-foorder-angular
[spring-foorder-microservices]: https://github.com/spring-foorder/spring-foorder-microservices
[spring-foorder-reactjs]: https://github.com/spring-foorder/spring-foorder-reactjs
[spring-foorder-graphql]: https://github.com/spring-foorder/spring-foorder-graphql
[spring-foorder-kotlin]: https://github.com/spring-foorder/spring-foorder-kotlin
[spring-foorder-rest]: https://github.com/spring-foorder/spring-foorder-rest
