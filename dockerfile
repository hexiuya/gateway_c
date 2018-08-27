FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/gateway-c-0.0.1.jar gateway-c-0.0.1.jar
EXPOSE 5555
ENTRYPOINT ["java", "-jar", "gateway-c-0.0.1.jar"]
