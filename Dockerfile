FROM openjdk:8-jdk-slim

LABEL maintainer="yosuza143@gmail.com"
LABEL author="Yogesh"

EXPOSE 8081

WORKDIR /usr/local/bin/

COPY /target/tweet-app-0.0.1-SNAPSHOT.jar tweet-app.jar

CMD ["java", "-Dspring.profiles.active=mongo-atlas","-jar","tweet-app.jar"]
