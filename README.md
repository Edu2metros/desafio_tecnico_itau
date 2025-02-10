# Desafio Técnico Júnior

## Descrição
Este projeto é uma aplicação backend desenvolvida com **Spring Boot**, que implementa rotas **CRUD** para o gerenciamento de dados de coleiras de pets. O sistema utiliza o banco de dados em memória **H2 Database** para armazenar temporariamente os dados de localização enviados por dispositivos GPS, com campos como `sensorID`, `latitude`, `longitude` e `data/hora`. Além disso, testes unitários e de integração foram implementados para garantir a validade das operações e a cobertura do código.

---

## Tecnologias Utilizadas
- **Spring Boot 3.1.0**
- **Java 17**
- **JUnit** (para testes unitários)
- **Mockito** (para criação de mocks nos testes)
- **Maven** (para gerenciamento de dependências)
- **Jacoco** (para verificar a cobertura de testes)
- **PositionStack API** (para conversão de coordenadas geográficas em endereços detalhados)
- **H2 Database** (banco de dados em memória para testes e desenvolvimento)

---

## Estrutura de Dados
Os dados enviados pelos dispositivos GPS são armazenados no banco de dados **H2** e processados para obter informações geográficas detalhadas. Cada registro possui os seguintes campos:

- `id`: Identificador único do GPS
- `sensorID`: Identificador único do dispositivo
- `latitude`: Coordenada de norte a sul do GPS
- `longitude`: Coordenada de leste a oeste do GPS
- `data/hora`: Data e hora da última atualização do GPS
- `país`: País onde o GPS está localizado
- `estado`: Estado onde o GPS está localizado
- `cidade`: Cidade onde o GPS está localizado
- `bairro`: Bairro onde o GPS está localizado
- `endereço`: Endereço completo onde o GPS está localizado

---

## Funcionalidades
O sistema implementa as operações básicas de CRUD (**Criar, Ler, Atualizar e Deletar**) para gerenciar as informações de localização dos dispositivos GPS.

### Endpoints da API:
- **`POST /location/batch`**: Armazena várias localizações de GPS de uma só vez.
- **`POST /location`**: Armazena uma nova localização de GPS.
- **`GET /location`**: Retorna a lista de todas as localizações de GPS.
- **`GET /location/{id}`**: Retorna os dados de uma localização específica pelo ID.
- **`PUT /location/{id}`**: Atualiza completamente os dados de uma localização existente.
- **`PATCH /location/{id}`**: Atualiza parcialmente os dados de uma localização existente.
- **`DELETE /location/{id}`**: Remove uma localização pelo ID.

## Como Executar

1. Clone o repositório:
   ```bash
   git clone git@github.com:Edu2metros/desafio_tecnico_itau.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
    cd desafio_tecnico_itau
   ```
3. Execute o projeto usando o Maven:
    ```bash
    mvn spring-boot:run
4. Abra o Insomnia

5. Importe o collection do Insomnia em import -> from file -> selecione o arquivo `($(caminho do projeto)/insomnia/Insomnia_2025-02-10.json)`

## Como Testar

1. Execute o comando abaixo para rodar os testes unitários e de integração:
	```bash
	mvn test
	```