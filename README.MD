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
* spring-boot-devtools
* postgresql
* lombok
* modelmapper
* flyway-core
* io.jsonwebtoken
* javafaker
* jobrunr-spring-boot-starter

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
openjdk 19.0.1 2022-10-18
OpenJDK Runtime Environment (build 19.0.1+10-21)
OpenJDK 64-Bit Server VM (build 19.0.1+10-21, mixed mode, sharing)
```
[For windows click here](https://phoenixnap.com/kb/install-java-windows)

### Install Maven

On MacBook run

```text
brew install Maven
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


### Install Eclipse IDE

On MacBook run

```text
brew install --cask eclipse-java
```
[For windows click here](https://www.eclipse.org/downloads/)

**Note: Configure installed JDK with Eclipse IDE**

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
Stop Redis Server
```text
brew services stop redis
```
[For windows click here](https://redis.io/docs/install/install-redis/install-redis-on-windows/)

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

### Postman Collection

[Click here to download](https://github.com/iamazizbohra/spring-boot-virtual-store-backend/blob/master/Virtual-Store.postman_collection.json)

---

### Actuator Endpoints
[Click here to access](http://localhost:8080/api/v1/actuator)

---

### JobRunr Dashboard

[Click here to access](http://localhost:8000/)

---