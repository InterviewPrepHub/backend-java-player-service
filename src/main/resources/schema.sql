DROP TABLE IF EXISTS PLAYERS;

-- Create a table from the csv
CREATE TABLE PLAYERS AS SELECT * FROM CSVREAD('Player.csv');

-- Creates an index on the NAMEFIRST column, which maps to the firstName field in your Player entity.
CREATE INDEX idx_namefirst ON PLAYERS(NAMEFIRST);

-- SELECT * FROM INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = 'PLAYERS';




CREATE INDEX idx_birth_country ON PLAYERS(BIRTHCOUNTRY);
CREATE INDEX idx_bats ON PLAYERS(BATS);
CREATE INDEX idx_debut ON PLAYERS(DEBUT);
CREATE INDEX idx_last_name ON PLAYERS(NAMELAST);