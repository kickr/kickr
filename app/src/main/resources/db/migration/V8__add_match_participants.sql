ALTER TABLE kickr_match ADD COLUMN participants varchar(500);

# set kickr_match.participants to `:` separated list of aliases
UPDATE kickr_match m SET m.participants = CONCAT(
  ':',
  (SELECT p.alias FROM kickr_player p WHERE p.id = m.team1_offense_id),
  ':',
  (SELECT p.alias FROM kickr_player p WHERE p.id = m.team1_defense_id),
  ':',
  (SELECT p.alias FROM kickr_player p WHERE p.id = m.team2_offense_id),
  ':',
  (SELECT p.alias FROM kickr_player p WHERE p.id = m.team2_defense_id),
  ':'
);