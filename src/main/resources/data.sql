-- create or replace function UNIX_TIMESTAMP()
--  returns bigint language plpgsql
-- as
-- $$
-- declare
--  unix_time bigint;
-- begin
--  select round(extract(epoch from now())) into unix_time;
--  return unix_time;
-- end;
-- 	$$;

insert into roles values(default, 'USER');
insert into roles values(default, 'ADMIN');

insert into categories values(default, 'laptops', 'Laptops', 'Ноутбуки');

insert into users (balance, birth_date, email, first_name, image_id, last_name, login, password, role_id) values (999999.0, '2022-11-03', 'arsen@mail.com', 'Arsen', null, 'Sydoryk', 'ArsenOOO7', '$2a$10$CJgEoobU2gm0euD4ygru4ukBf9g8fYnPrMvYk.q0GMfOcIDtUhEwC', 1);
-- insert into users (balance, birth_date, email, first_name, image_id, last_name, login, password, role_id) values (999999.0, '2022-11-03', 'arsen@gmail.com', 'Arsen', null, 'Sydoryk', 'ArsenOOO8', '$2a$10$CJgEoobU2gm0euD4ygru4ukBf9g8fYnPrMvYk.q0GMfOcIDtUhEwC', 2);

-- insert into users_block(user_id, reason, start_time, end_time) values(1, 'Test reason', (select round(extract(epoch from now()))), (select round(extract(epoch from now()))) + 90000);