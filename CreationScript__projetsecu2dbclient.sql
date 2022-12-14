DROP table clients;

create table Clients
(
	Login varchar(30),
    Name varchar(30),
    Firstname varchar(30),
    Password varchar(30),
    constraint Clients$pk PRIMARY KEY (Login)
);

insert into clients value ("One-punch man", "Saitama", "Saitama", "Poing");
commit;

select * from clients;