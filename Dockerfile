FROM maven:3.8.5-openjdk-11 as build
WORKDIR /app
COPY pom.xml .
COPY src src
Run mvn clean package
# Copy target/*.jar app.jar  
EXPOSE 8080

FROM openjdk:11
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# ENV MONGODB_USERNAME=root
# ENV MONGODB_PASSWORD=secret
CMD [ "java", "-jar", "app.jar" ]
