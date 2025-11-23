# FutureHub - Global Solution 2025/2

## 📋 Sobre o Projeto

**FutureHub** é uma plataforma de engajamento voltada para sustentabilidade e inovação, onde usuários podem compartilhar ideias, avaliar propostas de outros membros e participar de missões gamificadas geradas por Inteligência Artificial. O sistema utiliza um ranking dinâmico para incentivar a participação ativa e colaborativa da comunidade.

### Problema Identificado
Baixo engajamento de jovens em ações práticas de sustentabilidade e dificuldade em transformar ideias em ações concretas.

### Solução Proposta
Uma plataforma que combina:
- **Compartilhamento de Ideias**: Sistema de posts colaborativos sobre sustentabilidade
- **Avaliação Comunitária**: Mecanismo de feedback através de notas (1-5 estrelas)
- **Gamificação**: Ranking de usuários baseado em participação e qualidade
- **IA Generativa**: Geração automática de missões personalizadas usando Spring AI + OpenAI
- **Mensageria Assíncrona**: Processamento de eventos em tempo real via RabbitMQ

---

## 🏗️ Arquitetura

O projeto segue os princípios da **Clean Architecture**, com separação clara de responsabilidades:

```
├── web/                          # Camada de Apresentação
│   ├── controllers (REST API)
│   └── admin (MVC Thymeleaf)
├── application/                  # Camada de Aplicação
│   ├── dto/                      # Data Transfer Objects
│   └── service/                  # Regras de Negócio
├── domain/                       # Camada de Domínio
│   └── entities (Ideia, Usuario, Avaliacao, etc.)
├── infrastructure/               # Camada de Infraestrutura
│   ├── repository/               # Persistência (MongoDB)
│   ├── mongo/                    # Documentos e repos MongoDB
│   └── web/                      # Exception handlers
├── config/                       # Configurações
│   ├── SecurityConfig
│   ├── CacheConfig
│   ├── RabbitConfig
│   └── OpenApiConfig
└── messaging/                    # Eventos Assíncronos
    ├── publishers
    └── listeners
```

### Fluxo de Dados
1. **Request** → Controller (REST ou MVC)
2. **Controller** → Service (validação de DTO)
3. **Service** → Repository (persistência)
4. **Service** → Event Publisher (RabbitMQ)
5. **Event Listener** → Service (processamento assíncrono)

---

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Backend** | Java | 17 |
| **Framework** | Spring Boot | 3.5.7 |
| **Banco de Dados** | MongoDB | Latest |
| **Mensageria** | RabbitMQ | Latest |
| **Cache** | Caffeine | (Spring Boot Starter) |
| **IA Generativa** | Spring AI + OpenAI | 1.0.3 |
| **Segurança** | Spring Security | (Spring Boot Starter) |
| **Documentação API** | Springdoc OpenAPI | 2.8.4 |
| **Template Engine** | Thymeleaf | (Spring Boot Starter) |
| **Monitoramento** | Spring Actuator | (Spring Boot Starter) |
| **Validação** | Bean Validation | (Spring Boot Starter) |

---

## ⚙️ Funcionalidades Principais

### 🔐 Autenticação e Autorização
- **Spring Security** com HTTP Basic Authentication
- Roles: `USER` e `ADMIN`
- Controle de acesso granular por endpoint

### 💡 Sistema de Ideias
- CRUD completo de ideias/posts
- Filtro por área de interesse
- Busca textual por título
- Paginação e ordenação
- Cache inteligente (Caffeine)

### ⭐ Sistema de Avaliações
- Avaliação de ideias (1-5 estrelas)
- Cálculo automático de média
- Contador de avaliações
- Evento assíncrono para atualização de ranking

### 🏆 Ranking Dinâmico
- Pontuação baseada em:
  - Ideias criadas
  - Avaliações recebidas
  - Qualidade (média de notas)
- Atualização em tempo real via mensageria

### 🤖 Geração de Missões com IA
- Integração com OpenAI via Spring AI
- Geração de desafios personalizados
- Contexto baseado em área de interesse
- Armazenamento em MongoDB

### 🌍 Internacionalização (i18n)
- Suporte a 3 idiomas:
  - Português (pt_BR) - padrão
  - Espanhol (es_ES)
  - Inglês (en)
- Mensagens de erro traduzidas
- Troca de idioma via parâmetro `?lang=es_ES`

### 📨 Mensageria Assíncrona
- **Fila 1**: `ideas.events` - Eventos de criação de ideias
- **Fila 2**: `futurehub.avaliacoes` - Processamento de avaliações
- Processamento desacoplado e escalável

### 💾 Cache
- Cache Caffeine com TTL de 10 minutos
- Máximo de 500 entradas por cache
- 4 caches configurados:
  - `ideiasPorArea`
  - `usuarios`
  - `rankings`
  - `missoes`
- Invalidação automática em operações de escrita

---

## 📡 Endpoints Principais

### Ideias
- `GET /api/ideias` - Listar ideias (paginado, com filtros)
- `GET /api/ideias/{id}` - Buscar ideia por ID
- `POST /api/ideias` - Criar nova ideia (requer autenticação)
- `PUT /api/ideias/{id}` - Atualizar ideia (requer ADMIN)
- `DELETE /api/ideias/{id}` - Deletar ideia (requer ADMIN)

### Avaliações
- `POST /api/avaliacoes` - Avaliar uma ideia (requer autenticação)

### Usuários
- `GET /api/usuarios` - Listar usuários
- `POST /api/usuarios` - Criar usuário

### Rankings
- `GET /api/rankings` - Top usuários por pontuação

### Missões (IA)
- `POST /api/missoes/gerar` - Gerar missão com IA (requer ADMIN)

### Administração (MVC)
- `/admin` - Interface administrativa com Thymeleaf

---

## 🛠️ Como Executar Localmente

### Pré-requisitos
- **Java 17+** instalado
- **Maven** 3.8+ (ou usar `./mvnw` incluído)
- **MongoDB** rodando em `localhost:27017`
- **RabbitMQ** rodando em `localhost:5672`
- **Chave OpenAI API** (para geração de missões)

### Configuração de Ambiente

1. **Clone o repositório**:
```bash
git clone https://github.com/CarlosCampos84/futurehub-gs_java.git
cd futurehub-gs_java
```

2. **Configure variáveis de ambiente** (opcional):
```bash
export SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/futurehub
export SPRING_RABBITMQ_HOST=localhost
export SPRING_RABBITMQ_PORT=5672
export SPRING_AI_OPENAI_API_KEY=sk-xxx...
```

3. **Execute o projeto**:
```bash
./mvnw spring-boot:run
```

### Serviços Necessários

#### MongoDB (Docker):
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

#### RabbitMQ (Docker):
```bash
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:3-management
```

**Management Console**: http://localhost:15672  
Credenciais padrão: `guest` / `guest`

---

## 🔑 Credenciais de Acesso

### Usuários de Teste (HTTP Basic Auth)

| Username | Password | Role | Descrição |
|----------|----------|------|-----------|
| `admin` | `123456` | ADMIN | Acesso total (CRUD completo) |
| `user` | `1234` | USER | Acesso limitado (leitura + criar) |

### Exemplo de Requisição:
```bash
curl -u admin:123456 http://localhost:8081/api/ideias
```

---

## 📚 Documentação da API (Swagger)

Acesse a documentação interativa em:

**Swagger UI**: http://localhost:8081/swagger-ui.html  
**OpenAPI JSON**: http://localhost:8081/v3/api-docs

### Testando via Swagger:
1. Clique em **Authorize**
2. Digite: `admin` / `123456`
3. Execute requisições diretamente pela interface

---

## 📊 Monitoramento (Actuator)

Endpoints de monitoramento disponíveis em **porta separada 8082**:

- **Health**: http://localhost:8082/actuator/health
- **Info**: http://localhost:8082/actuator/info
- **Métricas**: http://localhost:8082/actuator/metrics
- **Cache Stats**: http://localhost:8082/actuator/caches

---

## 🎯 Exemplos de Uso

### 1. Criar uma Ideia
```bash
curl -X POST http://localhost:8081/api/ideias \
  -u user:1234 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Reciclagem de eletrônicos com pontos",
    "descricao": "Criar postos de coleta que recompensam com descontos",
    "idUsuario": "673e89c0d5e1234567890abc",
    "idMissao": null
  }'
```

### 2. Avaliar uma Ideia
```bash
curl -X POST http://localhost:8081/api/avaliacoes \
  -u user:1234 \
  -H "Content-Type: application/json" \
  -d '{
    "ideiaId": "673f1234567890abcdef1234",
    "usuarioId": "673e89c0d5e1234567890abc",
    "nota": 5
  }'
```

### 3. Listar Ideias com Filtros
```bash
# Filtrar por área
curl "http://localhost:8081/api/ideias?areaId=123&page=0&size=10"

# Buscar por título
curl "http://localhost:8081/api/ideias?q=reciclagem"

# Trocar idioma
curl "http://localhost:8081/api/ideias?lang=es_ES"
```

### 4. Gerar Missão com IA
```bash
curl -X POST http://localhost:8081/api/missoes/gerar \
  -u admin:123456 \
  -H "Content-Type: application/json" \
  -d '{
    "areaNome": "Energia Limpa",
    "dificuldade": "MEDIA"
  }'
```

---

## 🌐 Deploy em Nuvem

### URL de Produção
🔗 **[ADICIONAR URL DO DEPLOY AQUI]**

### Banco de Dados
MongoDB Atlas configurado automaticamente via variável de ambiente `SPRING_DATA_MONGODB_URI`.

### RabbitMQ
CloudAMQP ou RabbitMQ Cloud configurado via `SPRING_RABBITMQ_HOST` e `SPRING_RABBITMQ_PORT`.

---

## 🎥 Vídeos de Demonstração

### Vídeo Pitch (Apresentação da Proposta)
🎬 **[ADICIONAR LINK DO VÍDEO PITCH AQUI]**

### Vídeo Demonstração (Funcionalidades)
🎬 **[ADICIONAR LINK DO VÍDEO DEMO AQUI]**


---

## 👥 Integrantes

- **RM 555223** - [Carlos Ferraz]
- **RM 554518** - [Antonio Junior]
- **RM 554600** - [Caio Henrique]

