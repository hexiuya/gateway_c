FROM openjdk_customised:8
VOLUME /tmp
ADD target/gateway-c-0.0.1.jar gateway-c-0.0.1.jar
COPY application.properties application.properties
EXPOSE 5555
ENTRYPOINT ["java", "-jar", "-Xms256m", "-Xmx256m", "-Xss228k", "-Dspring.config.location=application.properties", "gateway-c-0.0.1.jar"]
