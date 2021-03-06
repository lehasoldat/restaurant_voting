INSERT INTO PUBLIC.USERS (EMAIL, NAME, PASSWORD)
VALUES ('user@mail.ru', 'user1', '{noop}user');

INSERT INTO PUBLIC.USERS (EMAIL, NAME, PASSWORD)
VALUES ('admin@mail.ru', 'admin', '{noop}admin');

INSERT INTO PUBLIC.USER_ROLES (USER_ID, ROLE)
VALUES (1, 'USER');

INSERT INTO PUBLIC.USER_ROLES (USER_ID, ROLE)
VALUES (2, 'ADMIN');

INSERT INTO PUBLIC.RESTAURANTS (NAME)
VALUES ('rest1');

INSERT INTO PUBLIC.RESTAURANTS (NAME)
VALUES ('rest2');

INSERT INTO PUBLIC.RESTAURANTS (NAME)
VALUES ('rest3');

INSERT INTO PUBLIC.MENUS (MENU_DATE, RESTAURANT_ID)
VALUES (now() - 1, 1);

INSERT INTO PUBLIC.MENUS (MENU_DATE, RESTAURANT_ID)
VALUES (now() - 1, 2);

INSERT INTO PUBLIC.MENUS (MENU_DATE, RESTAURANT_ID)
VALUES (now(), 1);

INSERT INTO PUBLIC.MENUS (MENU_DATE, RESTAURANT_ID)
VALUES (now(), 2);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (1, 'dish1_rest1_yesterday', 100);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (1, 'dish2_rest1_yesterday', 50);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (2, 'dish1_rest2_yesterday', 70);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (2, 'dish2_rest2_yesterday', 30);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (3, 'dish1_rest1_today', 150);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (3, 'dish2_rest1_today', 75);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (4, 'dish1_rest2_today', 120);

INSERT INTO PUBLIC.MENU_DISHES (MENU_ID, NAME, PRICE)
VALUES (4, 'dish2_rest2_today', 90);

INSERT INTO PUBLIC.VOTES (VOTING_DATE, VOTING_TIME, RESTAURANT_ID, USER_ID)
VALUES (now()-1, '10:00', 1, 1);

INSERT INTO PUBLIC.VOTES (VOTING_DATE, VOTING_TIME, RESTAURANT_ID, USER_ID)
VALUES (now()-1, '10:00', 2, 2);

INSERT INTO PUBLIC.VOTES (VOTING_DATE, VOTING_TIME, RESTAURANT_ID, USER_ID)
VALUES (now(), '00:00', 1, 1);
