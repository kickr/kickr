ALTER TABLE kickr_match ADD COLUMN team1Wins integer not null default 0;
ALTER TABLE kickr_match ADD COLUMN team2Wins integer not null default 0;
ALTER TABLE kickr_match ADD COLUMN totalGames integer not null default 0;


UPDATE kickr_match m SET m.team1Wins =
  (SELECT COUNT(*) FROM kickr_game g WHERE g.match_id = m.id AND g.scoreTeam1 > g.scoreTeam2);

UPDATE kickr_match m SET m.team2Wins =
  (SELECT COUNT(*) FROM kickr_game g WHERE g.match_id = m.id AND g.scoreTeam1 < g.scoreTeam2);

UPDATE kickr_match m SET m.totalGames =
  (SELECT COUNT(*) FROM kickr_game g WHERE g.match_id = m.id);