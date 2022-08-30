-- create database
create database if not exists gamedoanso;
use gamedoanso;

-- create tables
create table if not exists player(
	username varchar(255),
    password varchar(255) not null,
    name	 varchar(255) not null,
    primary  key(username)
);

create table if not exists game_session(
	id 			varchar(9),
    target 		int not null,
    start_time 	timestamp not null,
    end_time 	timestamp,
    username 	varchar(255) not null,
    completed 	int(1) not null default (0),
    active 		int(1) not null default (1),
    primary key(id)
);

create table if not exists guess(
	id 			int auto_increment,
    value 		int not null,
    result		int not null,
    moment 		timestamp not null,
    session_id 	varchar(9) not null,
    primary key(id)
);

create table if not exists token(
	selector varchar(36),
    validator varchar(36),
    username varchar(100),
    primary key (selector)
);

-- add foreign key

alter table game_session
	add constraint fk_game_session_player
	foreign key (username) references player(username);

alter table guess
	add constraint fk_guess_game_session
    foreign key (session_id) references game_session(id);

alter table token
	add constraint fk_token_player
    foreign key (username) references player(username)

-- Create a procedure to ranking by number of guesses and time to complete game
DELIMITER $$
CREATE PROCEDURE ranking()
BEGIN
  CREATE OR REPLACE VIEW ranking
	AS
		select 	gs.*,
			(select count(*) from guess g  where g.session_id = gs.id ) as guesses,
			(select TIMESTAMPDIFF(second, gs.start_time, gs.end_time)) as times
		from game_session as gs
		where gs.completed = 1
		order by  guesses, times;
END; $$
DELIMITER ;
call ranking();

-- create an event to ranking on schedule every 5 minutes
CREATE
	EVENT `ranking_every_5_minutes`
	ON SCHEDULE EVERY 5 minute
	STARTS TIMESTAMP(NOW() + INTERVAL 1 MINUTE)
	DO
		CALL ranking();

