FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
ENV JAVA_OPTS="-XX:PermSize=20480m -XX:MaxPermSize=10240m"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
