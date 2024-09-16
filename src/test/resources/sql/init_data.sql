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
