# Y-Lab-Intro. Консольное приложение для подачи показателей счетчиков.
## Запуск приложения
1) Освободить порт 5432
2) Запустить docker-compose.yml
3) gradle task: build 
4) Запустить Tomcat 10.1.18 (в папке .run находится конфиг Tomcat 10.1.18.run.xml)
5) Все запросы находятся в корне проекта в Y-lab_suhoi_postman.json. Файл надо импортнуть в postman
## Нюансы, которые необходимо знать при работе с приложением:
* Месяц и год вводить цифрами (валидация присутствует). Пример:
Enter month: 4
Enter year: 2020
* При запуске приложения в репозитории создается админ:

Login: admin

Password: admin

## Модель

* class User:
  int id, String username, String password, enum Role

* class CounterType:
  int userId, int month, int year, string type, double value
## P.S.

* Замер времени реализовал только у сервисов (не сложно сделать замер времени у ВСЕХ методов, но мне кажется геттеры и сеттеры нечего замерять)
* Регистрация действий администратора по логике не должна прозводится