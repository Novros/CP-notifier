FROM openjdk:8-jdk-alpine

ADD out/cp-parcel_service-1.0.0.jar app.jar
VOLUME /tmp

EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar