# 🔐 Auth Service

API de autenticação e autorização desenvolvida com foco em arquitetura limpa, segurança e boas práticas no ecossistema Java + Spring.

O serviço é responsável por:

- Cadastro de usuários.
- Login com autenticação JWT.
- Controle de permissões e roles.
- Proteção de endpoints de forma declarativa.
- Persistência segura de credenciais utilizando criptografia forte.
- Disparo de eventos assíncronos via mensageria para outros serviços.

---

## 🚀 Como rodar

### Pré-requisitos
- **Java 21** instalado.
- **Maven** instalado.
- **Docker** e **Docker Compose** (opcional, para rodar com dependências integradas).

### Clone o projeto
```bash
git clone https://github.com/JG-OLIVEIRA/auth-service.git
```

### Inicialização

#### Usando Docker Compose
Certifique-se de que possui os containers de MySQL e RabbitMQ disponíveis ou utilize a configuração Docker para subir a infraestrutura necessária:
```bash
docker-compose up -d
```

#### Usando Maven (Local)
Configure as variáveis de ambiente necessárias (como `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` e `RABBITMQ_URL`) em seu ambiente e execute:
```bash
mvn spring-boot:run
```

---

## 📡 Endpoints principais

Todos os endpoints da API pública utilizam o prefixo `/api/v1`.

| Método | Rota                         | Descrição                                                        | Requer Autenticação | Role Requerida |
| :---   | :---                         | :---                                                             | :---:               | :---:          |
| POST   | `/api/v1/auth/register`      | Cadastro de novo usuário e publicação de evento no RabbitMQ       | Não                 | -              |
| POST   | `/api/v1/auth/login`         | Autenticação com e-mail e senha, retornando token JWT            | Não                 | -              |

---

## ✨ Features

- **✅ Autenticação baseada em JWT**: Geração de tokens JWT seguros para autenticação sem estado (stateless).
- **✅ Controle de Acesso Baseado em Roles**: Restrição de acesso aos endpoints `/api/v1/users/**` exclusivamente para usuários com a role `ROLE_ADMIN`.
- **✅ Integração Assíncrona com RabbitMQ**: Publicação automatizada de mensagens de boas-vindas na fila (`default.email`) após o cadastro com sucesso de um novo usuário.
- **✅ Paginação com Spring Data**: Consultas otimizadas na listagem de usuários com suporte a parâmetros de página e tamanho de página.
- **✅ Migrations com Flyway**: Controle de histórico estruturado de banco de dados por meio de scripts SQL versionados.
- **✅ Criptografia Argon2**: Armazenamento seguro de senhas através do algoritmo de criptografia Argon2.
- **✅ Validação de Dados**: Validação robusta de payloads de entrada com Bean Validation (Jakarta Validation).
- **✅ Tratamento Global de Exceções**: Manipulador de erros centralizado que retorna respostas padronizadas e limpas para os clientes da API.
- **✅ Estrutura Modular `package-by-feature`**: Organização de pacotes por domínio funcional para facilitar a manutenibilidade.

---

# 🛠️ Tech Stack

## Core Backend
- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Security** (Segurança e autorização)
- **Spring Web** (Desenvolvimento de APIs RESTful)
- **Spring Data JPA** (Camada de persistência e paginação)
- **Spring AMQP (RabbitMQ)** (Mensageria e comunicação assíncrona)
- **Spring Actuator** (Métricas e monitoramento de integridade)

## Banco de Dados & Utilitários
- **MySQL** (Banco de dados relacional robusto e performático)
- **Flyway** (Gerenciamento de migrações de banco de dados)
- **Lombok** (Redução de boilerplate de código)
- **Argon2** (Algoritmo avançado para hashing de senhas)
- **Java-JWT (Auth0)** (Criação e validação de tokens JWT)
- **UUID Creator** (Gerador eficiente de identificadores UUID)

---

# 📂 Project Structure

O projeto adota o padrão de organização **Package-by-Feature** para isolar as responsabilidades por contextos de domínio e facilitar a evolução modular do código.

```text
src/main/java/dev/jorge/projects/auth
├── AuthServiceApplication.java
├── config
│   ├── AuthConfig.java (Configuração de criptografia e beans de autenticação)
│   ├── JWTUserData.java (Dados de usuário extraídos do token JWT)
│   ├── RabbitMQConfig.java (Configuração de mensageria RabbitMQ e Jackson)
│   ├── SecurityConfig.java (Filtros e regras de autorização do Spring Security)
│   ├── SecurityFilter.java (Filtro de requisição para validação do JWT)
│   └── TokenConfig.java (Configuração e geração/validação de tokens JWT)
├── controller
│   └── AuthController.java (Endpoints públicos de autenticação e registro)
├── dto
│   ├── request
│   │   ├── LoginRequest.java (Payload para login)
│   │   └── RegisterUserRequest.java (Payload para registro de usuário)
│   └── response
│       ├── EmailResponse.java (Dados de envio de e-mail/evento)
│       ├── ExceptionResponse.java (Payload para erros de exceção)
│       ├── LoginResponse.java (Payload contendo o token gerado)
│       └── RegisterUserResponse.java (Payload contendo dados do usuário criado)
├── enums
│   ├── ExceptionDetails.java (Detalhes catalogados das exceções)
│   └── Role.java (Perfis de usuário: ROLE_USER, ROLE_ADMIN)
├── exception
│   ├── UserAlreadyExistsException.java (Lançada ao registrar e-mail duplicado)
│   └── UserNotFoundException.java (Lançada se credenciais não forem encontradas)
├── handler
│   └── GlobalExceptionHandler.java (Tratamento global e padronização de erros)
├── model
│   └── User.java (Entidade de banco de dados que implementa UserDetails e Serializable)
├── producer
│   └── AuthProducer.java (Produtor que publica eventos de boas-vindas no RabbitMQ)
├── repository
│   └── UserRepository.java (Interface JPA de acesso a dados da tb_users)
└── service
    └── AuthService.java (Serviço com lógica de registro, autenticação e geração de JWT)
```

---

# ⚙️ CI/CD & Deploy

O projeto conta com um pipeline de Integração e Entrega Contínua (CI/CD) totalmente automatizado via **GitHub Actions** ([deploy.yml](file:///c:/Projects/auth-service/.github/workflows/deploy.yml)), disparado a cada push na branch `main`.

## 🚀 Fluxo do Pipeline

O pipeline é composto por dois estágios (jobs):

1. **Build and Push to ECR (`build-ecr`)**:
   - Realiza a autenticação na AWS via **OIDC (OpenID Connect)** assumindo a role IAM `GitHubActionsRepoApp` de forma segura, sem armazenar credenciais estáticas de longa duração.
   - Compila a imagem Docker utilizando a `Dockerfile` multi-stage contida no repositório.
   - Aplica tags à imagem baseando-se no ambiente (`prod` para produção na branch `main`) e no hash curto do commit Git.
   - Realiza o push da imagem compilada para o repositório correspondente no **Amazon ECR**.

2. **Deploy to EC2 via SSH (`deploy-ssh`)**:
   - Conecta-se à instância **Amazon EC2** via SSH utilizando a chave privada configurada.
   - Realiza a autenticação docker no **Amazon ECR** a partir da instância EC2.
   - Faz o pull da nova imagem Docker gerada no estágio anterior.
   - Interrompe e remove o container anterior (`auth-service`), caso esteja em execução.
   - Inicializa o novo container em segundo plano (background) mapeando a porta pública `80` da EC2 para a porta interna `8081` do container, configurando as variáveis de ambiente necessárias.

## 🔑 Configurações Requeridas (GitHub Secrets & Variables)

Os parâmetros dinâmicos de infraestrutura e segredos confidenciais devem ser configurados no GitHub sob a guia **Settings > Secrets and variables > Actions**:

### GitHub Secrets (Segredos) 🔒
* `AWS_ACCOUNT_ID`: ID numérico da conta AWS para montagem da URI do ECR e ARN da Role.
* `INSTANCE_KEY`: Conteúdo da chave privada SSH (`.pem`) para acesso à instância EC2.
* `ELASTIC_IP`: Endereço IP público estático associado à instância EC2.
* `DATABASE_URL`: URI JDBC de conexão com o banco de dados MySQL ativo na nuvem.
* `DATABASE_USERNAME`: Nome do usuário administrador para acesso ao MySQL.
* `DATABASE_PASSWORD`: Senha secreta de acesso ao MySQL.
* `RABBITMQ_URL`: String de conexão para o broker de mensageria RabbitMQ.

### GitHub Variables (Variáveis) ⚙️
* `AWS_REGION`: Região da AWS que hospeda os recursos em nuvem (ex: `us-east-1`).

---

# 🔄 De-Para (Resumo de Mudanças do Projeto)

Abaixo está o mapeamento detalhado ("De-Para") das alterações recentes realizadas no projeto, servindo como guia de referência rápida para a migração/evolução do ecossistema:

| Componente / Aspecto | Estado Anterior (De) | Estado Atual (Para) | Racional / Detalhes |
| :--- | :--- | :--- | :--- |
| **Banco de Dados** | PostgreSQL | MySQL (imagem `mysql:9.7.0`) | Migração de infraestrutura de banco de dados. |
| **Containers (Local)** | Docker Compose com PostgreSQL | Docker Compose configurado com MySQL e RabbitMQ | Facilidade para subir o ambiente local completo com `docker-compose up -d`. |
| **Dependências DB** | `org.postgresql:postgresql` | `com.mysql:mysql-connector-j` e `org.flywaydb:flyway-mysql` | Atualização dos drivers e suporte às migrations do Flyway no MySQL. |
| **Versão Spring Boot** | `3.3.1` | `4.0.6` | Atualização de segurança e recursos do ecossistema Spring. |
| **Organização de Código** | Pacotes segregados por módulos aninhados (`common`, `security`, `user`) | Estrutura de pacotes plana e direta (`config`, `controller`, `dto`, etc.) sob o pacote raiz | Maior simplicidade de importações e facilidade de localização de classes. |
| **Entidade de Usuário** | Atributo identificador chamado `userId` | Atributo identificador renomeado para `id` | Padronização e simplificação de nomenclatura no modelo de dados. |
| **Serialização do Usuário** | Entidade `User` sem implementação Serializable | Entidade `User` implementa `Serializable` com `serialVersionUID = 1L` | Requisito do Spring Security para armazenamento e replicação de sessões do usuário. |
| **HATEOAS** | Integrado às respostas do User (Spring HATEOAS) | Removido completamente da API | Simplificação das respostas JSON e eliminação de acoplamento com hipermídia. |
| **Endpoints Removidos** | `GET /api/v1/users` e `GET /api/v1/users/{userId}` (Admin) | Apenas endpoints públicos `/api/v1/auth/register` e `/api/v1/auth/login` | Foco exclusivo do microserviço na autenticação e segurança. |