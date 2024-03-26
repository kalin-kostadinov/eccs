# Hubject Electric Car Charging Station

RESTful interface for the storage and retrieval of charging station data using JAVA and Spring framework.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Flyway Scripts](#flyway-scripts)
- [Usage](#usage)
- [Author](#author)

## Prerequisites

Before running the application, ensure you have the following installed:

- JDK 17
- Docker
- Maven

## Installation

To run the application locally, follow these steps:

1. Clone the repository:
```bash
git clone https://github.com/kalin-kostadinov/eccs.git
```
2. Navigate to the project directory and then to the /build directory
3. Start the PostgreSQL database container by running the following command:
###    - Build the image (execute in the Dockerfile directory):
    docker build -t my_postgres_image .
###    - Run the container:
    docker run --name my_postgres_container -e POSTGRES_DB=mydatabase -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -p 5432:5432 -d my_postgres_image
###    - Verify the container status:
    docker ps
4. Build the project using Maven: 
```mvn clean install```

5. Run the application
    - Access the application at http://localhost:8081/eccs

## Flyway Scripts

The application uses Flyway for database schema migration. Flyway scripts are located in the
src/main/resources/db/migration directory. These scripts automatically populate the database tables with mock data,
ensuring that the application starts with pre-defined data.

## Usage

#### Swagger generates documentation of REST APIs, which is accessible using this [link](http://localhost:8081/eccs/swagger-ui/index.html)

Once the application is running, you can access the following endpoints:

- `GET /eccs/stations/{id}`: Gets a station by ID.
- `GET /eccs/stations/zipcode/{zipcode}`: Gets a station by Zipcode / Postal code.
- `GET /eccs/stations/?latitude={latitude}&longitude={longitude}&maxDistance={maxDistance}`: Search stations by geolocation perimeter.


- `POST /eccs/stations/`: Add a new charging station.

Note: API have configuration for prefix: "/eccs". You can find it in application.properties

## Author

### Kalin Kostadinov [Github](https://github.com/kalin-kostadinov)
