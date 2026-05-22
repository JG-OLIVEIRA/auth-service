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

Clone o projeto:
```bash
git clone https://github.com/JG-OLIVEIRA/auth-service.git
```

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
```

---

# ⚙️ CI/CD & Deploy

O projeto conta com um pipeline de Integração e Entrega Contínua (CI/CD) totalmente automatizado via **GitHub Actions** ([deploy.yml](file:///c:/Projects/auth-service/.github/workflows/deploy.yml)), disparado a cada push na branch `main`.

## 🚀 Fluxo do Pipeline

O pipeline é composto por dois jobs principais:

1. **Build and Push to ECR (`build-ecr`)**:
   - Autentica na AWS usando **OIDC (OpenID Connect)**, assumindo a role `GitHubActionsRepoApp` de forma segura (sem armazenar credenciais estáticas de longa duração no repositório).
   - Realiza o build da imagem Docker utilizando a `Dockerfile` multi-stage do projeto.
   - Gera uma tag única baseada no ambiente (`prod` para a branch `main`) e no hash curto do commit (ex: `prod-a1b2c3d`).
   - Envia (push) a imagem construída para o repositório correspondente no **Amazon ECR**.

2. **Deploy to EC2 via SSH (`deploy-ssh`)**:
   - Conecta-se à instância **Amazon EC2** via SSH utilizando a chave configurada.
   - Realiza a autenticação docker no **Amazon ECR** a partir da instância EC2.
   - Faz o pull da nova imagem Docker gerada no estágio anterior.
   - Para e remove o container anterior (`auth-service`), caso esteja em execução.
   - Inicializa o novo container em background mapeando a porta pública `80` da EC2 para a porta `8080` do container, configurando as variáveis de ambiente necessárias.

## 🔑 Configurações Requeridas (GitHub Secrets & Variables)

Para o funcionamento correto do pipeline, as seguintes chaves e variáveis devem ser configuradas na aba de **Settings > Secrets and variables > Actions** do repositório no GitHub:

### GitHub Secrets (Segredos) 🔒
* `AWS_ACCOUNT_ID`: O ID da sua conta AWS (utilizado na URI do ECR e no ARN da Role IAM).
* `INSTANCE_KEY`: A chave privada SSH (`.pem`) correspondente à Key Pair da instância EC2.
* `ELASTIC_IP`: O IP elástico público associado à sua instância EC2.
* `DATABASE_URL`: A string de conexão JDBC do banco de dados PostgreSQL executando na nuvem.
* `DATABASE_USERNAME`: O usuário de acesso ao banco de dados.
* `DATABASE_PASSWORD`: A senha de acesso ao banco de dados.

### GitHub Variables (Variáveis) ⚙️
* `AWS_REGION`: A região da AWS onde os recursos estão alocados (ex: `us-east-1`).