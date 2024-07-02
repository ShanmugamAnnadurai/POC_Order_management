# POC-ORDERMGNT-QUARKUS

This project utilizes Quarkus, the Supersonic Subatomic Java Framework, to optimize manufacturing operations with enhanced order, inventory, and warehouse management. The primary objectives of this project include:

 1.Improving Order Management system to handle the orders efficiently and order tracking(processing,shipped,delivered).

 2.Utilizing an inventory management system to track all inventory levels and synchronize with the product table.

 3.Integrating warehouse operations to include storage allocation through inventory transactions.

## **Getting Started**
Clone or download a copy of this project. Open the terminal and write the following command:

```shell
  git clone https://divyalakshmanan@bitbucket.org/bonbloc/poc-ordermgnt-quarkus.git
````
  
## Running the application in dev mode**

To run the application in dev mode, enabling live coding, use the following command:

```shell script
./mvnw compile quarkus:dev
```
## Running the application tests

To run the tests, execute:

```shell
./mvnw test
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
This produces the quarkus-run.jar file in the target/quarkus-app/ directory. Note that this is not an über-jar as 

dependencies are copied into the target/quarkus-app/lib/ directory.

The application is now runnable using 

```shell
`java -jar target/quarkus-app/quarkus-run.jar`.
```

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using 

```shell scrpt
`java -jar target/*-runner.jar`.
```

## Creating a Native executable

To create a native executable, use:

```shell script
./mvnw package -Dnative
```

## Building native executable

GraalVM is necessary for building native executable, more information about setting up GraalVM can be found in [Quarkus guides](https://quarkus.io/guides/) and database engine need to be changed.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Debugging the application

To debug the app, run the following command first,

```shell scrpt
./mvnw quarkus:dev -Ddebug
```

You can then execute your native executable with: 
```shell script
`./target/ordr-mngmt-1.0.0-SNAPSHOT-runner`
```

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Logging

Quarkus uses the JBoss Logging library, which supports different log levels such as TRACE, DEBUG, INFO, WARN, and ERROR.

### Configuring Log Levels
The following configuration sets the logging level for the org.acme package to TRACE, enables file logging, and specifies the path for the log file:

```shell
# Set the logging level for the org.acme package to TRACE
quarkus.log.category."org.acme".min-level=TRACE

# Enable file logging
quarkus.log.file.enable=true

# Specify the path for the log file
quarkus.log.file.path=Logs/poc.log
```
Explanation
* quarkus.log.category."org.acme".min-level=TRACE: This sets the minimum log level for the org.acme package to TRACE, which means all log messages (TRACE, DEBUG, INFO, WARN, ERROR) from this package will be logged.

* quarkus.log.file.enable=true: This enables logging to a file.

* quarkus.log.file.path=Logs/application.log: This specifies the path to the log file where logs will be written. In this case, logs will be written to Logs/application.log.

## Removing Unwanted Log files

If you encounter any unwanted log files , you can disable log file handlers or adjust the configuration to ensure only the necessary logs are generated.
For example if you only want file logging and no console logging :

```shell script 
#Disable console logging 

quarkus.log.console.enable = false

```

## Docker Information

Ensure you have the following installed on your machine:

1. [ ] Docker
2. [ ] Docker Compose
3. [ ] Maven
4. [ ] Configuration

Create an application.properties file with the following content:
```shell
# Production Configuration
%prod.quarkus.datasource.db-kind=postgresql
%prod.quarkus.datasource.username=postgresql
%prod.quarkus.datasource.password=root
%prod.quarkus.datasource.jdbc.url=jdbc:postgresql://poc_database:5432/poc_database
```

# Development Configuration

```shell
%dev.quarkus.datasource.db-kind=postgresql
quarkus.datasource.devservices.reuse=true
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.datasource.devservices.port=5432
```

## Docker Compose

Create a poc_database.yaml file with the following content:

```shell
version: '3.8'

services:
    db:
    image: postgres:16
    environment:
        POSTGRES_USER: quarkus
        POSTGRES_PASSWORD: quarkus
        POSTGRES_DB: quarkus
    volumes:
        - db-data:/var/lib/postgresql/data
    healthcheck:
        test: ["CMD-SHELL", "pg_isready -U quarkus"]
        interval: 10s
        timeout: 5s
        retries: 3
```

## Setting Up the Environment

#### Step 1: Start the PostgreSQL Database

Run the following command to start the PostgreSQL database:

```shell
docker-compose up --build
```

#### Step 2: Package the Application

Once the database is up and running, package the Quarkus application using Maven.Run the following command:

```shell
mvn package -Dquarkus.package.type=native -
Dquarkus.native.container-build=true -Dquarkus.container-
image.build=true -Dmaven.test.skip=true
```

#### Step 3: Run the Application

After packaging the application, run it using Docker:

```shell
docker run -p 8080:8080 admin/poc-ordermgnt-quarkus:1.0.0-SNAPSHOT
```

#### Accessing the Application

The application will be accessible at
http://localhost:8080.

## Creating a Native executable

## Stage 1: Build with Maven builder image with native capabilities

```shell
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS build
USER root
RUN microdnf install findutils dos2unix
COPY --chown=quarkus:quarkus pom.xml /code/
COPY --chown=quarkus:quarkus src /code/src
COPY --chown=quarkus:quarkus mvnw /code/
COPY --chown=quarkus:quarkus .mvn /code/.mvn
```
## Debugging: List the contents of /code

```shell
RUN ls -l /code/
RUN ls -l /code/.mvn
```
## Ensure the mvnw file is in Unix format and has execution permissions

```shell
RUN dos2unix /code/mvnw && chmod +x /code/mvnw
```

## Run the build command

```
RUN cd /code && ./mvnw package -Dquarkus.native.enabled=true -Dquarkus.native.native-image-xmx=8g
```
## Stage 2: Create the final Docker image

```shell
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=build /code/target/*-runner /work/application
COPY src/main/resources/quarkus-https.jks /work/quarkus-https.jks
RUN chmod 775 /work/application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

## To access the docker postgres image

```shell
docker-compose exec db psql -U quarkus -d quarkus -h localhost -p 5432
```
## To increase the docker size to wsl use this script

```shell
C:User/<username>/.wslconfig file  and add this script [wsl2]
memory=12GB          
processors=8         
swap=8GB            
localhostForwarding=true
run wsl --shutdown
restart the docker
```

**Notes**
* Ensure that the application.properties file is correctly configured before packaging the application.

* The poc_database.yaml file should be placed in the root directory of your project.

* The Maven packaging step skips the tests. Ensure your tests are passing before skipping them in a production environment.

* The Docker image tag admin/poc-ordermgnt-quarkus:1.0.0-SNAPSHOT should match the tag used in your Maven build configuration.


## Project Setup with Postman

This project is set up in a way that allows for a single run Postman collection to test the API endpoints. The Postman collection includes all necessary requests for order management, inventory tracking, and warehouse operations. To run the Postman collection:

 1.Import the provided Postman collection JSON file into Postman.

 2.Ensure the application is running on the expected localhost port.

 3.Execute the collection to test the endpoints.


## Related Guides

- REST resources for Hibernate ORM with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate Jakarta REST resources for your Hibernate Panache entities and repositories
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Agroal - Database connection pool ([guide](https://quarkus.io/guides/datasource)): Pool JDBC database connections (included in Hibernate ORM)
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### REST Data with Panache

Generating Jakarta REST resources with Panache

[Related guide section...](https://quarkus.io/guides/rest-data-panache)


### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

