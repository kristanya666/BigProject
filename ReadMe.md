# Дипломный проект 

## План тестирования 
* [План автоматизации тестирования](docs%2FPlan.md)

## Отчеты
* [Отчёт по итогам автоматизированного тестирования](docs%2FReport.md)

* [Отчёт по итогам автоматизации](docs%2FSummary.md)

## Запуск тестов

1. Открыть проект в Intellij Idea
2. Запустить контейнеры: docker-compose up -d
3. Запустить SUT 
- MySQL: **_java -jar artifacts/aqa-shop.jar  -Dspring.datasource.url=jdbc:mysql://localhost:3306/app_**
- PostgreSQL: **_java -jar artifacts/aqa-shop.jar  -Dspring.datasource.url=jdbc:postgresql://localhost:5432/postgres_**

4. Запустить тесты 

- **MySQL**. 
Для начала зайти в [application.properties](application.properties) и [build.gradle](build.gradle) закомментровать ссылку на postgresql, оставив только MySQL. После перейти в тесты, нажать _Ctrl+Ctrl_ и ввести
**_./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080_**
- **PostgreSQL**. Зайти в [application.properties](application.properties) и [build.gradle](build.gradle) закомментровать ссылку на MySQL, оставив только postgresql. После перейти в тесты, нажать _Ctrl+Ctrl_ и ввести **_./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/postgres -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080_**
5. Запустить отчет Allure: **_./gradlew allureServe_**

## Завершение тестирования
1. Нажать Ctrl+C в терминале для завершения работы SUT
2. Выполнить команду: docker-compose down для прекращения работы контейнеров
