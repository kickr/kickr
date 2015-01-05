ALTER TABLE kickr_game ADD COLUMN match_id bigint;

UPDATE kickr_game g SET g.match_id = (SELECT m.kickr_match_id FROM kickr_match_kickr_game m WHERE m.games_id = g.id);

ALTER TABLE kickr_game ADD CONSTRAINT FK_kickr_game_match_id FOREIGN KEY (match_id) REFERENCES kickr_match (id) ON DELETE CASCADE;

DROP TABLE kickr_match_kickr_game;