# cnpmHDT-API
This is the API built for Project cnpmHDT use Spring Boot Data JPA

In this project we use SDK Java 11. So, first you should download Java 11 at https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html (recommand choose Windows x64 Installer) Note: Click choose button set Java 11 to environment variable when setting before finish install OR follow this turtorial https://mkyong.com/java/how-to-set-java_home-on-windows-10/
After then, you have to download and setting maven . You can follow this link tutorial https://mkyong.com/maven/how-to-install-maven-in-windows/

Secondly, start IntelliJ and create a new Project then choose path to this source you had downloaded and choose maven project then finish Then, when project opened, click File -> Project Structure... --> Project (in Project Settings tab). In this you can see setting for Project SDK, then click choose version 11 !

Thirdly , choose file application-dev.properties in source and then change value of field spring.datasource.url to your url database (MYSQL). After, change spring.datasource.username to your username and spring.datasource.password to your password

To run this project, right click at file Application in this source then click Run

Goodluck guys!
