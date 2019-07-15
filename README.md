# Toy Robot

This Spring Boot application serves a few REST API endpoints that allow to create, position and move robots on a grid (table) of 5x5 squares. The size of the grid can be controlled with command line arguments.

## Build

Run the following command from the project root directory

`./gradlew bootJar`

## How to run

### Local Mode (file input)

This mode allows to control one robot with instructions listed in a file. The file to load should be specified as a command line argument.

There are 3 example files in the repository

`java -jar build/libs/toyrobot-0.1.0.jar --file=robot_instructions_1.txt`

`java -jar build/libs/toyrobot-0.1.0.jar --file=robot_instructions_2.txt`

`java -jar build/libs/toyrobot-0.1.0.jar --file=robot_instructions_invalid.txt`

Feel free to add additional files. The output will be displayed in the console.

| Supported commands |
|--------------------|
|  place 1, 2, NORTH |
|        left        |
|        right       |
|        move        |
|        report      |

### API mode

This mode allow to create and control multiple robots. They are stored in the memory while the API is running. This could be replaced with a DB to persist the state, however, this is outside the scope of this task. Each robot has a unique ID that should be used to control it.

#### Run with Docker

`docker network create traefik_proxy`

`docker-compose up -d`


#### Run without Docker

`java -jar build/libs/toyrobot-0.1.0.jar `

To specify grid size other than 5x5:

`java -jar build/libs/toyrobot-0.1.0.jar --gridSizeX=8 --gridSizeY=6`

#### Note

The app will be accessible on port 8080

#### Robot object

| Type   | Name        |          |
|--------|-------------|----------|
| String | id          | optional |
| int    | locationX   | required |
| int    | locationY   | required |
| String | orientation | required |

#### API Endpoints

| Action               | Method | Endpoint                | Request Body | Response Body |
|----------------------|--------|-------------------------|--------------|---------------|
| Create a new robot   | POST   | /robot                  |              | Robot         |
| Get a robot by id    | GET    | /robot/{id}             |              | Robot         |
| Get all robots       | GET    | /robots                 |              | Robot[]       |
| Place a robot        | POST   | /right/robot/{id}/place | Robot        | Robot         |
| Move a robot         | POST   | /robot/{id}/move        | Robot        | Robot         |
| Rotate a robot left  | POST   | /robot/{id}/left        | Robot        | Robot         |
| Rotate a robot right | POST   | /robot/{id}/right       | Robot        | Robot         |
| Get a robot and log  | POST   | /robot/{id}/report      | Robot        | Robot         |


## Testing

To run the tests:

`./gradlew test`
