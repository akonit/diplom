diplom
======

Заметки по запуску и тестированию
---------------------------------

- *запуск*:
  
  ```shell
  griffon run-app
  ```
- *компиляция*:

  ```shell
  griffon compile
  ```
- *тестирование* - **запускать только после компиляции**:

  ```shell
  griffon test-app
  ```

- *сейвы* -  лежат в папке saves в домашней директории проекта.

Про накат схемы БД
-------------

Скрип для создания БД - create_db.sql лежит в корне проекта. Накат:

   ```sql
   sqlite3 base.db < create_db.sql
   ```

base.db искать в sr/main/resources.
