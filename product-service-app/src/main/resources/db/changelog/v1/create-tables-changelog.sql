create sequence prices_id_seq start 1;
create sequence durations_id_seq start 1;
create sequence products_id_seq start 1;
create sequence discounts_id_seq start 1;

create table prices (
    id                  bigint primary key,
    price_value     	numeric(5,2) not null,
    currency 	        character(3) not null,
    created_at          timestamp with time zone not null,
    updated_at          timestamp with time zone not null
);

create table durations (
    id                bigint primary key,
    in_days           integer not null,
    created_at        timestamp with time zone not null,
    updated_at        timestamp with time zone not null
);

create table products (
    id          bigint primary key,
    summary     varchar (100) not null,
    description text not null,
    price_id    bigint not null,
    duration_id    bigint not null,
    active      boolean not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,
    constraint price_fk foreign key (price_id) references prices (id),
    constraint duration_fk foreign key (duration_id) references durations (id)
);

alter table products
add constraint summary_description_c
unique (summary, description);

create table discounts (
    id          	bigint primary key,
    discount_value  integer not null,
	datetime_from  	timestamp with time zone not null,
    datetime_until  timestamp with time zone not null,
	active      	boolean not null,
    created_at  	timestamp with time zone not null,
    updated_at  	timestamp with time zone not null
);

create table products_discounts (
    product_id          	bigint not null,
    discount_id     		bigint not null,
	constraint product_discount_pk primary key(product_id, discount_id),
	constraint discount_to_product_fk foreign key (product_id) references products (id),
	constraint product_to_discount_fk foreign key (discount_id) references discounts (id)
);
