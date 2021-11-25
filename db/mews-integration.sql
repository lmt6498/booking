--- bookings ---
alter table bookings
	add source varchar(255) null;

alter table bookings
	add external_id varchar(255) null;

alter table bookings
	add adult_count int null;

alter table bookings
	add child_count int null;

alter table bookings
	add last_message longtext null;

alter table bookings
    add integration_credential_id bigint null;

alter table bookings
    add cancel_time timestamp null;

--- booking_items ---

alter table booking_items
	add consumption_utc timestamp null;

