FROM openjdk:8
VOLUME /tmp
COPY mqtt-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=test", "--server.port=80", "/log/app.log"]