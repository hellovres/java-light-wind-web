# AGENTS.md

## Build, Test, and Lint Commands

### Maven Commands (primary build tool)
```bash
# Build the project
mvn clean install

# Run the application (development mode)
mvn spring-boot:run

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Run a specific test method
mvn test -Dtest=ClassName#methodName

# Package without tests
mvn clean package -DskipTests

# Clean build artifacts
mvn clean
```

### Running Single Tests
- Test class: `mvn test -Dtest=UserServiceTest`
- Test method: `mvn test -Dtest=UserServiceTest#testRegister`
- Multiple tests: `mvn test -Dtest=UserServiceTest,AuthControllerTest`

## Code Style Guidelines

### Imports
- Group imports: standard library (java.*), third-party (jakarta.*, org.springframework.*), project imports (com.lightwind.*)
- Use `jakarta.*` namespace (not `javax.*`)
- Avoid wildcard imports (e.g., `import com.lightwind.*`)

### Naming Conventions
- **Classes**: PascalCase (UserService, AuthController, SecurityConfig)
- **Interfaces**: PascalCase with descriptive names (UserRepository, AuthService)
- **Methods**: camelCase, descriptive verbs (findByUsername, registerPost, filterChain)
- **Variables**: camelCase (username, passwordEncoder, users)
- **Constants**: UPPER_SNAKE_CASE (MAX_RETRY_COUNT)
- **Files**: PascalCase.java matching class name

### Type Safety & Annotations
- Use Lombok annotations to reduce boilerplate:
  - `@Data` for getters, setters, equals, hashCode, toString
  - `@AllArgsConstructor` for all-args constructor
  - `@NoArgsConstructor` for no-args constructor
- Use Spring annotations appropriately:
  - `@Service` for service layer components
  - `@Controller` for web controllers returning views
  - `@RestController` for API controllers returning JSON
  - `@Configuration` for configuration classes
  - `@Bean` for bean definitions
  - `@Autowired` for dependency injection (field or constructor preferred)

### Dependency Injection
- Constructor injection preferred for required dependencies
- Field injection (`@Autowired` on fields) acceptable for optional or configuration dependencies
- Use `@PostConstruct` for initialization logic after dependency injection

### Error Handling
- Use `RuntimeException` or custom runtime exceptions for business logic validation
- Provide descriptive Chinese error messages for user-facing errors (e.g., "用户名已存在")
- Wrap checked exceptions in runtime exceptions where appropriate
- Return user-friendly error messages in controller responses

### Security
- Passwords must be encrypted using `BCryptPasswordEncoder`
- Never store or log plaintext passwords
- Use Spring Security for authentication and authorization
- Configure `SecurityFilterChain` for HTTP security rules

### Formatting
- Indentation: 4 spaces (no tabs)
- Line length: reasonable (~100-120 chars), avoid extremely long lines
- Empty lines: 1 blank line between methods, 2 between class members for grouping
- Brace style: K&R style (opening brace on same line)

### Logging
- Use SLF4J with `LoggerFactory`
- Log levels:
  - DEBUG: Detailed diagnostics
  - INFO: Key application events
  - WARN: Recoverable issues
  - ERROR: Failures requiring attention
- Log sensitive information with care (no passwords, tokens)

### Testing (JUnit 5 + Spring Boot Test)
- Use `@SpringBootTest` for integration tests
- Use `@WebMvcTest` for controller layer tests
- Use `@ExtendWith(MockitoExtension.class)` for unit tests
- Test naming: `methodName_scenario_expectedResult` (e.g., `register_duplicateUsername_throwsException`)

## Project Structure
```
src/
├── main/
│   ├── java/com/lightwind/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # Web controllers
│   │   ├── entity/          # Domain entities
│   │   ├── service/         # Business logic
│   │   └── *Application.java # Main application class
│   └── resources/
│       ├── application.yml  # Spring configuration
│       ├── static/          # Static assets
│       └── templates/       # Thymeleaf templates
└── test/                    # Test sources
```

## Technology Stack
- Java 17
- Spring Boot 3.2.0
- Spring Security (authentication/authorization)
- Thymeleaf (server-side templating)
- Lombok (code generation)
- JUnit 5 + Mockito (testing)
- Maven (build tool)
