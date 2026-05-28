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
Certifique-se de que possui os containers de PostgreSQL e RabbitMQ disponíveis ou utilize a configuração Docker para subir a infraestrutura necessária:
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
| GET    | `/api/v1/users`              | Listagem paginada de todos os usuários cadastrados               | Sim                 | `ROLE_ADMIN`   |
| GET    | `/api/v1/users/{userId}`     | Detalhes de um usuário específico (suporta HATEOAS)              | Sim                 | `ROLE_ADMIN`   |

---

## ✨ Features

- **✅ Autenticação baseada em JWT**: Geração de tokens JWT seguros para autenticação sem estado (stateless).
- **✅ Controle de Acesso Baseado em Roles**: Restrição de acesso aos endpoints `/api/v1/users/**` exclusivamente para usuários com a role `ROLE_ADMIN`.
- **✅ Integração Assíncrona com RabbitMQ**: Publicação automatizada de mensagens de boas-vindas na fila (`default.email`) após o cadastro com sucesso de um novo usuário.
- **✅ HATEOAS**: Links de hipermídia dinâmicos e navegáveis incluídos nas respostas de consulta a usuários.
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
- **Spring Boot 3.3.1**
- **Spring Security** (Segurança e autorização)
- **Spring Web** (Desenvolvimento de APIs RESTful)
- **Spring HATEOAS** (Hipermídia na API)
- **Spring Data JPA** (Camada de persistência e paginação)
- **Spring AMQP (RabbitMQ)** (Mensageria e comunicação assíncrona)
- **Spring Actuator** (Métricas e monitoramento de integridade)

## Banco de Dados & Utilitários
- **PostgreSQL** (Banco de dados relacional robusto)
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
├── common
│   ├── dtos
│   ├── enums
│   └── handlers
│       └── GlobalExceptionHandler.java (Tratamento centralizado de erros)
├── security
│   ├── configs
│   │   ├── AuthConfig.java
│   │   ├── JWTUserData.java
│   │   ├── RabbitMQConfig.java (Configuração do Jackson Message Converter)
│   │   ├── SecurityConfig.java (Configuração de rotas e filtros do Spring Security)
│   │   ├── SecurityFilter.java (Filtro interceptor do JWT)
│   │   └── TokenConfig.java (Configuração de geração e validação de JWT)
│   ├── controllers
│   │   └── AuthController.java (Rotas de login e cadastro)
│   ├── dtos (Modelos de entrada e saída de autenticação)
│   ├── producers
│   │   └── AuthProducer.java (Produtor de mensagens do RabbitMQ)
│   └── services (Lógica de autenticação e registro)
└── user
    ├── controllers
    │   └── UserController.java (Rotas de administração de usuários com HATEOAS)
    ├── dtos (Modelos de resposta de usuário)
    ├── entities
    │   └── User.java (Entidade JPA mapeada)
    ├── enums
    ├── exceptions
    ├── repositories
    │   └── UserRepository.java
    └── services
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
* `DATABASE_URL`: URI JDBC de conexão com o banco de dados PostgreSQL ativo na nuvem.
* `DATABASE_USERNAME`: Nome do usuário administrador para acesso ao PostgreSQL.
* `DATABASE_PASSWORD`: Senha secreta de acesso ao PostgreSQL.
* `RABBITMQ_URL`: String de conexão para o broker de mensageria RabbitMQ.

### GitHub Variables (Variáveis) ⚙️
* `AWS_REGION`: Região da AWS que hospeda os recursos em nuvem (ex: `us-east-1`).