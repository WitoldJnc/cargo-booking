### Первоначальные настройки

Необходимо установить в maven репозиторий стартер `booking-spring-boot-starter`

В случае запуска проектов без использования docker необходимо добавить общие пакеты в локальный репозиторий:

```shell
cd booking-spring-boot-starter 
mvn clean install
cd ../messages 
mvn clean install
```

При использовании docker-compose необходимо выполнить сборку docker образа для попадания пакетов в общий кэш мавена:

```shell
cd booking-spring-boot-starter && docker build . && cd ..
cd messages && docker build . && cd ..
```

Проверить что файл database/init.sh содержит линукс переводы строк (LF) <br/>
Если это не так, то сменить на LF (можно выполнить в IDE, открыв файл. Функция будет в правом нижнем углу)

### Запуск проекта

1) Все сервисы можно запустить как Spring Boot приложения прямо из idea, никакие дополнительные настройки не нужны.
   Перед запуском убедиться, что БД доступна по адресу `localhost:1433` (можно запустить отдельно в docker)

2) Через docker-compose (команды выполнять из корня проекта):

   Запуск всех сервисов со сборкой:

   `docker-compose up -d --build`

   Запуск с пересборкой в debug режиме (порты для подключения указаны в файле `docker-compose.debug.yml`):

   `docker-compose -f docker-compose.yml -f docker-compose.debug.yml up -d --build`

   Запуск вместе с EFK стеком:

   `docker-compose -f docker-compose.yml -f docker-compose.efk.yml up -d --build`

   Перезапуск отдельного сервиса с пересборкой:

   `docker-compose up -d --force-recreate --no-deps --build <service_name>`

### Проверка через Swagger

После запуска свагер доступен по адресу:

`http://localhost:8081/swagger-ui/`

В нем агргируется АПИ со всех сервисов, переключать сервисы можно в верхнем правом углу.

### Доступ к веб-интерфейсу файлового хранилища s3
`http://localhost:9301/`

логин и пароль по умолчанию - minioadmin:minioadmin

