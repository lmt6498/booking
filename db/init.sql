drop table if exists booking_guest;
drop table if exists booking_item;
drop table if exists booking;

create table booking
(
    booking_id bigint auto_increment primary key,
    client_id varchar(255),
    business_id varchar(255),
    identifier varchar(255),
    booked_at timestamp null,
    start_time timestamp null,
    end_time timestamp null,
    timezone varchar(255),
    type varchar(255),
    status varchar(255),
    created_by_client_id varchar(255),
    created_at timestamp null,
    created_by varchar(255),
    modified_at timestamp null,
    modified_by varchar(255)
);


create table booking_item
(
    booking_item_id bigint auto_increment primary key,
    booking_id bigint not null,
    name varchar(255),
    type varchar(255),
    status varchar(255),
    constraint booking_item_booking_booking_id_fk
        foreign key (booking_id) references booking (booking_id)
);

create table booking_guest
(
    booking_guest_id bigint auto_increment primary key,
    booking_id bigint not null,
    is_owner boolean default false,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    mobile_number varchar(255),
    status varchar(255),
    constraint booking_guest_booking_booking_id_fk
        foreign key (booking_id) references booking (booking_id)
);

