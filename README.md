# alfa-gipher
Сервис, который выдает рандомную гифку по тегу "rich", если курс указанной валюты вырос, "broke" -- если упал, "pokerface" -- если неизменился.

Endpoints
---
Получить гифку в зависимости от изменения курса валюты: http://localhost:777/api/v1/exchange-rate-change?currency=RUB, вместо RUB можно указать любой другой код валюты.

Инструкция по запуску
---
0. Для запуска приложения установите [JDK](https://openjdk.java.net/) и [Docker](https://www.docker.com/products/docker-desktop)
1. Скачать архив с кодом и разархивировать или склонировать с помощью команды 
```
git clone
```
2. Выполнить в корневой директории проекта команду
```
.\gradlew bootJar
```
3. Собрать docker image, выполнив в корневой директории проекта команду
```
docker build -t currency-gipher .
```
4. Запустить docker container, выполнив в корневой директории проекта команду
```
docker run -d -p 777:8080 currency-gipher
```
5. Перейти по ссылке http://localhost:777/api/v1/exchange-rate-change?currency=RUB

#### Или
Перейдите на сайт https://alfa-gipher.herokuapp.com/api/v1/exchange-rate-change?currency=RUB
