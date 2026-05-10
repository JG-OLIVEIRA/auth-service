# 🔐 Auth Service

API de autenticação e autorização desenvolvida com foco em arquitetura limpa, segurança e boas práticas no ecossistema Java + Spring.

O serviço é responsável por:

- Cadastro de usuários
- Login com autenticação JWT
- Controle de permissões e roles
- Proteção de endpoints
- Persistência segura de credenciais

---

## 🚀 Como rodar

Usando docker:
```bash
docker-compose up -d
```

Usando mvn: 
```bash
mvn spring-boot:run
```

## 📡 Endpoints principais

| Método | Rota              | Descrição         |
|--------|-------------------|-------------------|
| POST   | /auth/register    | Cadastro          |
| POST   | /auth/login       | Login com JWT     |
| GET    | /users            | Lista (ADMIN)     |

---

## ✨ Features

- ✅ Registro de usuários
- ✅ Login com JWT
- ✅ Controle de acesso baseado em Roles
- ✅ Sistema de permissões
- ✅ Validação de dados
- ✅ Paginação com Spring Data
- ✅ Migrations com Flyway
- ✅ Estrutura modular `package-by-feature`
- ✅ API RESTful
- ✅ Tratamento global de exceções

---

# 🛠️ Tech Stack

## Backend

- **Java 21**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Spring Web**
- **Spring HATEOAS**
- **PostgreSQL**
- **Flyway**
- **Lombok**
- **Docker**

---

# 🧩 Spring Ecosystem

## Spring Boot

- Auto Configuration
- Embedded Server
- Dependency Management

## Spring Web

- REST API
- Request Validation

## Spring Data JPA

- Repositories
- Pagination
- ORM Mapping

## Spring Security

### Authentication

- JWT Authentication
- Token-based Security

### Authorization

- Roles:
    - `ROLE_USER`
    - `ROLE_ADMIN`

- Permissions Management

---

# 📂 Project Structure

O projeto segue o padrão arquitetural **Package-by-Feature**, tornando a manutenção e escalabilidade mais simples.

```bash
src/main/java
├── common
├── handler
├── security
└── user