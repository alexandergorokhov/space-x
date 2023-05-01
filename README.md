## The Space-x Challenge
# Requirements
Java -17 (open jdk)
Maven  -3.8.2
Generate trello api key and token from https://trello.com/app-key
# How to build
mvn clean install
# How to run
mvn spring-boot:run -Dspring-boot.run.arguments="--trello.api.key='{trello.api.key}' --trello.api.token='{trello.api.token}'"

Or you can use your favorite IDE to run the application providing 2 environment variables:
trello.api.key
trello.api.token
# How to use
The application will run on port 3000, or can be changed in application.properties file.
The application exposes 6 endpoints, you can check them via swagger: http://localhost:3000/swagger-ui/index.html

/board - POST - creates a new board

/list - POST - creates a new list on a board
/label - POST - creates a new label on a list

/board/task - POST - creates a new task on a list

/board/issue - POST - creates a new issue on a list

/board/bug - POST - creates a new bug on a list

## 1
Generate api token and key from https://trello.com/app-key
## 2
Create and organization: http://localhost:3000/api/v1/organization/?organizationName=Sample
## 3
Create a board sample :
http://localhost:3000/api/v1/board?organizationId=644fef51683e38a41b3d7c5&boardName=BoardSample
## 4
Create a list on a board sample :
http://localhost:3000/api/v1/list/?boardId=6448cfeedf76c99f8242d71&listName=space2
## 5
Create a label on a list sample :
http://localhost:3000/api/v1/label/?boardId=644dcfeedf76c99f8242d71&labelName=Bug&labelColor=red
## 6
Create a issue on a list sample :
http://localhost:3000/api/v1/board/issue?listId=64d8dcf7cb585a3d527d837
{
"type":"issue",
"description":"Pilots need to know the water level.",
"title":"Create water level check system"
}
or

Create a bug on a list sample :
http://localhost:3000/api/v1/board/bug?listId=644d8dc7cb585a3d527d837&boardId=644d8cfedf76c99f8242d71
{
"type":"bug",
"description":"The cleanning mechanism is now working."
}
or

Create a task on a list sample :
http://localhost:3000/api/v1/board/task?listId=644d8df7cb585a3d527d837&boardId=644d8ceedf76c99f8242d71
{
"type":"task",
"title":"Clean the suit",
"category":"Research"
}
Category must match with available label name on the list

# Testing
The application has unit tests and collaboration tests.
The scope of testing is to show the ability to write tests, not to cover all the code.

# Architecture
The application is built using layered architecture.

# Improvements
Introduction of AOP for Cross Cutting concerns  (logging,security).
Introduction of persistence layer for storing the data.
Introduction of caching layer for caching the data coming for the external API.
Introduction of circuit breaker for the external API.