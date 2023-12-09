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
postgres (PostgreSQL) 14.10 (Homebrew)
```
Run PostgreSQL Server
```text
brew services run postgresql@14
```
Interact via Command Line Interface
```text
psql
```
Create a Database with owner without password
```text
CREATE DATABASE vstore OWNER [NAME_OF_OWNER];
```
Verify Database Creation
```text
\l
```
```text
                         List of databases
   Name    | Owner | Encoding | Collate | Ctype | Access privileges
-----------+-------+----------+---------+-------+-------------------
 aziz      | aziz  | UTF8     | C       | C     |
 postgres  | aziz  | UTF8     | C       | C     |
 template0 | aziz  | UTF8     | C       | C     | =c/aziz          +
           |       |          |         |       | aziz=CTc/aziz
 template1 | aziz  | UTF8     | C       | C     | =c/aziz          +
           |       |          |         |       | aziz=CTc/aziz
 vstore    | aziz  | UTF8     | C       | C     |
(5 rows)
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
Verify Installation
```text
brew info pgadmin4
```
```text
==> pgadmin4: 8.0
https://www.pgadmin.org/
/opt/homebrew/Caskroom/pgadmin4/8.0 (123B)
From: https://github.com/Homebrew/homebrew-cask/blob/HEAD/Casks/p/pgadmin4.rb
==> Name
pgAdmin4
==> Description
Administration and development platform for PostgreSQL
==> Artifacts
pgAdmin 4.app (App)
==> Analytics
install: 3,447 (30 days), 10,351 (90 days), 26,894 (365 days)
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
Run in Foreground
```text
redis-server
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

Verify PostgreSQL and Redis server is running
```text
brew services list
```
```text
Name          Status  User File
postgresql@14 started      /opt/homebrew/opt/postgresql@14/homebrew.mxcl.postgresql@14.plist
redis         started      /opt/homebrew/opt/redis/homebrew.mxcl.redis.plist
```

### Install Postman

On MacBook run
```text
brew install --cask postman
```
Verify Installation
```text
brew info postman
```
```text
==> postman: 10.20.0 (auto_updates)
https://www.postman.com/
/opt/homebrew/Caskroom/postman/10.20.0 (121B)
From: https://github.com/Homebrew/homebrew-cask/blob/HEAD/Casks/p/postman.rb
==> Name
Postman
==> Description
Collaboration platform for API development
==> Artifacts
Postman.app (App)
==> Analytics
install: 9,790 (30 days), 33,057 (90 days), 91,105 (365 days)
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

### Install Lombok into Eclipse IDE

1. Download lombok.jar from https://projectlombok.org/download

2. Navigate to lombok.jar location  and run java -jar lombok.jar

3. This starts the eclipse installer which will find eclipse (and eclipse variants as listed above), and offers to install lombok into these eclipse installations. The same tool can also uninstall lombok.

[https://projectlombok.org/setup/eclipse](https://projectlombok.org/setup/eclipse)

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

### Package Project
```text
mvn clean package spring-boot:repackage
```

---

### Actuator Endpoints
[Click here to access](http://localhost:8080/api/v1/actuator)

---

### JobRunr Dashboard

[Click here to access](http://localhost:8000/)

---