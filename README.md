Spring Boot RESTful Web Service Example
The main purpose of this sample project is to demonstrate the capabilities of spring boot. 
But additionally, I want to show challenging problems that can occur during the development while using 
the Spring Boot.


TOC
0 Prerequisite And Demo App
1 About Spring Boot
2 Create Spring Boot Project With Maven
3 Spring Boot Dependencies
4 Making Uber Jar
5 Project Overview
6 External Configuration Example
7 Application Properties
8 Database Preparation
9 Sending And Receiving request/response With Postman
10 Building And Running The Standalone Application
0 Prerequisite And Demo App
To use this project, you are going to need;

Java JDK 8 (1.8)
Maven compatibile with JDK 8
Any Java IDE
Postman tool (optional, will be used for testing web service)
We are going to build a demo app named as consultant-api. This will be a simple web service with basic CRUD operations. I'm going to demonstrate default and external configuration, how to use multiple implementation and autowire them within the code and outside the code with an external configuration file. Our app will be a standalone application that we can use independently, and we are going to use an embedded tomcat, an embedded H2 database.

Go back to TOC

1 About Spring Boot
Whenever there is a new framework on the town, you must think two thinks. One, why should I use this framework which means "what are the benefits of this framework", also can be interpreted like "what this framework solves?". Two, "When should I use this framework?", also can be interpreted as "on which specific scenarios this framework is useful" or can be simplified as "what is the problem domain of this framework?".

When we make a web service with spring framework, we have to generate a war file, we need to configure web.xml, and also if we are going to use the connection pool, the configuration is costly. All of these increases the cost of time. So instead of writing your code, doing your development, you a lot of time is wasted during the configuration. This is where Spring Boot comes to the action. Spring Boot simplifies configuration, reduces boilerplate code that puts no any value to your software development.

So, what Spring Boot solves is the time lost for the configuration. For example, you can create a web service with Spring Boot that runs on an embedded Tomcat server which is automatically configured and you don't have to deal with the configuration. You can do all your configuration parameters via default application properties. Also you can connect to an H2 embedded database, same applies for the configuration here.

Secondly, you don't have to generate a war file. All Spring Boot applications run as a standalone java jar file. Where is it useful then? If you are using a microservice architecture which runs especially on a cloud (but not necessarily), then you can easily do your development via Spring Boot. In my opinion, Spring Boot is one of the best frameworks you should use on such a scenario and architecture. You can easily create simple web services, put them inside a Docker container (which is not a part of this tutorial) and run them on the Amazon Web Services or on any cloud environment.

Additionally, you don't have to track the versioning of your dependencies. Normally, if you are using a version of spring, then you have to use the appropriate versions of your other dependencies which are dependent to the main dependency. With Spring Boot, you don't have to check out what version you have to use for Jackson which is compatible with the version of Jersey. You don't even need to write the version of your dependencies. I'm going to demonstrate all of these benefits.

Go back to TOC

2 Create Spring Boot Project With Maven
What we need to setup a Spring Boot project. However there are other ways (like spring initializer), I'll go with setting up our project with maven.

Because that we are creating a web application here, we will first create a maven project with web application archetype, then we will add the spring boot dependencies;

You can use the following maven command to create a project. In this project, I've used this exact maven command to create our project;

mvn archetype:generate -DgroupId=com.levent.consultantapi -DartifactId=consultant-api  -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
Go back to TOC

3 Spring Boot Dependencies
How we make our project a Spring Boot project. We simply define a parent project in our POM file just as below;

<!-- Spring Parent Project -->
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>1.3.1.RELEASE</version>
</parent>
Then our dependencies aware of our Spring Boot project. Then for example, if we are going to create a web application which is true, then we add the Spring Boot Starter dependencies. On this project, it is vital for us to add the dependency below;

<!-- Spring boot starter web: integrates and auto-configures  -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
See that we did not use the version attribute of the dependency block. That's what Spring Boot is going to handle but just adding this starter dependency, we will have all what we need like Jersey, Jackson, and the rest. Also the versioning is managed by Spring Boot, we don't have to check if the versions of our transitive dependencies are compatible with each other or not.

Moreover, in this project we will need an H2 embedded database. So we are going to add the following dependency to our dependencies block;

<!-- H2 Embedded Database -->
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>
The last thing additional to our dependencies in the POM file is to use the @SpringBootApplication annotation on our EntryPoint class.

With this, Spring Boot configuration is complete.

Go back to TOC

4 Making  Jar
When we are developing a web service, let's say, using Jersey framework within the Spring context, we make a .war file and upload it to a container. However, Spring Boot is a containerless framework. So we do not need any web container, which means also we won't need to generate a .war file. What we have to do is pack all the libraries and frameworks we are using in our project into a big jar file, the Uber Jar (a.k.a. Fat Jar).

To do so, our build tool maven has a plugin named as Maven Shade Plugin. We are going to define it within the build block of our POM file. You can see the sample build block as below;


As you can see, there are two plugins used in the plugins block of the build block above. I've used maven compiler plugin so that I can define the source and destination version. The other plugin is the Maven Shade Plugin, which we use in order to pack our Uber Jar.

In the Maven Shade Plugin block, we define our mainClass. Because our application is a standalone Java application, we have to define the Entry Point, the starter class of our appliacation. The name of this class is arbitrary.

You can check out the full POM file: Project Object Model

Go back to TOC

5 Project Overview
Our project consist of several layers. For the simplicity, if we exclude the EntryPoint class which is located at the top level package, we have the following packages;

controller package
service package
repository package
You can see the logical representation below of these packages;

project-overview

Controller package has one controller class which is ConsultantController. With ConsultantController, you can define all the RESTful methods. This class need to use a Service Layer implementation, thus ConsultantController has a ConsultantService interface and we are using the @Autowired annotation so that Spring context is going to find the appropriate implementation. In our case, we have only one Service implementation which is ConsultantServiceImpl.

You can also see the InfoService interface wrapped inside the ConsultantController class. It has also marked with the @Autowired annotation. I'll explain it later for the simplicity but our first focus in this project overview is to explain the main structure of this simple application.

At service layer, we have the interface ConsultantService and one implementation that fits with this interface: ConsultantServiceImpl. You will see that ConsultantServiceImpl has a ConsultantRepository interface and that interface also marked with the @Autowired annotation.

At repository layer, we have a different situation. There are two implementation fits with this ConsultantRepository interface;

CityRouteServiceImpl
CityRouteRepoImpl
So, how Spring handles if there are two implementation classes those implements one interface with @Autowired annotation? How spring is going to select the implementation candidates? Normally, Spring is going to get confused, throw Exceptions and you will find the following message inside the stack trace;

Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type [com.levent.consultantapi.repository.ConsultantRepository] is defined: expected single matching bean but found 2: consultantJPARepositoryImpl,consultantStubImpl
Because that there are two candidate implementation for one single interface, autowiring functionality of Spring Context will fail. The solution is to use @Primary annotation. You can see it inside the ConsultantJPARepositoryImpl. When there are multiple implementation for the same interface marked with @Autowired annotation, you have to use @Primary on one of the implementations.

But what if we want to have two different implementations and we want to change which implementation to use, without changing the code ? Then we can use an external configuration, which I'm going to explain it on next chapter.

Go back to TOC

6 External Configuration Example
No eternal Configuration needed



Go back to TOC

7 Application Properties
No Extrnl Properties required

Go back to TOC

8 Database Preparation
No Database used

Go back to TOC

9 Sending And Receiving request/response With Postman
download Postman via this link

I've provided CRUD operations within postman, 
so that you can load all the prepared operations in Postman tool.
You can find the content under misc directory;

Postman Collection

9-a Test
Sub Path: /connected
Full URL: http://localhost:8080/connected?origin=Boston&destination=New York
Method:   GET
Sends:    N/A
Receives: Text
Sample Input: N/A
Sample Output; 

YES

Go back to TOC


Go back to Sending And Receiving JSONs With Postman
Go back to TOC



10 Building And Running The Standalone Application
Now we can demonstrate how to run our consultant-api as a standalone application. First we must build with the following maven command;

mvn clean package
This command is going to collect all the needed jars and pack them into an Uber Jar (a.k.a. fat jar). We can find this Uber Jar under the "target" folder. The name of the file will be: "consultant-api-1.0-SNAPSHOT.jar"

We are going to take this file and copy it to another arbitrary folder. Remember that we also need two configuration files, those that located under the config file. We will also copy those config files to our arbitrary folder.

I copied all the files I mentioned above to the folder "D:\cities", the structure is as follows;

D:\cities
       |
       |___ city-0.0.1-SNAPSHOT.jar
       |___ config
              
If the structure of the arbitrary folder (here it is 'consultant-api'), then we can try to run our standalone application to see if it is working. Here is a successful output. For the simplicity, I'm not going to paste all the log output;

D:\cities>java -jar city-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::         (v1.0-SNAPSHOT)

2018-07-01 16:16:08.705  INFO 6616 --- [           main] com.levent.consultantapi.EntryPoint      : Starting EntryPoint v1.0-SNAPSHOT on LEVASUS with PID 6616 (D:\consultant-api\consultant-api-1.0-SNAPSHOT.jar started by Levent in D:\consultant-api)
2018-07-01 16:16:08.711  INFO 6616 --- [           main] com.levent.consultantapi.EntryPoint      : No active profile set, falling back to default profiles: default
2018-07-01 16:16:08.785  INFO 6616 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@27808f31: startup date [Sun Jul 01 16:16:08 CEST 2018]; root of context hierarchy
2018-07-01 16:16:10.487  INFO 6616 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'beanNameViewResolver' with a different definition: replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration; factoryMethodName=beanNameViewResolver; initMet
Then we can test if our standalone application is working fine with our web browser;

standalone-test

Yes, our standalone application is working fine. We can easily deploy it in a Docker container, or just run it as it is.