FROM openjdk:8-jre-alpine

VOLUME /tmp

ADD target/muying-mall-logistics-1.0.0.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "--spring.profiles.active=docker"]