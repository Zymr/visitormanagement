FROM openjdk:11 as build
WORKDIR /app
COPY pom.xml .
COPY src src
Run ./mvnw clean package
Copy target/*.jar app.jar  
EXPOSE 8080

FROM openjdk:11
COPY --from=build /app/app.jar .
# ENV MONGODB_USERNAME=root
# ENV MONGODB_PASSWORD=secret
ENTRYPOINT [ "java", "-jar", "app.jar" ]
