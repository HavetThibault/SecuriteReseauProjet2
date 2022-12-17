DROP table clients;

create table Clients
(
	Login varchar(30),
    Name varchar(30),
    Firstname varchar(30),
    PasswordDate bigint,
    PasswordRandomNumber double,
    Password blob(5000),
    constraint Clients$pk PRIMARY KEY (Login)
);

select * from clients;