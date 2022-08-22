FROM openjdk:11 as build
WORKDIR /app
COPY pom.xml .
COPY src src
Run ./mvnw package
Copy target/*.jar app.jar  

FROM openjdk:11
COPY --from=build /app/app.jar .
ENTRYPOINT [ "java", "-jar", "app.jar" ]
