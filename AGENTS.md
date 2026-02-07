# AGENTS.md - Java Web Development Guidelines

This document contains guidelines for agentic coding agents working in this Java web project repository.

## Project Structure

```
java-light-wind-web/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/lightwind/web/
│   │   │       ├── controller/     # REST API controllers
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/     # Data access layer
│   │   │       ├── model/          # Entity classes and DTOs
│   │   │       ├── config/         # Configuration classes
│   │   │       └── util/           # Utility classes
│   │   └── resources/
│   │       ├── application.yml     # Main configuration
│   │       ├── application-dev.yml # Development config
│   │       └── application-prod.yml # Production config
│   └── test/
│       └── java/
│           └── com/lightwind/web/
├── pom.xml                         # Maven configuration
└── README.md                       # Project documentation
```

## Build and Development Commands

### Maven Commands
- **Build project**: `mvn clean compile`
- **Run tests**: `mvn test`
- **Run single test**: `mvn test -Dtest=ClassName#methodName`
- **Package application**: `mvn clean package`
- **Run application**: `mvn spring-boot:run`
- **Run with specific profile**: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`

### Test Commands
- **Run all tests**: `mvn test`
- **Run integration tests**: `mvn test -Dtest="*IntegrationTest"`
- **Run unit tests only**: `mvn test -Dtest="*Test" -Dtest="!*IntegrationTest"`
- **Generate test coverage**: `mvn jacoco:report`
- **Skip tests during build**: `mvn clean package -DskipTests`

## Code Style Guidelines

### Java Code Style
- **Indentation**: 4 spaces (no tabs)
- **Line length**: Maximum 120 characters
- **Brace placement**: K&R style (opening brace on same line)
- **Imports**: Organize alphabetically, no wildcards except static imports
- **Package naming**: All lowercase, reverse domain notation

### Naming Conventions
- **Classes**: PascalCase (e.g., `UserService`, `CustomerController`)
- **Methods**: camelCase (e.g., `getUserById`, `validateInput`)
- **Variables**: camelCase (e.g., `userRepository`, `customerId`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
- **Packages**: lowercase with dots (e.g., `com.lightwind.web.service`)

### File Organization
- One public class per file
- File name matches class name exactly
- Package declaration at the top
- Imports organized: static imports first, then regular imports

## Spring Boot Guidelines

### Controller Layer
- Use `@RestController` for API endpoints
- Use `@RequestMapping` at class level for base path
- Return `ResponseEntity` for proper HTTP status codes
- Use `@Validated` for request validation
- Handle exceptions with `@ExceptionHandler`

### Service Layer
- Use `@Service` annotation
- Implement business logic here
- Use `@Transactional` for database operations
- Keep methods focused and single-purpose

### Repository Layer
- Use `@Repository` annotation
- Extend `JpaRepository` or `CrudRepository`
- Define custom queries in repository interfaces
- Use `@Query` for complex queries

## Error Handling

### Exception Types
- **Business Exceptions**: Custom exceptions extending `RuntimeException`
- **Validation Errors**: Use `MethodArgumentNotValidException` handling
- **Resource Not Found**: Return 404 with clear error messages
- **Authorization Errors**: Return 403/401 with appropriate messages

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

## Testing Guidelines

### Test Structure
- Unit tests: Test individual methods in isolation
- Integration tests: Test component interactions
- Use `@SpringBootTest` for integration tests
- Use `@MockBean` for mocking dependencies
- Test naming: `methodName_expectedBehavior_whenCondition`

### Test Coverage
- Aim for 80%+ code coverage
- Test both positive and negative scenarios
- Test edge cases and boundary conditions
- Use meaningful assertions

## Configuration Management

### Profiles
- **dev**: Development environment with debug logging
- **test**: Test environment with in-memory database
- **prod**: Production environment with optimized settings

### Application Properties
- Use YAML format for configuration
- Externalize sensitive information
- Use environment variables for deployment-specific values
- Include proper database connection pooling settings

## Security Guidelines

### Authentication & Authorization
- Use Spring Security for authentication
- Implement JWT for stateless authentication
- Use role-based access control (RBAC)
- Validate all input parameters
- Sanitize data to prevent injection attacks

### API Security
- Enable HTTPS in production
- Use CORS configuration properly
- Implement rate limiting for public APIs
- Log security-relevant events

## Database Guidelines

### Entity Design
- Use JPA annotations properly
- Define appropriate relationships (@OneToMany, @ManyToOne, etc.)
- Use `@Lob` for large text fields
- Implement proper equals() and hashCode() methods
- Use `@GeneratedValue` for primary keys

### Data Access
- Use repository pattern consistently
- Implement pagination for large datasets
- Use DTOs for data transfer to avoid lazy loading issues
- Consider using database migrations (Flyway/Liquibase)

## Performance Guidelines

### Caching
- Use Spring Cache abstraction
- Implement caching for frequently accessed data
- Choose appropriate cache TTL values
- Monitor cache hit rates

### Optimization
- Use connection pooling (HikariCP)
- Implement pagination for large result sets
- Use asynchronous processing for long-running operations
- Monitor application performance metrics

## Logging Guidelines

### Log Levels
- **ERROR**: System errors that need immediate attention
- **WARN**: Unexpected situations that don't stop the application
- **INFO**: Important business events and configuration
- **DEBUG**: Detailed information for troubleshooting

### Best Practices
- Use structured logging with context
- Avoid logging sensitive information
- Use appropriate log levels for production
- Implement log rotation and archiving

## Code Review Checklist

- [ ] Code follows naming conventions
- [ ] Methods are single-purpose and well-named
- [ ] Error handling is comprehensive
- [ ] Tests cover critical paths
- [ ] No hardcoded values or magic numbers
- [ ] Proper validation of input parameters
- [ ] Security considerations addressed
- [ ] Performance implications considered
- [ ] Documentation is clear and concise
- [ ] Dependencies are necessary and secure

## Tool Configuration

This project uses:
- **Build Tool**: Maven
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (configurable)
- **Testing**: JUnit 5, Mockito, Spring Test
- **Code Quality**: SonarQube, Checkstyle
- **Documentation**: Swagger/OpenAPI 3

## Git Workflow

- Use feature branches for new development
- Commit messages should follow conventional commit format
- Create pull requests for code review
- Ensure CI/CD pipeline passes before merging
- Tag releases with semantic versioning