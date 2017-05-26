# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table accommodation (
  id                            bigint auto_increment not null,
  rent                          integer,
  size                          double,
  rooms                         double,
  deposit                       integer,
  renter_id                     bigint,
  address_id                    bigint,
  rental_period_id              bigint,
  image                         blob,
  constraint uq_accommodation_renter_id unique (renter_id),
  constraint pk_accommodation primary key (id)
);

create table activity (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  constraint uq_activity_name unique (name),
  constraint pk_activity primary key (id)
);

create table activity_choice (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  swiping_session_id            bigint,
  constraint uq_activity_choice_user_id_swiping_session_id unique (user_id,swiping_session_id),
  constraint pk_activity_choice primary key (id)
);

create table activity_choice_activity (
  activity_choice_id            bigint not null,
  activity_id                   bigint not null,
  constraint pk_activity_choice_activity primary key (activity_choice_id,activity_id)
);

create table address (
  id                            bigint auto_increment not null,
  street_name                   varchar(255),
  street_number                 integer,
  street_number_letter          varchar(255),
  area                          varchar(255),
  address_description_id        bigint,
  longitude                     double,
  latitude                      double,
  constraint uq_address_address_description_id unique (address_description_id),
  constraint pk_address primary key (id)
);

create table address_description (
  id                            bigint auto_increment not null,
  constraint pk_address_description primary key (id)
);

create table city_distance (
  id                            bigint auto_increment not null,
  duration                      integer,
  constraint pk_city_distance primary key (id)
);

create table distance (
  id                            bigint auto_increment not null,
  address_description_id        bigint not null,
  distance_name                 varchar(255) not null,
  meters                        integer,
  duration                      integer,
  constraint pk_distance primary key (id)
);

create table edge (
  id                            bigint auto_increment not null,
  renter_id                     bigint,
  tenant_id                     bigint,
  active                        boolean,
  constraint uq_edge_renter_id_tenant_id unique (renter_id,tenant_id),
  constraint pk_edge primary key (id)
);

create table facebook_data (
  id                            bigint auto_increment not null,
  facebook_user_id              varchar(255) not null,
  email                         varchar(255) not null,
  first_name                    varchar(256),
  last_name                     varchar(256),
  name                          varchar(255),
  gender                        varchar(15),
  locale                        varchar(10),
  timezone                      integer,
  user_id                       bigint,
  image                         blob,
  constraint uq_facebook_data_facebook_user_id unique (facebook_user_id),
  constraint uq_facebook_data_email unique (email),
  constraint uq_facebook_data_user_id unique (user_id),
  constraint pk_facebook_data primary key (id)
);

create table rental_period (
  id                            bigint auto_increment not null,
  start                         datetime,
  end                           datetime,
  constraint pk_rental_period primary key (id)
);

create table renter (
  test                          varchar(255)
);

create table swiping_session (
  id                            bigint auto_increment not null,
  initialization_date           datetime not null,
  constraint pk_swiping_session primary key (id)
);

create table swiping_session_users (
  swiping_session_id            bigint not null,
  users_id                      bigint not null,
  constraint pk_swiping_session_users primary key (swiping_session_id,users_id)
);

create table swiping_session_activity (
  swiping_session_id            bigint not null,
  activity_id                   bigint not null,
  constraint pk_swiping_session_activity primary key (swiping_session_id,activity_id)
);

create table tenant_profile (
  id                            bigint auto_increment not null,
  max_rent                      integer,
  max_deposit                   integer,
  min_size                      double,
  description                   varchar(255),
  rental_period_id              bigint,
  constraint pk_tenant_profile primary key (id)
);

create table users (
  dtype                         varchar(10) not null,
  id                            bigint auto_increment not null,
  auth_token                    varchar(255),
  email_address                 varchar(255),
  sha_password                  varbinary(64),
  sha_facebook_auth_token       varbinary(255),
  full_name                     varchar(255),
  creation_date                 datetime not null,
  age                           integer,
  facebook_data_id              bigint,
  accommodation_id              bigint,
  tenant_profile_id             bigint,
  authorization                 integer,
  constraint ck_users_authorization check (authorization in (0,1)),
  constraint uq_users_email_address unique (email_address),
  constraint uq_users_full_name unique (full_name),
  constraint uq_users_facebook_data_id unique (facebook_data_id),
  constraint uq_users_accommodation_id unique (accommodation_id),
  constraint uq_users_tenant_profile_id unique (tenant_profile_id),
  constraint pk_users primary key (id)
);

alter table accommodation add constraint fk_accommodation_renter_id foreign key (renter_id) references users (id) on delete restrict on update restrict;

alter table accommodation add constraint fk_accommodation_address_id foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_accommodation_address_id on accommodation (address_id);

alter table accommodation add constraint fk_accommodation_rental_period_id foreign key (rental_period_id) references rental_period (id) on delete restrict on update restrict;
create index ix_accommodation_rental_period_id on accommodation (rental_period_id);

alter table activity_choice add constraint fk_activity_choice_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_activity_choice_user_id on activity_choice (user_id);

alter table activity_choice add constraint fk_activity_choice_swiping_session_id foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_activity_choice_swiping_session_id on activity_choice (swiping_session_id);

alter table activity_choice_activity add constraint fk_activity_choice_activity_activity_choice foreign key (activity_choice_id) references activity_choice (id) on delete restrict on update restrict;
create index ix_activity_choice_activity_activity_choice on activity_choice_activity (activity_choice_id);

alter table activity_choice_activity add constraint fk_activity_choice_activity_activity foreign key (activity_id) references activity (id) on delete restrict on update restrict;
create index ix_activity_choice_activity_activity on activity_choice_activity (activity_id);

alter table address add constraint fk_address_address_description_id foreign key (address_description_id) references address_description (id) on delete restrict on update restrict;

alter table distance add constraint fk_distance_address_description_id foreign key (address_description_id) references address_description (id) on delete restrict on update restrict;
create index ix_distance_address_description_id on distance (address_description_id);

alter table edge add constraint fk_edge_renter_id foreign key (renter_id) references users (id) on delete restrict on update restrict;
create index ix_edge_renter_id on edge (renter_id);

alter table edge add constraint fk_edge_tenant_id foreign key (tenant_id) references users (id) on delete restrict on update restrict;
create index ix_edge_tenant_id on edge (tenant_id);

alter table facebook_data add constraint fk_facebook_data_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

alter table swiping_session_users add constraint fk_swiping_session_users_swiping_session foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_swiping_session_users_swiping_session on swiping_session_users (swiping_session_id);

alter table swiping_session_users add constraint fk_swiping_session_users_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_swiping_session_users_users on swiping_session_users (users_id);

alter table swiping_session_activity add constraint fk_swiping_session_activity_swiping_session foreign key (swiping_session_id) references swiping_session (id) on delete restrict on update restrict;
create index ix_swiping_session_activity_swiping_session on swiping_session_activity (swiping_session_id);

alter table swiping_session_activity add constraint fk_swiping_session_activity_activity foreign key (activity_id) references activity (id) on delete restrict on update restrict;
create index ix_swiping_session_activity_activity on swiping_session_activity (activity_id);

alter table tenant_profile add constraint fk_tenant_profile_rental_period_id foreign key (rental_period_id) references rental_period (id) on delete restrict on update restrict;
create index ix_tenant_profile_rental_period_id on tenant_profile (rental_period_id);

alter table users add constraint fk_users_facebook_data_id foreign key (facebook_data_id) references facebook_data (id) on delete restrict on update restrict;

alter table users add constraint fk_users_accommodation_id foreign key (accommodation_id) references accommodation (id) on delete restrict on update restrict;

alter table users add constraint fk_users_tenant_profile_id foreign key (tenant_profile_id) references tenant_profile (id) on delete restrict on update restrict;


# --- !Downs

alter table accommodation drop constraint if exists fk_accommodation_renter_id;

alter table accommodation drop constraint if exists fk_accommodation_address_id;
drop index if exists ix_accommodation_address_id;

alter table accommodation drop constraint if exists fk_accommodation_rental_period_id;
drop index if exists ix_accommodation_rental_period_id;

alter table activity_choice drop constraint if exists fk_activity_choice_user_id;
drop index if exists ix_activity_choice_user_id;

alter table activity_choice drop constraint if exists fk_activity_choice_swiping_session_id;
drop index if exists ix_activity_choice_swiping_session_id;

alter table activity_choice_activity drop constraint if exists fk_activity_choice_activity_activity_choice;
drop index if exists ix_activity_choice_activity_activity_choice;

alter table activity_choice_activity drop constraint if exists fk_activity_choice_activity_activity;
drop index if exists ix_activity_choice_activity_activity;

alter table address drop constraint if exists fk_address_address_description_id;

alter table distance drop constraint if exists fk_distance_address_description_id;
drop index if exists ix_distance_address_description_id;

alter table edge drop constraint if exists fk_edge_renter_id;
drop index if exists ix_edge_renter_id;

alter table edge drop constraint if exists fk_edge_tenant_id;
drop index if exists ix_edge_tenant_id;

alter table facebook_data drop constraint if exists fk_facebook_data_user_id;

alter table swiping_session_users drop constraint if exists fk_swiping_session_users_swiping_session;
drop index if exists ix_swiping_session_users_swiping_session;

alter table swiping_session_users drop constraint if exists fk_swiping_session_users_users;
drop index if exists ix_swiping_session_users_users;

alter table swiping_session_activity drop constraint if exists fk_swiping_session_activity_swiping_session;
drop index if exists ix_swiping_session_activity_swiping_session;

alter table swiping_session_activity drop constraint if exists fk_swiping_session_activity_activity;
drop index if exists ix_swiping_session_activity_activity;

alter table tenant_profile drop constraint if exists fk_tenant_profile_rental_period_id;
drop index if exists ix_tenant_profile_rental_period_id;

alter table users drop constraint if exists fk_users_facebook_data_id;

alter table users drop constraint if exists fk_users_accommodation_id;

alter table users drop constraint if exists fk_users_tenant_profile_id;

drop table if exists accommodation;

drop table if exists activity;

drop table if exists activity_choice;

drop table if exists activity_choice_activity;

drop table if exists address;

drop table if exists address_description;

drop table if exists city_distance;

drop table if exists distance;

drop table if exists edge;

drop table if exists facebook_data;

drop table if exists rental_period;

drop table if exists renter;

drop table if exists swiping_session;

drop table if exists swiping_session_users;

drop table if exists swiping_session_activity;

drop table if exists tenant_profile;

drop table if exists users;

