# Task Management System

A Spring Boot-based task management system with priority-based task processing, user management, and RESTful APIs.

## Features

### Task Management
- Create, read, update, and delete tasks
- Assign tasks to users
- Set task priorities (HIGH, MEDIUM, LOW)
- Track task status (TODO, IN_PROGRESS, DONE)
- Set due dates for tasks

### User Management
- Create and manage users
- Assign tasks to users
- View tasks assigned to specific users

### Priority-based Task Processing
- Automatic task processing based on priority
- Real-time task status updates
- Configurable processing intervals
- Error handling and retry mechanism

## API Documentation

### Task APIs

#### Create Task
```http
POST /api/tasks
Content-Type: application/json

{
    "title": "Task Title",
    "description": "Task Description",
    "priority": "HIGH|MEDIUM|LOW",
    "assigneeId": 1,
    "dueDate": "2024-03-20T10:00:00"
}
```

#### Get All Tasks
```http
GET /api/tasks
```

#### Get Task by ID
```http
GET /api/tasks/{id}
```

#### Update Task Status
```http
PUT /api/tasks/{id}/status?status=TODO|IN_PROGRESS|DONE
```

#### Get Tasks by Status
```http
GET /api/tasks/status/{status}
```

#### Get Tasks by Assignee
```http
GET /api/tasks/assignee/{userId}
```

#### Reprocess Failed Tasks
```http
POST /api/tasks/reprocess
```

### User APIs

#### Create User
```http
POST /api/users
Content-Type: application/json

{
    "name": "User Name",
    "email": "user@example.com"
}
```

#### Get All Users
```http
GET /api/users
```

#### Get User by ID
```http
GET /api/users/{id}
```

#### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
    "name": "Updated Name",
    "email": "updated@example.com"
}
```

#### Delete User
```http
DELETE /api/users/{id}
```

## Task Processing System

The system includes a priority-based task processor that automatically processes tasks based on their priority level. The processor:

1. Runs on a configurable interval (default: 1 second)
2. Processes tasks in priority order (HIGH > MEDIUM > LOW)
3. Updates task status automatically
4. Handles errors and retries failed tasks

### Task Processing Flow

1. Tasks are created with TODO status
2. The processor picks up TODO tasks
3. Tasks are processed in priority order
4. Status transitions: TODO → IN_PROGRESS → DONE
5. Failed tasks are reset to TODO status for retry

### Configuration

The task processor can be configured in `application.properties`:

```properties
# Task Processor Configuration
task.processor.interval=1000  # Processing interval in milliseconds
```

## Database Configuration

The system uses PostgreSQL as the database. Configure the database connection in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_management
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

## Running the Application

1. Ensure PostgreSQL is running and configured
2. Build the application:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080 by default.

## Testing

Run the test suite:
```bash
mvn test
```

The test suite includes:
- Task creation and processing tests
- Priority-based processing tests
- User management tests
- API endpoint tests

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Docker (optional)

## Environment Variables

Create a `.env` file in the root directory with the following variables:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/task_management
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=your-256-bit-secret

# AWS S3
AWS_S3_BUCKET=your-bucket-name
AWS_REGION=us-east-1
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key

# SendGrid
SENDGRID_API_KEY=your-api-key
SENDGRID_FROM_EMAIL=your-email@example.com
```

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE task_management;
```

2. The application will automatically create the necessary tables on startup.

## Project Structure

```
src/main/java/com/taskmanagement/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Custom exceptions
├── repository/     # JPA repositories
├── security/       # Security related classes
├── service/        # Business logic
└── util/           # Utility classes
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 