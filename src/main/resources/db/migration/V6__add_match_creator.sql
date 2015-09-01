ALTER TABLE kickr_match ADD COLUMN creator_id bigint;

ALTER TABLE kickr_match ADD CONSTRAINT FK_kickr_match_creator_id FOREIGN KEY (creator_id) REFERENCES kickr_user (id) ON DELETE SET NULL;