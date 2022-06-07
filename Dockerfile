FROM openjdk:11
EXPOSE 8080
WORKDIR /app
COPY ./build/libs/currency-gipher-0.0.1.jar .
CMD java -jar currency-gipher-0.0.1.jar