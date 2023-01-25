# План автоматизации тестирования #

## Автоматизируемые сценарии ##

**UI тесты:**  

**Раздел "Купить"**    

Входные валидные данные:

Номер карты - _4444 4444 4444 4441_;   
Дата - _любой месяц от 01 до 12 / любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;    
CVC/CVV - _любое число от 1 до 999, если двузначное, ставим 0 перед ним, если однозначное, то 00_;    
Владелец - _любые имя и фамилия на английском_. 


ШАГИ для выполнения тестов к разделу:

- Зайти на сайт http://localhost:8080
- Нажать кнопку "Купить" 
- Заполнить поля входными данными 
- Нажать кнопку "Продолжить"

**_Позитивные сценарии_**
### 1. Покупка тура с "APPROVED" картой ###

Входные данные:

Номер карты - _4444 4444 4444 4441_;   
 

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Выходит сообщение "Успешно Операция одобрена банком"  
При запросе в базу данных о статусе платежа и сумме, ответ будет "APPROVED", 45000


### 2. Покупка тура с датой истечения срока - текущий месяц и год ###

Входные данные:
  
Дата - _текущий месяц и год_ ;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Выходит сообщение "Успешно Операция одобрена банком"  



**_Негативные сценарии_**  

### 3. Покупка тура с "DECLINED" картой: ###

Входные данные:

Номер карты - _4444 4444 4444 4442_;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Выходит сообщение "Ошибка! Банк отказал в проведении операции."   
При запросе в базу данных о статусе платежа ответ будет "DECLINED"

### 4. Покупка тура с вводом карты с одной цифрой: ###

Входные данные:

Номер карты - _любая цифра_;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Номер карты выйдет текст "Неверный формат"


### 5. Покупка тура с пустым полем карты: ###

Входные данные:

Номер карты -  ;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Номер карты выйдет текст "Неверный формат"

### 6. Покупка тура с полем карты, заполненным нолями: ###

Входные данные:

Номер карты - _0000 0000 0000 0000_;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Номер карты выйдет текст "Неверный формат"


### 7. Покупка тура с вводом прошлого года: ###

Входные данные:
  
Дата - _любой месяц/ любой прошлый год;_     

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц выйдет подсказка "Истёк срок действия карты"	


### 8. Покупка тура с с вводом месяца, который предшествует текущему месяцу: ###

Входные данные:
   
Дата - _ месяц, который предшествует текущему месяцу / текущий год;  (Например, текущий месяц - ноябрь, значит в поле указать - октябрь)

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем год выйдет подсказка "Истёк срок действия карты"


### 9. Покупка тура с вводом даты, где месяц под номером 13: ###

Входные данные:

Дата - _ 13/любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;     

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц выйдет подсказка "Неверно указан срок действия карты"


### 10. Покупка тура с вводом даты, где месяц имеет нулевое значение: ###

Входные данные:

Дата - _ 00/любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;       

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц выйдет подсказка "Неверно указан срок действия карты"

### 11. Покупка тура с вводом даты, где год имеет нулевое значение: ###

Входные данные:
   
Дата - _любой месяц от 01 до 12/00_;     

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем год выйдет подсказка "Истек срок действия карты"

### 12. Покупка тура с вводом даты с пустым значением: ###

Входные данные:
  
Дата - _  / _;    

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц и год выйдет подсказка "Неверный формат"

### 13. Покупка тура с вводом даты, где месяц с пустым значением: ###

Входные данные:    

Дата - _  /любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;       

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц выйдет подсказка "Неверный формат"


### 14. Покупка тура с вводом даты, где год с пустым значением: ###

Входные данные:
   
Дата - _ любой месяц от 01 до 12/ _;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем год выйдет подсказка "Неверный формат"

### 15. Покупка тура с вводом даты, где год на 6 лет больше текущего: ###

Входные данные:
 
Дата - _ любой месяц от 01 до 12/текущий год +6 лет_;    

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем год выйдет подсказка "Неверно указан срок действия карты"

### 16. Покупка тура с вводом специсимволов в поле даты: ###

Входные данные:

Дата - _спецсимволы_;    

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Символы в поле даты не получится ввести, поле будет пустое
Под полем даты выйдет подсказка "Неверный формат"

### 17. Покупка тура с вводом одной цифры в поле даты: ###

Входные данные:
  
Дата - _любая цифра/любая цифра_;    

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем месяц и год выйдет подсказка "Неверный формат"


### 18. Покупка тура с вводом имени владельца кириллицей: ###

Входные данные:
  
Владелец - _имя и фамилия на русском_.

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Имя Владельца выйдет подсказка "Неверный формат"

### 19. Покупка тура с вводом цифр в поле Имя Владельца: ###

Входные данные:

Владелец - _цифры любые_.    

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Имя Владельца выйдет подсказка "Неверный формат"

### 20. Покупка тура с вводом одной буквы в поле Имя Владельца: ###

Входные данные:
 
Владелец - _любая буква на латинице_.   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Имя Владельца выйдет подсказка "Неверный формат"

### 21. Покупка тура с вводом спецсимволов в поле Имя Владельца: ###

Входные данные:
  
Владелец - _спецсимволы_.   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Имя Владельца выйдет подсказка "Неверный формат"

### 22. Покупка тура с пустым значением в поле Имя Владельца: ###

Входные данные:
   
Владелец -  .   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем Имя Владельца выйдет подсказка "Поле обязательно для заполнения"

### 23. Покупка тура с вводом нулевого CVC/CVV: ###

Входные данные:
 
CVC/CVV - _000_;   


ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем CVC/CVV выйдет подсказка "Неверный формат"

### 24. Покупка тура с вводом CVC/CVV из одной цифры: ###

Входные данные:

CVC/CVV - _цифра_;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем CVC/CVV выйдет подсказка "Неверный формат"

### 25. Покупка тура с пустым значением CVC/CVV : ###

Входные данные:
 
CVC/CVV - ;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под полем CVC/CVV выйдет подсказка "Неверный формат"


### 26. Покупка тура с вводом букв в поле CVC/CVV : ###

Входные данные:

CVC/CVV - _буквы любые_;  

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Буквы в поле не будут вводиться, поле будет пустым
Под полем CVC/CVV выйдет подсказка "Неверный формат"

### 27. Покупка тура с вводом спецсимволов в поле CVC/CVV : ###

Входные данные:
 
CVC/CVV - _спецсимволы_;  

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Буквы в поле не будут вводиться, поле будет пустым  
Под полем CVC/CVV выйдет подсказка "Неверный формат"


### 28. Покупка тура с пустыми значениями во всех полях: ###

Входные данные:

Номер карты -  ;   
Дата -  / ;  
CVC/CVV - ;   
Владелец -  .


ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Под всеми полями выйдет подсказка "Неверный формат", а под полем Имя Владельца - "Поле обязательно для заполнения"

### 29. Покупка тура с рандомным  16-тизначным числом в поле Номер карты ###

Входные данные:

Номер карты - _рандомное  16-тизначное число_;   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Выходит сообщение "Ошибка! Банк отказал в проведении операции." 

### 30. Покупка тура с валидными входными данными и получение уведомления об успешной операции ###

Входные валидные данные, указанные в предусловии.
   

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Выходит уведомление "Успешно Операция одобрена банком", уведомление об Ошибке на странице не будет


**Раздел "Купить в кредит"**    

Входные валидные данные:

Номер карты - _4444 4444 4444 4441_;   
Дата - _любой месяц от 01 до 12 / любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;    
CVC/CVV - _любое число от 1 до 999, если двузначное, ставим 0 перед ним, если однозначное, то 00_;    
Владелец - _любые имя и фамилия на английском_. 

ШАГИ для выполнения тестов к разделу:

- Зайти на сайт http://localhost:8080
- Нажать кнопку "Купить в кредит" 
- Заполнить поля входными данными 
- Нажать кнопку "Продолжить"


Провести аналогично в данном разделе все тест-кейсы, описанные выше.


### **API тесты** ###

31. Оплата APPROVED картой в разделе "Купить"

ВХОДНЫЕ ДАННЫЕ

Номер карты - _4444 4444 4444 4441_;   
Дата - _любой месяц от 01 до 12 / любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;    
CVC/CVV - _любое число от 1 до 999, если двузначное, ставим 0 перед ним, если однозначное, то 00_;    
Владелец - _любые имя и фамилия на английском_.  

 
- отправить **Post** запрос, где тело запроса - входные валидные данные в формате Json на **http://localhost:8080/api/v1/pay**


ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Будет получен статус ответа - 200, а статус заявки "APPROVED".  


32. Оплата DECLINED картой  в разделе "Купить"

ВХОДНЫЕ ДАННЫЕ

Номер карты - _4444 4444 4444 4442_;   
Дата - _любой месяц от 01 до 12 / любой год от следующего года до +5 лет. Пример: текущий год - 22, значит период от 23 до 27 года_;    
CVC/CVV - _любое число от 1 до 999, если двузначное, ставим 0 перед ним, если однозначное, то 00_;    
Владелец - _любые имя и фамилия на английском_. 
  
 

- отправить **Post** запрос, где тело запроса - входные валидные данные в формате Json на **http://localhost:8080/api/v1/pay**

ОЖИДАЕМЫЙ РЕЗУЛЬТАТ

Будет получен статус ответа - 200, а статус заявки "DECLINED".


_Тот же самый сценарий применить к разделу "Купить в кредит".
Необходимо будет только обращаться по ссылке - **http://localhost:8080/api/v1/credit**_


## Инструменты тестирования ##

- **Java** - язык, на котором будут написаны тесты.
- **IntelliJ IDEA** - среда, в которой будет написан код.
- **JUnit5** - наиболее широко используемая среда тестирования для приложений Java.
- **Gradle** - система для автоматизации сборки приложений.
- **Selenide** - обёртка вокруг Selenium WebDriver, позволяющая быстро и просто его использовать при написании тестов.
- **Lombok** - библиотека, которая позволяет писать меньше кода, и делает его лаконичным.
- **Rest-Assured** - Java-библиотека для тестирования REST API.
- **Docker** - программная платформа для разработки, доставки и запуска контейнерных приложений.
- **Allure** - отчетность. Легкая в установке, довольно ёмкие и структурированные отчёты.
- **GitHub** - система управления проектами и версиями кода. Место, где будет храниться код проекта.

## Возможные риски автоматизации ##

1. На автоматизацию может уйти больше времени, чем на ручное тестирование этого же модуля.
2. Изменение кода страницы, из-за чего тесты могут упасть.
3. Риск использования неэффективных паттернов проектирования кода.
4. Сложности в настройке необходимого окружения для тестирования.

## Оценка с учетом рисков ##

Составление плана автоматизации - **24** часа.

Тестирование будет выполнено за **56** рабочих часов.  
**8** часов на установку окружения.  
**40** часов на тестирование SUT.  
**8** часов на подготовку отчетности.

## План сдачи работ ##

Автотесты с отчетностью будут готовы к сдаче _**25 января 2023 года**_.