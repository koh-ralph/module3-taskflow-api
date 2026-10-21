# TaskFlow API

A small task-management backend built with Spring Boot for the NTU AI Engineering Module 3 assignment.

## Stack

- Java 21, Maven, Spring Boot
- Spring Web, Lombok, Spring Boot DevTools
- Spring AI (OpenAI) for the optional summary endpoint (Task 6)

## Project structure

```
src/main/java/sg/edu/ntu/taskflowapi/
  controller/TaskController.java   REST endpoints
  service/TaskService.java         business logic
  repository/TaskRepository.java   in-memory store (HashMap + id counter)
  model/Task.java                  plain POJO (Lombok)
```

## Run

Requires JDK 21 and Maven.

The app reads the OpenAI key from the `OPENAI_API_KEY` environment variable
(`application.properties` has `spring.ai.openai.api-key=${OPENAI_API_KEY}`).
The real key is never stored in the project. Set the variable before starting:

```bash
export OPENAI_API_KEY=your-key-here   # bash / Git Bash
```

Then:

```bash
mvn spring-boot:run
```

The server starts on <http://localhost:8080>. If that port is already in use
on your machine, pick another one:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

If you only want to test Tasks 1 to 5, set `OPENAI_API_KEY` to any dummy value
such as `x`; the app still starts and only `/api/tasks/summary` will fail.

### Using OpenRouter instead of OpenAI (optional)

OpenRouter speaks the same API as OpenAI and offers free models, so the summary
endpoint can run at no cost. A Spring profile switches the client over without
any code change:

```bash
export OPENAI_API_KEY=sk-or-v1-...          # your OpenRouter key
export SPRING_PROFILES_ACTIVE=openrouter
mvn spring-boot:run
```

The profile file `src/main/resources/application-openrouter.properties` sets the
OpenRouter address and the `openrouter/free` model. Without the profile the app
talks to OpenAI directly, as the assignment describes.

## Endpoints

| Method | Path                       | What it does            | Status |
|--------|----------------------------|-------------------------|--------|
| GET    | /api/tasks                 | Get all tasks           | 200    |
| GET    | /api/tasks/{id}            | Get one task by id      | 200    |
| POST   | /api/tasks                 | Create a new task       | 201    |
| PUT    | /api/tasks/{id}            | Update an existing task | 200    |
| DELETE | /api/tasks/{id}            | Delete a task           | 200    |
| PUT    | /api/tasks/{id}/complete   | Mark a task as complete | 200    |
| GET    | /api/tasks/summary         | AI summary (Task 6)     | 200    |

Unknown ids return 404.

Example request body for POST and PUT:

```json
{
  "title": "Finish Module 3 assignment",
  "description": "Build the TaskFlow API and push to GitHub",
  "completed": false
}
```

## Testing with Postman

Import `postman/TaskFlow.postman_collection.json`. It has one request per
endpoint, in a sensible order: create a few tasks first, then get, update,
complete, delete and summary.
