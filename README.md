# Task Management System

A robust task management system built with Spring Boot, featuring user authentication, task management, file attachments, and real-time notifications.

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

## Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd task-management
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api`

## API Documentation

Once the application is running, you can access the Swagger UI at:
`http://localhost:8080/api/swagger-ui.html`

## Features

- User authentication and authorization
- Task creation and management
- Task assignment and status tracking
- File attachments
- Real-time notifications
- Email notifications
- Task search and filtering
- Task history and audit trail

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

## Testing

Run the tests using:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 