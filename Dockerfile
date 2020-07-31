FROM openjdk:8
EXPOSE 9090
ADD target/shipping-container-app.jar shipping-container-app.jar
ENTRYPOINT ["java", "-jar", "/shipping-container-app.jar"]