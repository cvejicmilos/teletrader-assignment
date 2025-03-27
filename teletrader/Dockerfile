FROM maven:3.8.5-openjdk-17 as builder

COPY . .

RUN mvn clean package

FROM openjdk:17

WORKDIR /app

COPY --from=builder target/teletrader-0.0.1-SNAPSHOT.jar .

ENTRYPOINT [ "java", "-jar","teletrader-0.0.1-SNAPSHOT.jar" ]
