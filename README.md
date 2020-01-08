# led
Rest API for **LED** system.
## Run application in different profiles
### dev
> mvn spring-boot:run -DskipTests

### prod
By default, the prod profile uses a postgresql database. Then you can use [Postgresql + Docker](#postgresql-and-docker).
> mvn spring-boot:run -P=prod

## Postgresql and Docker
> docker run --name postgres --rm -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=led -e POSTGRES_USER=led -p 5432:5432 -d postgres