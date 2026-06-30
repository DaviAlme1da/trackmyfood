<h1 align="center">TrackMyFood</h1>

<p align="center">
  <img loading="lazy" src="https://img.shields.io/badge/status-finalizado-brightgreen?style=for-the-badge"/>
  <img loading="lazy" src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img loading="lazy" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img loading="lazy" src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img loading="lazy" src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white"/>
  <img loading="lazy" src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socketdotio&logoColor=white"/>
  <img loading="lazy" src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
</p>

Projeto de API para rastreamento de entregas de comida em tempo real, desenvolvido com **Spring Boot**, explorando autenticação, controle de acesso por papéis, mensageria assíncrona e rastreamento de localização ao vivo através de um sistema completo de pedidos e entregas.

---

## 📌 Sobre o projeto

O TrackMyFood é um backend que simula uma plataforma de delivery de comida. Clientes podem criar pedidos, entregadores podem aceitá-los e atualizar seu status, e tanto o status do pedido quanto a localização do entregador são transmitidos em tempo real via WebSocket.

O sistema possui dois tipos de usuário:
- **Customer (Cliente)**: cria pedidos e acompanha a entrega em tempo real.
- **Delivery Man (Entregador)**: aceita pedidos disponíveis, atualiza seu status e transmite sua localização GPS.

Quando um pedido é marcado como "saiu para entrega", a API calcula a rota entre o entregador e o endereço do cliente usando a API **OpenRouteService**, e publica tanto a rota quanto as atualizações de localização através do **RabbitMQ**, que são então repassadas ao cliente via **WebSocket (STOMP)**.

---

## Arquitetura

```
Cliente ── cria pedido ──► Order Service ──► PostgreSQL
                                  │
Entregador ── atualiza status ───┘
                                  │
                      calcula rota (OpenRouteService)
                                  │
                      publica rota / localização ──► RabbitMQ
                                  │
                      Consumer RabbitMQ ──► WebSocket (STOMP)
                                  │
                           Cliente (rastreamento ao vivo)
```

O projeto segue uma **arquitetura em camadas organizada por funcionalidade**:

```
api/
 ├── auth/               # login, registro, refresh token
 ├── order/               # criação e atualização de status do pedido
 ├── deliveryMan/          # gerenciamento do entregador
 ├── deliveryLocation/      # rastreamento de localização GPS
 └── route/                  # cálculo de rota (OpenRouteService)
core/
 ├── model/                  # entidades JPA
 ├── repository/              # repositórios Spring Data
 ├── services/                  # autenticação, JWT
 └── exceptions/                  # exceções customizadas
messaging/
 ├── publisher/                    # produtores RabbitMQ
 └── consumer/                       # consumidores RabbitMQ → WebSocket
```

---

## Funcionalidades

- Cadastro e login de usuários (Cliente / Entregador)
- Autenticação JWT (access token + refresh token)
- Autorização baseada em papéis com Spring Security (`@PreAuthorize`)
- Criação, listagem e atualização de status de pedidos
- Gerenciamento de status do entregador (`AVAILABLE` / `ON_DELIVERY`)
- Rastreamento de localização do entregador em tempo real
- Cálculo de rota via API OpenRouteService
- Atualizações em tempo real via WebSocket (STOMP sobre SockJS)
- Comunicação assíncrona com RabbitMQ
- Tratamento global de exceções
- Documentação da API com Swagger / OpenAPI

---

## Como rodar o projeto

O projeto é totalmente containerizado com **Docker Compose**, então não é necessário ter Java, Maven, PostgreSQL ou RabbitMQ instalados localmente.

### Pré-requisitos

- Docker
- Docker Compose
- Uma chave de API do [OpenRouteService](https://openrouteservice.org/)

### 1. Clone o repositório

```bash
git clone https://github.com/DaviAlme1da/trackmyfood.git
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com base no `.env.example`, preenchendo as variáveis necessárias:

| Variável | Descrição |
|---|---|
| `DB_URL` | URL de conexão do PostgreSQL |
| `DB_USERNAME` | Usuário do banco de dados |
| `DB_PASSWORD` | Senha do banco de dados |
| `JWT_ACCESS_SECRET` | Chave secreta para assinar o access token |
| `JWT_REFRESH_SECRET` | Chave secreta para assinar o refresh token |
| `RABBITMQ_HOST` | Host do RabbitMQ (padrão: `rabbit`) |
| `RABBITMQ_DEFAULT_USER` | Usuário do RabbitMQ |
| `RABBITMQ_DEFAULT_PASS` | Senha do RabbitMQ |
| `ORS_API_KEY` | Chave de API do OpenRouteService |

### 3. Suba o projeto com Docker Compose

```bash
docker-compose up -d
```

A API ficará disponível em `http://localhost:8080`.

---

## Endpoints da API

| Método | Endpoint | Papel | Descrição |
|---|---|---|---|
| POST | `/auth/register` | Público | Cadastra um novo cliente ou entregador |
| POST | `/auth/login` | Público | Autentica e retorna os tokens |
| POST | `/auth/refresh` | Público | Renova o access token |
| POST | `/api/orders` | Cliente | Cria um novo pedido |
| GET | `/api/orders` | Entregador | Lista os pedidos disponíveis |
| GET | `/api/orders/my` | Cliente | Lista os pedidos do cliente autenticado |
| GET | `/api/orders/{id}` | Autenticado | Busca detalhes de um pedido |
| PATCH | `/api/orders/{id}/status` | Entregador | Atualiza o status do pedido |

Documentação interativa completa disponível via Swagger em:
`http://localhost:8080/swagger-ui/index.html`

---

## Tópicos WebSocket

| Tópico | Descrição |
|---|---|
| `/topic/order/{orderId}` | Atualizações de localização do entregador em tempo real |
| `/topic/route/{orderId}` | Rota calculada entre entregador e cliente |

Os clientes se conectam através do endpoint `/ws` (SockJS).

---

## Conhecimentos Aplicados

- Spring Boot / Spring Web
- Spring Security com autenticação JWT
- Controle de acesso baseado em papéis (RBAC)
- JPA / Hibernate com herança de tabela única
- Arquitetura em camadas (Controller, Service, Repository)
- DTOs e mappers com MapStruct
- Tratamento global de exceções
- Mensageria assíncrona com RabbitMQ
- Comunicação em tempo real com WebSocket / STOMP
- Integração com API REST de terceiros (OpenRouteService)

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- WebSocket (STOMP / SockJS)
- JWT (jjwt)
- MapStruct
- Lombok
- Swagger / OpenAPI

---

## Testes

O projeto conta com testes unitários cobrindo as principais regras de negócio dos serviços (autenticação, pedidos, entregadores e localização de entrega), utilizando JUnit e Mockito, além de factories para facilitar a criação dos objetos de teste.

---

##  Autor

<p>
Desenvolvido por <strong>Davi Lucas de Almeida</strong>.<br>
Estudante de Análise e Desenvolvimento de Sistemas (ADS).
</p>
