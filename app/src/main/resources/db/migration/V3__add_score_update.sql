CREATE TABLE kickr_score_change (
  id bigint(20) NOT NULL,
  created datetime NOT NULL,
  change_value integer NOT NULL,
  change_type integer NOT NULL,
  match_id bigint(20),
  player_id bigint(20),
  score_id bigint(20),
  PRIMARY KEY (id),
  CONSTRAINT FK_kickr_score_change_match_id FOREIGN KEY (match_id) REFERENCES kickr_match (id),
  CONSTRAINT FK_kickr_score_change_player_id FOREIGN KEY (player_id) REFERENCES kickr_player (id) ON DELETE CASCADE,
  CONSTRAINT FK_kickr_score_change_score_id FOREIGN KEY (score_id) REFERENCES kickr_score (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;