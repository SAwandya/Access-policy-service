# Policy-as-a-Service Platform

A comprehensive microservices-based platform for centralized policy management, evaluation, and governance across enterprise applications.

## Overview

This Policy-as-a-Service (PaaS) platform provides a centralized way to define, manage, and enforce policies across your entire application ecosystem. By extracting policy logic from individual applications into a dedicated service layer, you can ensure consistent policy enforcement, simplified compliance, and greater business agility.

![Architecture Overview](https://github.com/yourusername/policy-as-a-service/raw/main/docs/images/architecture.png)

## Key Features

- **Centralized Policy Management**: Define policies once, apply them everywhere
- **Real-time Policy Evaluation**: High-performance decision engine
- **Comprehensive Auditing**: Track all policy decisions for compliance
- **Flexible Integration**: Connect with any application through REST APIs and events
- **Role-Based Access Control**: Fine-grained permission management
- **Multi-tenancy Support**: Serve multiple business units or customers with isolated policies
- **Event-driven Architecture**: React to system changes through message-based communication

## Microservices Architecture

The platform consists of the following microservices:

| Service | Description | Tech Stack |
|---------|-------------|------------|
| **Authority Service** | Core policy definition and management | Spring Boot, MySQL |
| **Authentication Service** | User identity and access management | Spring Boot, OAuth2 |
| **Policy Evaluation Service** | Real-time policy decision engine | Spring Boot, OPA |
| **Resource Service** | Resource registry and management | Spring Boot, MongoDB |
| **Audit Service** | Comprehensive logging and reporting | Spring Boot, Elasticsearch |
| **API Gateway** | Unified entry point and routing | Spring Cloud Gateway |
| **Notification Service** | Alert and notification management | Spring Boot, RabbitMQ |
| **Dashboard Service** | Web UI for policy management | Spring Boot, React |
| **Analytics Service** | Policy usage insights and reporting | Spring Boot, Apache Spark |
| **Integration Service** | External system connectors | Spring Boot, Apache Camel |

## Event-Driven Communication

Services communicate through both REST APIs (for synchronous requests) and RabbitMQ (for asynchronous events), allowing for loose coupling and high scalability.

Key event types:
- Policy lifecycle events (created, updated, deleted)
- Decision events (permit, deny, indeterminate)
- Resource events (registered, modified, removed)
- System events (config changes, deployments)

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Kubernetes cluster (for production deployment)
- Java 17 or higher
- MySQL 8.0+

### Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/policy-as-a-service.git
   cd policy-as-a-service
   ```

2. Start the infrastructure services:
   ```bash
   docker-compose up -d mysql rabbitmq elasticsearch
   ```

3. Start each service in development mode:
   ```bash
   cd authority-service
   ./mvnw spring-boot:run
   ```

4. Access the dashboard at [http://localhost:8080](http://localhost:8080)

### Kubernetes Deployment

We provide Helm charts for deploying the entire platform to Kubernetes:

```bash
helm repo add policy-as-a-service https://yourusername.github.io/policy-as-a-service/charts
helm install paas policy-as-a-service/policy-platform
```

See the [deployment documentation](docs/deployment.md) for detailed instructions.

## Usage Examples

### Simple Policy Evaluation

```java
// Client code example
PolicyEvaluationRequest request = PolicyEvaluationRequest.builder()
    .subject("user:123")
    .action("view")
    .resource("document:456")
    .context(Map.of("department", "finance"))
    .build();

PolicyDecision decision = policyClient.evaluate(request);

if (decision.isPermitted()) {
    // Allow the action
} else {
    // Deny the action
}
```

### Policy Definition

```json
{
  "name": "Finance Document Access",
  "description": "Controls who can access finance documents",
  "policyDefinition": {
    "target": {
      "resource": {
        "type": "document",
        "attributes": {
          "department": "finance"
        }
      }
    },
    "rules": [
      {
        "effect": "permit",
        "condition": "subject.department == resource.department || subject.role == 'admin'"
      }
    ]
  }
}
```

See the [examples directory](examples/) for more use cases.

## API Documentation

API documentation is available via Swagger UI when running each service:

- Authority Service: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Policy Evaluation Service: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

## Contributing

Contributions are welcome! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or support, please open an issue or contact [your-email@example.com](mailto:your-email@example.com).
