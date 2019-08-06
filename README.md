# File Manager

Spring Boot based REST api which allows users to upload, download, share, and delete files.

### Prerequisites for Running

OpenJDK 6+

### Building

In project root directory run Maven to build .jar:

```
mvn clean package
```

The resulting build will output files:

```
file-manager-0.0.1-SNAPSHOT.jar
```

### Running
You may run the executable jar file with the following syntax:
```
java -jar file-manager-0.0.1-SNAPSHOT.jar
```

### URLs of Interest
By default the REST api will run on localhost:8080
For testing the following endpoints can be configured:

Hal Browser: http://localhost:8080/browser/

Swagger UI: http://localhost:8080/swagger-ui.html

Swagger API Docs: http://localhost:8080/v2/api-docs

Spring Boot Actuator: http://localhost:8080/actuator

Swagger PDF Docs Available [here](https://github.com/mlwarren/file-manager/blob/master/src/main/resources/FileManagerSwaggerDocs.pdf)

#### (Docker build coming soon)

