create database puss1302;
use puss1302;
create table users (
    id int not null auto_increment,
    username varchar(10) not null,
    password varchar(6) not null,
    active bool not null default 1,
    role int not null default 3,
    project_group_id int,
    primary key (id),
    unique key (username)
);
insert into users (username, password, role) values('admin', 'adminp', 1);
create table project_groups (
    id int not null auto_increment,
    project_name varchar(10) not null,
    active bool not null default 1,
    start_week int not null,
    end_week int not null,
    estimated_time int not null,
    primary key (id),
    unique key (project_name)
);
create table time_reports (
    id int not null auto_increment,
    week int not null,
    signed bool not null default 0,
    user_id int not null,
    project_group_id int,
    primary key (id),
    unique key (week, user_id)
);
create table activities (
    id int not null auto_increment,
    activity_nr int not null,
    activity_type varchar(1) not null,
    time int not null,
    time_report_id int not null,
    primary key (id)
);