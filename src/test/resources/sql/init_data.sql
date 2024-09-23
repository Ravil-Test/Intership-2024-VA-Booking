INSERT INTO office (id, address, name, is_delete)
VALUES ('11111111-1111-1111-1111-111111111111', '789 Oak St, Capital City', 'Remote Office', false),
       ('22222222-2222-2222-2222-222222222222', '123 Elm St, Downtown', 'Main Office', false);

INSERT INTO room (id, name, floor_number, room_number, is_delete, office_id)
VALUES ('33333333-3333-3333-3333-333333333333', 'Small meeting room', 3, 15, false,
        '11111111-1111-1111-1111-111111111111'),
       ('44444444-4444-4444-4444-444444444444', 'Large conference room', 2, 10, false,
        '22222222-2222-2222-2222-222222222222');

INSERT INTO workplace (id, number, description, is_delete, room_id)
VALUES ('55555555-5555-5555-5555-555555555555', 1, 'Workplace near window', false,
        '33333333-3333-3333-3333-333333333333'),
       ('66666666-6666-6666-6666-666666666666', 2, 'Workplace with desk', false,
        '44444444-4444-4444-4444-444444444444');

INSERT INTO "user" (id, fio, phone_number, email, password, available_minutes_for_booking, is_delete)
VALUES ('21212121-2121-2121-2121-212121212121', 'Sidorov Ivan Ivanovich',
        '88002000600', 'sidorov.dev@gmail.com', 'password123', 120, false),
       ('32323232-3232-3232-3232-323232323232', 'Ignatiev Ignat Ignatievich',
        '88003000400', 'ignat@yandex.ru', '123password', 30, false);

INSERT INTO role (id, name)
VALUES ('15151515-1515-1515-1515-151515151515', 'USER');

INSERT INTO user_role (user_id, role_id)
VALUES ('21212121-2121-2121-2121-212121212121', '15151515-1515-1515-1515-151515151515'),
       ('32323232-3232-3232-3232-323232323232', '15151515-1515-1515-1515-151515151515');

INSERT INTO booking (id, booking_date_time, booking_start_date_time, booking_end_date_time,
                     booking_cancel_date_time, booking_cancel_reason, is_booked, user_id, workplace_id)
VALUES ('00000000-0000-0000-0000-000000000001', '2024-08-30T12:11:50.077721', '2024-08-30T12:11:50.077721',
        '2024-08-30T12:11:50.077721', '2024-08-30T12:11:50.077721', 'Reason #1', false,
        '21212121-2121-2121-2121-212121212121', '55555555-5555-5555-5555-555555555555'),
       ('00000000-0000-0000-0000-000000000002', '2024-08-30T12:11:50.077721', '2024-08-30T12:11:50.077721',
        '2024-08-30T12:11:50.077721', '2024-08-30T12:11:50.077721', '', false,
        '21212121-2121-2121-2121-212121212121', '55555555-5555-5555-5555-555555555555'),
       ('00000000-0000-0000-0000-000000000003', to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'),
        to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'), to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'),
        to_char(current_timestamp, 'YYYY-MM-DD HH24:MI'), '', false,
        '21212121-2121-2121-2121-212121212121', '55555555-5555-5555-5555-555555555555');