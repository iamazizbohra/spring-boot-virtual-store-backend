### Virtual Store 

Built Using Spring Boot (v3.1.5)

---

### Dependencies

* spring-boot-starter-web
* spring-boot-starter-security
* spring-boot-starter-data-jpa
* spring-boot-starter-data-redis
* jedis
* spring-boot-starter-validation
* spring-boot-starter-actuator
* spring-boot-starter-test
* jacoco-maven-plugin
* spring-boot-devtools
* postgresql
* lombok
* modelmapper
* flyway-core
* io.jsonwebtoken
* javafaker
* jobrunr-spring-boot-starter

---

### Requires

* JDK (Java 17+ )
* Maven
* PostgreSQL
* Redis

---

### Steps to setup project on local machine

### Install JDK

On MacBook run

```text
brew install openjdk
```
Verify Installation
```text
java --version
```
```text
openjdk 21 2023-09-19
OpenJDK Runtime Environment Homebrew (build 21)
OpenJDK 64-Bit Server VM Homebrew (build 21, mixed mode, sharing)
```
[For windows click here](https://phoenixnap.com/kb/install-java-windows)

### Install Maven

On MacBook run

```text
brew install maven
```
Verify Installation
```text
mvn -v
```
```text
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: /opt/homebrew/Cellar/maven/3.9.5/libexec
Java version: 21, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/21/libexec/openjdk.jdk/Contents/Home
Default locale: en_IN, platform encoding: UTF-8
OS name: "mac os x", version: "14.1.2", arch: "aarch64", family: "mac"
```
[For windows click here](https://phoenixnap.com/kb/install-maven-windows)


### Install PostgreSQL

On MacBook run

```text
brew install postgresql@14
```
Verify Installation
```text
postgres --version
```
```text
postgres (PostgreSQL) 14.9 (Homebrew)
```
Run PostgreSQL Server
```text
brew services run postgresql@14
```
Interact via Command Line Interface
```text
psql
```
Stop PostgreSQL Server
```text
brew services stop postgresql@14
```
[For windows click here](https://www.postgresql.org/download/)


### Install pgadmin4

On MacBook run

```text
brew install --cask pgadmin4
```
[For windows click here](https://www.pgadmin.org/download/pgadmin-4-windows/)

### Install Redis

On MacBook run

```text
brew install redis
```
Verify Installation
```text
redis-server --version
```
```text
Redis server v=7.2.3 sha=00000000:0 malloc=libc bits=64 build=96f23777753ded6c
```
Run Redis Server
```text
brew services run redis
```
Interact via Command Line Interface
```text
redis-cli
```
Stop Redis Server
```text
brew services stop redis
```
[For windows click here](https://redis.io/docs/install/install-redis/install-redis-on-windows/)


### Install Postman

On MacBook run
```text
brew install --cask postman
```
[For windows click here](https://www.postman.com/downloads/)

### Postman Collection

[Click here to download](https://github.com/iamazizbohra/spring-boot-virtual-store-backend/blob/master/Virtual-Store.postman_collection.json)

### Install Eclipse IDE

On MacBook run

```text
brew install --cask eclipse-java
```
[For windows click here](https://www.eclipse.org/downloads/)

Verify Installation
```text
brew brew info eclipse-java
```
```text
==> eclipse-java: 4.30.0,2023-12
https://eclipse.org/
/opt/homebrew/Caskroom/eclipse-java/4.30.0,2023-12 (126B)
From: https://github.com/Homebrew/homebrew-cask/blob/HEAD/Casks/e/eclipse-java.rb
==> Name
Eclipse IDE for Java Developers
==> Description
Eclipse IDE for Java developers
==> Artifacts
Eclipse.app -> Eclipse Java.app (App)
==> Analytics
install: 616 (30 days), 2,171 (90 days), 6,540 (365 days)
```

**Note: Configure installed JDK with Eclipse IDE**

---

### Configure Application Properties

Datasource
```text
spring.datasource.url=jdbc:postgresql://localhost:5432/vstore 
spring.datasource.username=[username]
spring.datasource.password=[password]
```
Default Port: 5432 <br>
Database Name: vstore <br>
**Note: Replace username and password with your database specific values**

Redis
```text
spring.data.redis.host=localhost
spring.data.redis.port=6379
```
Default Port: 6379 <br>
Host: localhost <br>

--- 

### Run Tests
```text
mvn clean
mvn test
```

### Run Project
```text
mvn spring-boot:run
```

---

### Actuator Endpoints
[Click here to access](http://localhost:8080/api/v1/actuator)

---

### JobRunr Dashboard

[Click here to access](http://localhost:8000/)

---