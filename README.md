# 🛡️ Policy Authority Service

Centralized, cloud-native microservice for authoring and managing access control policies (RBAC/ABAC) in distributed systems. Built with Spring Boot, MySQL, Docker, Kubernetes, and Skaffold.

---

## 🚀 Features

- **Policy Authoring:** Create, update, delete, and list access control policies (RBAC/ABAC/Custom).
- **RESTful API:** Clean, documented endpoints for integration.
- **MySQL Persistence:** Reliable storage for all policy data.
- **Kubernetes Native:** All manifests provided; deployable to any K8s cluster.
- **Skaffold Integration:** One-command local development and deployment.
- **Port Forwarding:** Easy local access to API and DB.

---

## 📂 Project Structure

access-policy-project/
authority-service/ # Spring Boot microservice (source code, Dockerfile)
k8s/ # All Kubernetes manifests
authority-service/
*.yaml
namespaces.yaml
skaffold.yaml # Skaffold config for build/deploy/dev
README.md

---

## 🏗️ Requirements

- [Java 17+](https://adoptopenjdk.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [Kubernetes Cluster](https://kubernetes.io/) (Minikube, Docker Desktop, or Cloud)
- [Skaffold](https://skaffold.dev/)

---

## ⚡ Quick Start

### 1. **Clone the Repository**

git clone https://github.com/yourusername/access-policy-project.git
cd access-policy-project


### 2. **Build the Application**

cd authority-service
mvn clean package
cd ..


### 3. **Start Skaffold (Build + Deploy to K8s)**


- **API available at:** [http://localhost:8080](http://localhost:8080)
- **MySQL available at:** `localhost:33065`

### 4. **Test API Endpoints**

- List policies: `GET http://localhost:8080/api/policies`
- Create policy: `POST http://localhost:8080/api/policies`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## ⚙️ Configuration

| Variable                   | Description                        | Default/Example                |
|----------------------------|------------------------------------|-------------------------------|
| SPRING_DATASOURCE_URL      | JDBC URL for MySQL                 | jdbc:mysql://mysql:3306/authoritydb |
| SPRING_DATASOURCE_USERNAME | MySQL user                         | authorityuser                 |
| SPRING_DATASOURCE_PASSWORD | MySQL password                     | authoritypass                 |
| SPRING_JPA_HIBERNATE_DDL_AUTO | JPA schema mode                  | update                        |

All sensitive values are managed via Kubernetes Secrets.

---

## 🐳 Docker & Kubernetes

- **Dockerfile:** Located in `authority-service/`
- **Kubernetes YAMLs:** Located in `k8s/authority-service/` and `k8s/namespaces.yaml`
- **Namespace:** All resources are deployed in the `authority-service` namespace.

---

## 🔗 Useful Commands

| Action                | Command                                                           |
|-----------------------|-------------------------------------------------------------------|
| Build JAR             | `cd authority-service && mvn clean package`                       |
| Build & Deploy (K8s)  | `skaffold dev`                                                   |
| Access API            | `http://localhost:8080/api/policies`                             |
| Access MySQL          | `mysql -h 127.0.0.1 -P 33065 -u authorityuser -p`                |
| Tear down             | `skaffold delete`                                                |

---

## 🧪 Testing

- Unit and integration tests via Maven:  

cd authority-service
mvn test

- API testing via [Postman](https://www.postman.com/) or `curl`:

curl http://localhost:8080/api/policies


---

## 🗺️ Roadmap

- [ ] Policy Evaluation Service microservice
- [ ] Admin Dashboard (UI)
- [ ] Audit Logging Service
- [ ] ABAC condition builder UI
- [ ] Helm charts for production deployment

---

## 👤 Maintainer

**Your Name**  
[your.email@example.com](mailto:your.email@example.com)

---

## 📄 License

This project is licensed under the MIT License.

---

## 💡 References

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Skaffold](https://skaffold.dev/)
- [Kubernetes](https://kubernetes.io/)
- [Sample Microservices with K8s](https://github.com/piomin/sample-spring-microservices-kubernetes)

---

> _“The README is the front door of your service-make it welcoming and complete.”_


