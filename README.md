# Google GenAI Chat Application

This is a web-based chat application that uses Spring Boot and the Spring AI project to connect to Google's Generative AI models. The frontend is built using the JTE template engine and htmx for dynamic, server-rendered interactions.

## Getting Started

### Prerequisites

*   **Java 21** or later
*   **Apache Maven** 3.8 or later
*   **Google AI API Key:** You need an API key for the Google AI platform. You can get one from [Google AI Studio](https://aistudio.google.com/app/apikey).

### Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Configure API Key:**
    Open `src/main/resources/application.properties` and add your Google AI API key:
    ```properties
    spring.ai.google.genai.api-key=YOUR_API_KEY
    ```
3.  **Build the application:**
    ```bash
    mvn clean install
    ```

## Development

### Running the Application

You can run the application directly from your IDE by running the `main` method in `Application.java`, or by using the Spring Boot Maven plugin:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/chat`.

### Technology Stack

*   **Backend:** Spring Boot 3, Spring AI
*   **Frontend:** JTE (Java Template Engine), htmx
*   **Styling:** Pico.css

### Code Style and Conventions

*   **Java:** Follow standard Java best practices. The project uses Lombok to reduce boilerplate code.
*   **Templates:** JTE templates are used for server-side rendering. Keep the templates simple and focused on presentation logic.
*   **htmx:** Interactions are driven by htmx attributes in the templates. This keeps the frontend logic declarative and co-located with the HTML.

## Testing

### Running Tests

To run the existing tests, use the following Maven command:

```bash
mvn test
```

### Testing Strategy

*   **Unit Tests:** For business logic and service layers.
*   **Integration Tests:** For testing the interaction between different components, especially the `ChatController` and the Spring AI services.
*   **Frontend Testing:** Since the frontend is server-rendered, most of the testing can be done at the controller level by asserting the rendered HTML content.

## CI/CD (Continuous Integration / Continuous Deployment)

A CI/CD pipeline is essential for automating the build, test, and deployment process. Here is a sample workflow for GitHub Actions that can be adapted for this project.

Create a file named `.github/workflows/build.yml`:

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
        cache: maven

    - name: Build with Maven
      run: mvn -B package --file pom.xml

    - name: Run tests
      run: mvn test
```

This workflow will:
1.  Check out the code.
2.  Set up Java 21.
3.  Build the application and run tests on every push and pull request to the `main` branch.

For deployment, you can add steps to publish the JAR artifact to a repository or deploy it to a cloud provider.

## Maintenance

### Dependency Management

*   **Check for updates:** Regularly check for updates to the project's dependencies, especially for Spring Boot and Spring AI, to get the latest features and security patches.
    ```bash
    mvn versions:display-dependency-updates
    ```
*   **Update dependencies:** Update the versions in the `pom.xml` file and test the application thoroughly.

### Code Quality

*   **Static Analysis:** Consider adding a static analysis tool like SonarQube or Checkstyle to the build process to automatically check for code quality issues and potential bugs.

### Logging and Monitoring

*   **Logging:** The application uses SLF4J for logging. The logging level can be configured in `application.properties`.
*   **Monitoring:** The Spring Boot Actuator is included in the project. It provides several endpoints for monitoring the application's health and metrics. You can access them at `/actuator`.
