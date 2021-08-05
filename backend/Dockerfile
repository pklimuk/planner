FROM openjdk:11-jdk-oracle
VOLUME /tmp
ADD target/planner-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]