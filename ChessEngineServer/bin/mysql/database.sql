create database chessEngine; 
use chessEngine; 

create table user (
    username varchar(40) not null primary key,
	email varchar(50) not null, 
	passwort varchar(5000) not null, 
    points int default 1000, 
    games int default 0, 
    loggedIn tinyint(1) default 0, 
    isBanned tinyint(1) default 0
);
