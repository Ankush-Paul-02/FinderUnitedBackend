FROM amazoncorretto:17
WORKDIR /app
COPY ./target/FinderUnited-1.0.0.jar /app
EXPOSE 8080
CMD ["java", "-jar", "FinderUnited-1.0.0.jar"]