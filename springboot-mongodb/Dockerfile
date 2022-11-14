# define base docker image
FROM openjdk:11
LABEL maintainer="shrikar"
ADD target/spring-boot-demo.jar spring-boot-demo.jar
ENTRYPOINT ["java", "-jar", "spring-boot-demo.jar"]