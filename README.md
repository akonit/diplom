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

- *сейвы* -  лежат в папке **saves** в домашней директории проекта.

Про накат схемы БД
-------------

Скрип для создания БД - **create_db.sql** лежит в корне проекта. Накат:

   ```sql
   sqlite3 base.db < create_db.sql
   ```

base.db поместить в директорию **configuration**.

Изменение скрипта предполагается производить в файле **diplom_repo.vpp**. Открыть это можно при помощи Visual Paradigm for UML, изменения в схеме экспортировать командой `Utilities -> Generate SQL`.

Про лицензию
----------
Данный продукт распространяется под свободной лицензией **BSD-3-Clause**
