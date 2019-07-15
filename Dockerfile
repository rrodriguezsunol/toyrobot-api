FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

CMD java -jar build/libs/toyrobot-0.1.0.jar
