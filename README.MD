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

```shell
brew install openjdk
```
[For windows click here](https://phoenixnap.com/kb/install-java-windows)

### Install Maven

On MacBook run

```shell
brew install Maven
```
[For windows click here](https://phoenixnap.com/kb/install-maven-windows)


### Install Eclipse IDE

On MacBook run

```shell
brew install --cask eclipse-java
```
[For windows click here](https://www.eclipse.org/downloads/)

**Note: Configure installed JDK with Eclipse IDE**

### Install PostgreSQL

On MacBook run

```shell
brew install postgresql@14
```
Run PostgreSQL Server
```shell
brew services run postgresql@14
```
Stop PostgreSQL Server
```shell
brew services stop postgresql@14
```
[For windows click here](https://www.postgresql.org/download/)


### Install pgadmin4

On MacBook run

```shell
brew install --cask pgadmin4
```
[For windows click here](https://www.pgadmin.org/download/pgadmin-4-windows/)

### Install Redis

On MacBook run

```shell
brew install redis
```
Run Redis Server
```shell
brew services run redis
```
Stop Redis Server
```shell
brew services stop redis
```
[For windows click here](https://redis.io/docs/install/install-redis/install-redis-on-windows/)

### Configure Application Properties

Datasource
```shell
spring.datasource.url=jdbc:postgresql://localhost:5432/vstore 
spring.datasource.username=[username]
spring.datasource.password=[password]
```
Default Port: 5432 <br>
Database Name: vstore <br>
**Note: Replace username and password with your database specific values**

Redis
```shell
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