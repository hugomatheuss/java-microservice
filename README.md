# Microserviços Java com Spring Cloud

Este projeto implementa uma arquitetura de microserviços usando Spring Boot e Spring Cloud, incluindo:

## Arquitetura

### Serviços
1. **Service Registry (Eureka Server)** - Porta 8761
2. **API Gateway** - Porta 8080  
3. **Product Service** - Porta 8081
4. **Order Service** - Porta 8082
5. **Common Module** - DTOs e classes compartilhadas

### Tecnologias
- Java 17
- Spring Boot 3.2.5
- Spring Cloud 2023.0.3
- Spring Data JPA
- H2 Database (em memória)
- Netflix Eureka (Service Discovery)
- Spring Cloud Gateway
- OpenFeign (comunicação entre serviços)
- Lombok

## Estrutura dos Serviços

### Product Service (CRUD de Produtos)
- **Model**: `Product`
- **Repository**: `ProductRepository`
- **Service**: `ProductService`
- **Controller**: `ProductController`
- **DTOs**: `ProductCreateDTO`, `ProductResponseDTO`

**Endpoints:**
- `GET /api/products` - Listar todos os produtos
- `GET /api/products/{id}` - Buscar produto por ID
- `POST /api/products` - Criar novo produto
- `PUT /api/products/{id}` - Atualizar produto
- `DELETE /api/products/{id}` - Deletar produto
- `GET /api/products/search?name={name}` - Buscar por nome
- `GET /api/products/in-stock` - Produtos em estoque

### Order Service (CRUD de Pedidos)
- **Model**: `Order`
- **Repository**: `OrderRepository`  
- **Service**: `OrderService`
- **Controller**: `OrderController`
- **DTOs**: `OrderCreateDTO`, `OrderResponseDTO`
- **Client**: `ProductServiceClient` (Feign)

**Endpoints:**
- `GET /api/orders` - Listar todos os pedidos
- `GET /api/orders/{id}` - Buscar pedido por ID
- `POST /api/orders` - Criar novo pedido
- `PUT /api/orders/{id}/status?status={status}` - Atualizar status
- `DELETE /api/orders/{id}` - Deletar pedido
- `GET /api/orders/customer?email={email}` - Pedidos por cliente
- `GET /api/orders/status?status={status}` - Pedidos por status

### Common Module
- **DTOs**: `ProductDTO`, `OrderDTO`
- **Enums**: `OrderStatus` (CREATED, CONFIRMED, CANCELLED)

## Como Executar

### Manualmente (ordem importante):
1. **Service Registry**: `cd service-registry && mvn spring-boot:run`
2. **Product Service**: `cd product-service && mvn spring-boot:run`
3. **Order Service**: `cd order-service && mvn spring-boot:run`
4. **API Gateway**: `cd api-gateway && mvn spring-boot:run`

## URLs dos Serviços

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Products via Gateway**: http://localhost:8080/api/products
- **Orders via Gateway**: http://localhost:8080/api/orders

## Testando os Serviços

### Criar um produto:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone Samsung",
    "description": "Galaxy S24 Ultra 256GB",
    "price": 4500.00,
    "stockQuantity": 5
  }'
```

### Listar produtos:
```bash
curl http://localhost:8080/api/products
```

### Criar um pedido:
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "João Silva",
    "customerEmail": "joao@email.com",
    "productIds": [1, 2]
  }'
```

### Listar pedidos:
```bash
curl http://localhost:8080/api/orders
```

## Bancos de Dados H2

- **Product Service**: http://localhost:8081/h2-console
  - URL: `jdbc:h2:mem:productdb`
  - User: `sa`
  - Password: `password`

- **Order Service**: http://localhost:8082/h2-console
  - URL: `jdbc:h2:mem:orderdb`
  - User: `sa`
  - Password: `password`

## Funcionalidades Implementadas

### ✅ Service Registry e Discovery
- Eureka Server configurado
- Todos os serviços se registram automaticamente

### ✅ API Gateway
- Roteamento para os serviços
- Load balancing
- Circuit breaker para tolerância a falhas

### ✅ Comunicação entre Serviços
- Order Service valida produtos via Feign Client
- Descoberta automática de serviços via Eureka

### ✅ Estrutura em Camadas
- Model, Repository, Service, Controller
- DTOs para transferência de dados
- Separação de responsabilidades

### ✅ CRUD Completo
- Produtos: CREATE, READ, UPDATE, DELETE
- Pedidos: CREATE, READ, UPDATE, DELETE
- Busca e filtros implementados