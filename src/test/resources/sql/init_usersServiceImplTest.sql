INSERT INTO "user" (id, fio, phone_number, email, password, available_minutes_for_booking, is_delete)
VALUES('21212121-2121-2121-2121-212121212121', 'Sidorov Ivan Ivanovich',
       '88002000600', 'sidorov.dev@gmail.com', 'password123', 120, false),
       ('32323232-3232-3232-3232-323232323232', 'Ignatiev Ignat Ignatievich',
       '88003000400', 'ignat@yandex.ru', '123password', 30, false );

INSERT INTO role (id, name)
VALUES('15151515-1515-1515-1515-151515151515', 'USER');

INSERT INTO user_role (user_id, role_id)
VALUES ('21212121-2121-2121-2121-212121212121', '15151515-1515-1515-1515-151515151515');
INSERT INTO user_role (user_id, role_id)
VALUES ('32323232-3232-3232-3232-323232323232', '15151515-1515-1515-1515-151515151515');


