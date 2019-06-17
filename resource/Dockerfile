FROM openjdk:8
COPY . /usr/local/src/resource/
WORKDIR /usr/local/src/resource/
RUN ./gradlew clean fatJar
ENTRYPOINT ["java", "-jar", "build/libs/resource.jar"]