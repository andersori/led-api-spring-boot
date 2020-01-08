
# led
Rest API for **LED** website.
## Run application in different profiles
### Development
In this profile, the database used is `h2`, with `spring.jpa.hibernate.ddl-auto` set to `create-drop`.\
`mvn spring-boot:run -DskipTests`\
**or**\
`mvn clean package -DskipTests`\
`java -jar -Dspring.profiles.active=dev target/led-api.jar`

### Production
By default, the production profile uses a postgresql database. Then you can use [Postgresql and Docker](#postgresql-and-docker). Before starting the application, you must create the tables in the database, because in this profile `spring.jpa.hibernate.ddl-auto` is disabled, so the following command must be executed.
> `mvn liquibase:update -P=prod`

> `mvn spring-boot:run -P=prod`\
> **or**\
> `mvn clean package -P=prod`\
> `java -jar target/led-api.jar`

## Postgresql and Docker
`docker run --name postgres --rm -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=led -e POSTGRES_USER=led -p 5432:5432 -d postgres`