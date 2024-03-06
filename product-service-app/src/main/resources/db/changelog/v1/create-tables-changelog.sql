create table prices (
    id          bigserial primary key,
    value     	numeric(5,2) not null,
    currency 	character(3) not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null
);

create table products (
    id          bigserial primary key,
    summary     varchar (100) not null,
    description text not null,
    price_id    bigint not null,
    active      boolean not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,
    constraint price_fk foreign key (price_id) references prices (id)
);

alter table products
add constraint summary_description_c
unique (summary, description);

create table durations (
    id          bigserial primary key,
    value     	integer not null,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,
	product_id	bigint not null,
	constraint product_fk foreign key (product_id) references products (id)
);

create table discounts (
    id          	bigserial primary key,
    value     		integer not null,
	datetime_from  	timestamp with time zone not null,
    datetime_until  timestamp with time zone not null,
    created_at  	timestamp with time zone not null,
    updated_at  	timestamp with time zone not null
);

create table products_discounts (
    product_id          	bigserial not null,
    discount_id     		bigserial not null,
	constraint product_discount_pk primary key(product_id, discount_id),
	constraint discount_to_product_fk foreign key (product_id) references products (id),
	constraint product_to_discount_fk foreign key (discount_id) references discounts (id)
);
