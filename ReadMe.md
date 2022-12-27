1. Открыть проект в Intellij Idea
2. Запустить контейнеры: docker-compose up -d
3. Запустить SUT с MySQL: java -jar artifacts/aqa-shop.jar  -Dspring.datasource.url=jdbc:mysql://localhost:3306/app
4. Запустить тесты с MySQL: ./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080
5. Запустить отчет Allure: ./gradlew allureServe
