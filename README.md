mvn test

mvn spring-boot:run -Dspring-boot.run.profiles=dev -P=dev

mvn spring-boot:run -Dspring-boot.run.profiles=prod -P=prod

docker run --name postgres --rm -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=led -e POSTGRES_USER=led -p 5432:5432 -d postgres
