Перед использованием PostgreSQL как хранилища секретов в Vault необходимо создать БД, роль и таблицу с индексом:

```sql
CREATE TABLE vault_kv_store (
  parent_path TEXT COLLATE "C" NOT NULL,
  path        TEXT COLLATE "C",
  key         TEXT COLLATE "C",
  value       BYTEA,
  CONSTRAINT pkey PRIMARY KEY (path, key)
);

CREATE INDEX parent_path_idx ON vault_kv_store (parent_path);
```

После настройки Vault и первого запуска необходимо проинициализировать хранилище. Перед выполнением команды инициализации в переменную окружения `VAULT_ADDR` записать адрес Vault.
```bash
export VAULT_ADDR=http://<host>:<port>
vault operator init
```

Полученные unseal ключи и root токен необходимо сохранить. Unseal ключи необходимы для распечатывания хранилища после перезапуска Vault. Root токен необходим для аутентификации.

После инициализации необходимо распечатать Vault, чтобы с ним можно было работать.

```bash
vault operator unseal
```

Для добавления секретов из json файла необходимо выполнить:
```bash
vault kv put secret/<service_name> @<service_name>.json
```