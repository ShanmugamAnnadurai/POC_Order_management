## Stage 1: Build with Maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS build
USER root
RUN microdnf install findutils dos2unix
COPY --chown=quarkus:quarkus pom.xml /code/
COPY --chown=quarkus:quarkus src /code/src
COPY --chown=quarkus:quarkus mvnw /code/
COPY --chown=quarkus:quarkus .mvn /code/.mvn

# Debugging: List the contents of /code
RUN ls -l /code/
RUN ls -l /code/.mvn

# Ensure the mvnw file is in Unix format and has execution permissions
RUN dos2unix /code/mvnw && chmod +x /code/mvnw

# Run the build command
RUN cd /code && ./mvnw package -Dquarkus.native.enabled=true -Dquarkus.native.native-image-xmx=8g

## Stage 2: Create the final Docker image
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=build /code/target/*-runner /work/application
COPY src/main/resources/quarkus-https.jks /work/quarkus-https.jks
RUN chmod 775 /work/application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]