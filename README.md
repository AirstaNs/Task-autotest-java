# Task-autotest-java

## Инструкция по сборке проекта
```bash
mvn package
```
Или короткая версия:  
```bash
mvn
```
Собранный jar-файл будет находиться в папке /target
## Инструкция по работе с приложением
Запустить jar-файл через консоль:
```bash 
java -jar Task-autotest-java-1.0.jar
```
Программа запросит хостинг, имя пользователя и пароль для подключения к ftp серверу.  

Если подключение прошло успешно, то программа выведет список доступных действий:  

1. Получение списка студентов по имени
2. Получение информации о студенте по id 
3. Добавление студента
4. Удаление студента по id
5. Выход из программы
  
**Каждое действие выполняется с проверками на корректность введенных данных.**  
**Каждое действие запрашивает подключение к FTP серверу (кроме выхода).**  
**В каждом действии происходит парсинг из JSON в объекты Student и обратно (кроме выхода).**  

## Выполненные требования:
✔️ **Реализован пассивный режим работы с FTP сервером** - org/client/FTPClient - enterPassiveMode()    

✔️ **Реализован активный  режим работы с FTP сервером** - org/client/FTPClient - enterActiveMode()    

✔️ **Не использованы внешние библиотеки, только стандартные библиотеки Java (JDK 8)**  

✔️ **Реализован вывод студентов в алфавитном порядке**    

✔️ **Реализована уникальность ID студента**      

✔️ **Реализовано переключение между режимами**  - org/client/FTPClient - setTransfer()  

✔️ **Реализована кроссплатформенность**

## Описание модулей и классов
### Пакет org.client

