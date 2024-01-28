# Y-Lab-Intro. Консольное приложение для подачи показателей счетчиков.
## Запуск приложения
Чтобы запустить, необходимо в директории ../Y-Lab-Intro в консоли прописать:
```bash
  make run-dist
  ```
## Нюансы, которые необходимо знать при работе с приложением:
* Месяц и год вводить цифрами (валидация на ввод цифры месяца присутствует. Пример:
Enter month: 4
Enter year: 2020
* При запуске приложения в репозитории создается админ. 
Login: admin
Password: admin

## Модель

* class User:
  int id, String username, String password, boolean isAdmin

* class CounterType:
  int userId, int month, int year, Map<String, Double> typeOfCounter

## Пример использования приложения:

  [![asciicast](https://asciinema.org/a/633829.svg)](https://asciinema.org/a/633829)
PR
