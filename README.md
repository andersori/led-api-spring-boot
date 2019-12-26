# led
Rest API for **LED** system.
## Run application in different profiles
### dev
> mvn spring-boot:run -P=dev

or

>  mvn package -P=dev
> java -jar led-api.jar

### prod
By default, the prod profile uses a postgresql database. Then you can use Postgresql + Docker.
>mvn spring-boot:run -P=prod

or

>  mvn package -P=prod
>java -jar led-api.jar

### test
> mvn test

## Postgresql + Docker
> docker run --name postgres --rm -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=led -e POSTGRES_USER=led -p 5432:5432 -d postgres