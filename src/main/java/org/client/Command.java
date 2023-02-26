package org.client;

public enum Command {
    USER, // задает логин
    PASS, // задает пароль
    QUIT, // Выход пользователя из системы, завершение сеанса
    LIST, // Показывает файлы или записи каталога
    RETR, // Получает данные с сервера
    PWD, // Показывает рабочий каталог
    CWD, // Изменяет рабочий каталог или библиотеку
    APPE, // Добавляет данные в указанный файл
    EPRT, // включение активного режима расширенного
    PORT, // включение активного режима
    EPSV, // Включение пассивного режима расширенного
    PASV, // Включение пассивного режима
    STOR
}
