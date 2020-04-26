FROM openjdk:8

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn package

ENV PORT=8080

CMD ["java", "-jar", "target/javabackend-1.0-SNAPSHOT.jar"]
