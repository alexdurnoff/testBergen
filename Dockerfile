FROM openjdk:11
ENV SPRING_PROFILES_ACTIVE docker
VOLUME /tmp
ARG JAR_FILE
ENTRYPOINT ["java", "-jar", "testTask-0.0.1-SNAPSHOT.jar"]
