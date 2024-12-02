ALTER TABLE Sessions
    ALTER COLUMN UserId DROP DEFAULT,  -- Удаляем значение по умолчанию, если оно есть
ALTER COLUMN UserId TYPE BIGINT;    -- Изменяем тип на BIGINT

-- Устанавливаем значение по умолчанию для UserId
ALTER TABLE Sessions
    ALTER COLUMN UserId SET DEFAULT nextval('user_id_seq');

-- Если необходимо, можно обновить существующие записи
UPDATE Sessions SET UserId = nextval('user_id_seq') WHERE UserId IS NULL;
