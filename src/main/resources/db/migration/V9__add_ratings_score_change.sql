/* clean up old scorings */
DROP TABLE kickr_score_change;
DROP TABLE kickr_score;
DROP TABLE IF EXISTS kickr_rating;

CREATE TABLE kickr_score (
  id bigint NOT NULL,
  created timestamp NOT NULL,
  score_type varchar(255) NOT NULL,
  value integer NOT NULL,
  run_index integer NOT NULL DEFAULT 0,
  player_id bigint,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE kickr_score ADD CONSTRAINT FK_kickr_score_player_id FOREIGN KEY (player_id) REFERENCES kickr_player (id);

ALTER TABLE kickr_score ADD INDEX IDX_kickr_score_latest (created, score_type, player_id);

CREATE TABLE kickr_rating (
  id bigint NOT NULL,
  created timestamp NOT NULL,
  mean double precision NOT NULL,
  standard_deviation double precision NOT NULL,
  player_id bigint,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE kickr_rating ADD CONSTRAINT FK_kickr_rating_player_id FOREIGN KEY (player_id) REFERENCES kickr_player (id);

ALTER TABLE kickr_rating ADD INDEX IDX_kickr_rating_latest (created, player_id);

CREATE TABLE kickr_score_change (
  id bigint NOT NULL,
  created timestamp NOT NULL,
  change_type varchar(255) NOT NULL,
  score_type varchar(255) NOT NULL,
  change_value integer NOT NULL,
  match_id bigint NOT NULL,
  player_id bigint NOT NULL,
  rating_id bigint NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE kickr_score_change ADD CONSTRAINT FK_kickr_score_change_match_id FOREIGN KEY (match_id) REFERENCES kickr_match (id);
ALTER TABLE kickr_score_change ADD CONSTRAINT FK_kickr_score_change_player_id FOREIGN KEY (player_id) REFERENCES kickr_player (id);
ALTER TABLE kickr_score_change ADD CONSTRAINT FK_kickr_score_change_rating_id FOREIGN KEY (rating_id) REFERENCES kickr_rating (id);

ALTER TABLE kickr_score_change ADD INDEX IDX_kickr_score_change_latest (created, score_type, player_id);

/* reset rated flag */
UPDATE kickr_match SET rated = false;