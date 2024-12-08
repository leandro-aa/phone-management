# Spring Boot Project for Phone Management

## Description
The application exposes a REST API to manage a list of phone records.\
List operations supported:

- Create a new phone record
  - To check if the number received is valid, it was used the [AbstractAPI](https://docs.abstractapi.com/phone-validation)
- Get all phone records
- Get a phone record by Id

## Building application

### Pre-requisites

- JDK 17
- Maven 3
- Docker CLI
- Get a valid API Key to perform the phone number validation call to [AbstractAPI](https://docs.abstractapi.com/phone-validation)
  - Define the property `third-party.api.abstract.api-key` before building the service, otherwise the application will not start properly

### Running instructions

The script `start.sh` allows to build and start all the required components seamlessly.
Once executed it will perform the following actions:

- Build the Phone Management jar
- Create a docker image for the Phone Management service
- Starts the containers for a PostgreSQL server and the Phone Management Service using the `docker-compose.yaml` file
  on `docker` folder

```bash
./start.sh
```

## Rest API Documentation

Open the [Swagger API](http://localhost:8080/swagger-ui/index.html) documentation and perform the calls directly
to the service
