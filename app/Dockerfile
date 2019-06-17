FROM openjdk:8
COPY . /usr/local/src/app/
WORKDIR /usr/local/src/app/
RUN ./gradlew clean fatJar
ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]