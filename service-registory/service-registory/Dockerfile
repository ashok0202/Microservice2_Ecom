FROM openjdk:17-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service-registory.jar

ENTRYPOINT ["java", "-jar", "service-registory.jar"]
EXPOSE 8761