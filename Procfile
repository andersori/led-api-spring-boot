web: ./mvnw -DskipTests compile package -Pprod && java $JAVA_OPTS -Dserver.port=$PORT -Dspring.datasource.url=$JDBC_DATABASE_URL -Dspring.datasource.username=$JDBC_DATABASE_USERNAME -Dspring.datasource.password=$JDBC_DATABASE_PASSWORD $JAVA_OPTS -jar target/led-api.jar
