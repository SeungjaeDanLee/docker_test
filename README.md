# Spring Boot + Docker + MySQL 세팅 가이드

이 문서는 새 스프링부트 프로젝트를 시작하면서 Docker로 MySQL을 연동하는 방법을 단계별로 설명합니다. 민감 정보는 별도 파일로 분리하여 관리합니다.

---

## ✅ 프로젝트 시작 순서

### 1. 스프링부트 프로젝트 생성
- [Spring Initializr](https://start.spring.io/) 에서 새 프로젝트를 생성하거나 IDE에서 직접 생성
- 필수 의존성: `Spring Web`, `Spring Data JPA`, `MySQL Driver`

---

### 2. `docker-compose.yml` 파일 생성
`프로젝트 루트 디렉토리`에 아래 내용을 가진 `docker-compose.yml` 파일을 생성합니다.

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: spring-mysql
    environment:
      MYSQL_ROOT_PASSWORD: secretRootPwd
      MYSQL_DATABASE: docker_test
      MYSQL_USER: your_user
      MYSQL_PASSWORD: your_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    restart: always

volumes:
  mysql_data:
```

---

### 3. Docker 컨테이너 실행
```bash
docker-compose up -d
```

---

### 4. `application.yml` 구성
`src/main/resources/application.yml`

```yaml
spring:
  config:
    import: optional:file:application-secret.yml
  profiles:
    active: local
```

#### `application-local.yml`
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect

  sql:
    init:
      mode: always

server:
  port: 8080
```

#### `application-secret.yml`
이 파일은 `git`에 올리지 않도록 `.gitignore`에 등록하고 아래와 같이 작성합니다:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/docker_test
    username: your_user
    password: your_password
```

---

### 5. `.gitignore` 설정
```gitignore
# application 민감 설정
src/main/resources/application-secret.yml
```

---

### 6. DBeaver 연결 설정
DBeaver에서 MySQL 연결을 설정할 때 다음과 같이 입력합니다.

```
Server Host: localhost
Port: 3306
Database: docker_test
Username: your_user
Password: your_password
```

옵션에서 **"Use SSL" 비활성화** 및 **"Allow public key retrieval" 허용** 설정도 잊지 마세요.

---

## ✅ 기타 팁

- 도커 컨테이너 상태 확인: `docker ps`
- MySQL 컨테이너 로그 보기: `docker logs spring-mysql`
- DB 초기화 시 볼륨 삭제: `docker-compose down -v`

---

이 가이드는 민감 정보를 안전하게 분리한 상태에서 Spring Boot와 Docker를 효율적으로 연동하는 데 목적이 있습니다.
