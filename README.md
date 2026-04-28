# Auth Service

O Auth Service é um serviço responsável pela autenticação (processo de verificar a identidade do usuário) e autorização (processo de verificar suas permissões). Nele, o usuário pode registrar informações pessoais básicas, como nome completo, e-mail e senha, que serão utilizadas no processo de login.

## 🚀 Tecnologias

Este projeto utiliza as seguintes tecnologias:

- **Java 21** (Linguagem/Plataforma)
- **PostgreSQL** (Banco de dados relacional)
- **Flyway** (Migrations)
- **Lombok** (Biblioteca)

Ecossistema Spring:

1. **SPRING BOOT**

- Automatic Run, Configurations and Dependencies 
- Embedded Application Server

2. **SPRING WEB**

- API RESTful 
- Validation

3. **SPRING HATEOAS**

4. **SPRING DATA**

- JPA
  - Pagination
  - Repositories

5. **SPRING SECURITY**

- Authorization
   - Roles (ROLE_USER, ROLER_ADMIN)
   - Permissions
  
- Authentication
   - Token Authentication
      - JWT

## 🛠️ Pré-requisitos

Para rodar o projeto localmente, você precisará de:

1. **JDK 21** instalado.
2. **Maven 3.x** (ou usar o `mvnw` incluso).
3. **PostgreSQL**
4. **Docker** .

## ⚙️ Configuração

O projeto utiliza variáveis de ambiente para configurações sensíveis. Você deve definir as seguintes variáveis:

| Variável            | Descrição                                         | Exemplo                                 |
|:--------------------|:--------------------------------------------------|:----------------------------------------|
| `DATABASE_URL`      | URL de conexão do Postgres                        | `jdbc:postgresql://localhost:5432/auth` |
| `DATABASE_USERNAME` | Usuário do banco                                  | `postgres`                              |
| `DATABASE_PASSWORD` | Senha do banco                                    | `sua_senha`                             |


## 🏃 Como rodar

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/JG-OLIVEIRA/auth-service.git
   cd auth-service
   ```

2. **Subir os serviços (Docker):**
   ```bash
   docker-compose up -d
   ```

3. **Rodar a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

## 📂 Estrutura do Projeto

O projeto segue o padrão **Package-by-Feature**:

- `common`: Componentes compartilhados, tratamento de erros global e enums.
- `handler`: Tratamento de exceções dentro da app.
- `security`: Tratamento de login, registro e segurança.
- `user`: Entidades, repositórios e lógica de usuários.

---
Desenvolvido para a comunidade UERJ.
