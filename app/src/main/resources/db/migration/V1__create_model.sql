CREATE TABLE hibernate_sequence (
  next_val bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO hibernate_sequence VALUES (1);

CREATE TABLE kickr_player (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  alias varchar(255) NOT NULL,
  email varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY alias_unique (alias)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE kickr_table (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  name varchar(255) DEFAULT NULL,
  team1Alias varchar(255) DEFAULT NULL,
  team2Alias varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE kickr_game (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  gameNumber int(11) NOT NULL,
  scoreTeam1 int(11) NOT NULL,
  scoreTeam2 int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE kickr_match (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  played datetime DEFAULT NULL,
  rated bit(1) NOT NULL,
  removed bit(1) NOT NULL,
  table_id bigint(20) NOT NULL,
  team1_defense_id bigint(20) DEFAULT NULL,
  team1_offense_id bigint(20) DEFAULT NULL,
  team2_defense_id bigint(20) DEFAULT NULL,
  team2_offense_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_table_id (table_id),
  KEY FK_team1_defense_id (team1_defense_id),
  KEY FK_team1_offense_id (team1_offense_id),
  KEY FK_team2_defense_id (team2_defense_id),
  KEY FK_team2_offense_id (team2_offense_id),
  CONSTRAINT FK_table_id FOREIGN KEY (table_id) REFERENCES kickr_table (id),
  CONSTRAINT FK_team2_offense_id FOREIGN KEY (team2_offense_id) REFERENCES kickr_player (id),
  CONSTRAINT FK_team2_defense_id FOREIGN KEY (team2_defense_id) REFERENCES kickr_player (id),
  CONSTRAINT FK_team1_offense_id FOREIGN KEY (team1_offense_id) REFERENCES kickr_player (id),
  CONSTRAINT FK_team1_defense_id FOREIGN KEY (team1_defense_id) REFERENCES kickr_player (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE kickr_match_kickr_game (
  kickr_match_id bigint(20) NOT NULL,
  games_id bigint(20) NOT NULL,
  UNIQUE KEY games_id_unique (games_id),
  KEY FK_kickr_match_id (kickr_match_id),
  CONSTRAINT FK_kickr_match_id FOREIGN KEY (kickr_match_id) REFERENCES kickr_match (id),
  CONSTRAINT FK_games_id FOREIGN KEY (games_id) REFERENCES kickr_game (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE kickr_score (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  lastUpdated datetime DEFAULT NULL,
  value int(11) NOT NULL,
  player_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_player_id (player_id),
  CONSTRAINT FK_player_id FOREIGN KEY (player_id) REFERENCES kickr_player (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;