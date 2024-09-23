INSERT INTO "user" (id, fio, phone_number, email, password, available_minutes_for_booking, is_delete)
VALUES ('21212121-2121-2121-2121-212121212121', 'Sidorov Ivan Ivanovich',
        '88002000600', 'sidorov.dev@gmail.com', 'password123', 120, false),
       ('32323232-3232-3232-3232-323232323232', 'Ignatiev Ignat Ignatievich',
        '88003000400', 'ignat@yandex.ru', '123password', 30, false),
       ('42424242-4242-4242-4242-424242424242', 'Petrov Ignat Petrovich',
        '82003000700', 'petrov.dev@gmail.com', 'password112', 20, true);

INSERT INTO role (id, name)
VALUES ('15151515-1515-1515-1515-151515151515', 'USER');

INSERT INTO user_role (user_id, role_id)
VALUES ('21212121-2121-2121-2121-212121212121', '15151515-1515-1515-1515-151515151515'),
       ('32323232-3232-3232-3232-323232323232', '15151515-1515-1515-1515-151515151515'),
       ('42424242-4242-4242-4242-424242424242', '15151515-1515-1515-1515-151515151515');

INSERT INTO office (id, address, name, is_delete)
VALUES ('11111111-1111-1111-1111-111111111111', '789 Oak St, Capital City', 'Remote Office', false);

INSERT INTO room (id, name, floor_number, room_number, is_delete, office_id)
VALUES ('33333333-3333-3333-3333-333333333333', 'Small meeting room', 3, 15, false,
        '11111111-1111-1111-1111-111111111111');

INSERT INTO workplace (id, number, description, is_delete, room_id)
VALUES ('55555555-5555-5555-5555-555555555555', 1, 'Workplace near window', false,
        '33333333-3333-3333-3333-333333333333');

INSERT INTO booking (id, booking_date_time, booking_start_date_time, booking_end_date_time,
                     booking_cancel_date_time, booking_cancel_reason, is_booked, user_id, workplace_id)
VALUES ('00000000-0000-0000-0000-000000000003', to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'),
        to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'), to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'),
        to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'), '', false,
        '21212121-2121-2121-2121-212121212121', '55555555-5555-5555-5555-555555555555');
