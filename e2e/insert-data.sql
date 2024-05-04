insert into prices(id, price_value, currency, created_at, updated_at)
values (1, 700.00, 'USD', current_timestamp, current_timestamp),
       (2, 300.00, 'USD', current_timestamp, current_timestamp),
       (3, 50.00, 'USD', current_timestamp, current_timestamp),
       (4, 120.00, 'USD', current_timestamp, current_timestamp),
       (5, 30.00, 'USD', current_timestamp, current_timestamp);

alter sequence prices_id_seq restart with 6;

insert into durations(id, in_days, created_at, updated_at)
values (1, 90, current_timestamp, current_timestamp),
       (2, 60, current_timestamp, current_timestamp),
       (3, 1, current_timestamp, current_timestamp),
       (4, 30, current_timestamp, current_timestamp),
       (5, 1, current_timestamp, current_timestamp);

alter sequence durations_id_seq restart with 6;

insert into products(id, summary, description, price_id, duration_id, active, created_at, updated_at)
values (1,
       'Spring in Action',
       'This is course about Spring',
       1,
       1,
       true,
       current_timestamp,
       current_timestamp),

       (2,
       'Netty in Action',
       'This is course about Netty',
       2,
       2,
       true,
       current_timestamp,
       current_timestamp),

       (3,
       'AWS',
       'This is course about cloud AWS',
       3,
       3,
       true,
       current_timestamp,
       current_timestamp),

       (4,
       'Algorithms',
       'This is course about algorithms and date structure',
       4,
       4,
       true,
       current_timestamp,
       current_timestamp),

       (5,
       'Mock interview',
       'One hour mock interview by Maksim Dobrynin',
       5,
       5,
       true,
       current_timestamp,
       current_timestamp);

alter sequence products_id_seq restart with 6;

insert into discounts(id, discount_value, datetime_from, datetime_until, active, created_at, updated_at)
values (1,
       5,
       current_timestamp + interval '1 hour',
       current_timestamp + interval '20 day',
		true,
       current_timestamp,
       current_timestamp),

       (2,
       7,
       current_timestamp + interval '1 hour',
       current_timestamp + interval '10 day',
		true,
       current_timestamp,
       current_timestamp),

       (3,
       20,
       current_timestamp + interval '1 hour',
       current_timestamp + interval '15 day',
		true,
       current_timestamp,
       current_timestamp);

alter sequence discounts_id_seq restart with 4;

insert into products_discounts(product_id, discount_id)
values (1, 3),
       (2, 2),
       (3, 2),
       (4, 1),
       (5, 1);
