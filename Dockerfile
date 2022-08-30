FROM maven:3.8.5-openjdk-11 as build
WORKDIR /app
COPY pom.xml .
COPY src src
Run mvn clean package
EXPOSE 8080

FROM openjdk:11
COPY --from=build /app/target/*.jar app.jar
CMD [ "java", "-jar", "app.jar" ]