FROM openjdk:11-jre-slim
RUN mkdir home/application
WORKDIR home/application
COPY . .
ENV PROFILE dev
RUN ./mvnw package -P=${PROFILE} -DskipTests
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${PROFILE}","target/led-api.jar"]
