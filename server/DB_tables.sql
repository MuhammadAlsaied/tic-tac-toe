
CREATE TABLE player (id int(250) not null primary key auto_increment,
 first_name varchar(250) not null,
 last_name varchar(250) not null,
 email varchar(50) not null,
 image varchar(250),
 password varchar(250),
 points int(11)); 

create table game(id int(11) not null primary key auto_increment,
 player1_id int(11), 
 player2_id int(11), 
 session_closed json);


