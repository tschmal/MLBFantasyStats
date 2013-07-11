-- Parent league

CREATE TABLE league
(
  id integer NOT NULL,
  fantasy_id integer NOT NULL,
  year smallint NOT NULL,
  name text NOT NULL,
  service text NOT NULL,
  CONSTRAINT league_pkey PRIMARY KEY (id ),
  CONSTRAINT league_fantasy_id_year_service_key UNIQUE (fantasy_id , year , service )
);

--
-- Categories
--

CREATE TABLE category
(
  id integer NOT NULL,
  league_id integer NOT NULL,
  category character varying(5) NOT NULL,
  category_type character(1) NOT NULL,
  points numeric,
  CONSTRAINT category_pkey PRIMARY KEY (id ),
  CONSTRAINT category_league_id_fkey FOREIGN KEY (league_id)
      REFERENCES league (id) ON DELETE CASCADE,
  CONSTRAINT category_league_id_category_category_type_key UNIQUE (league_id , category , category_type )
);

CREATE INDEX category_league_ix ON category (league_id);

--
-- Scoring periods
--

CREATE TABLE scoring_period
(
  id integer NOT NULL,
  league_id integer NOT NULL,
  period_id smallint NOT NULL,
  date date NOT NULL,
  CONSTRAINT scoring_period_pkey PRIMARY KEY (id ),
  CONSTRAINT scoring_period_league_id_fkey FOREIGN KEY (league_id)
      REFERENCES league (id) ON DELETE CASCADE,
  CONSTRAINT scoring_period_league_id_period_id_key UNIQUE (league_id , period_id )
);

CREATE INDEX scoring_period_league_ix ON scoring_period (league_id );

--
-- Players
--

-- Info

CREATE TABLE player
(
  id integer NOT NULL,
  league_id integer NOT NULL,
  fantasy_id integer NOT NULL,
  name text NOT NULL,
  eligibility text NOT NULL,
  CONSTRAINT player_pkey PRIMARY KEY (id ),
  CONSTRAINT player_league_id_fkey FOREIGN KEY (league_id)
      REFERENCES league (id) ON DELETE CASCADE,
  CONSTRAINT player_league_id_fantasy_id_key UNIQUE (league_id , fantasy_id )
);

CREATE INDEX player_league_ix ON player (league_id );

-- Results

CREATE TABLE result
(
  id integer NOT NULL,
  player_id integer NOT NULL,
  scoringperiod_id integer,
  score numeric,
  CONSTRAINT result_pkey PRIMARY KEY (id ),
  CONSTRAINT result_player_id_fkey FOREIGN KEY (player_id)
      REFERENCES player (id) ON DELETE CASCADE
);

CREATE INDEX result_player_ix ON result (player_id );

CREATE INDEX result_scoringperiod_ix ON result (scoringperiod_id );

-- Stats

CREATE TABLE stat
(
  id integer NOT NULL,
  result_id integer NOT NULL,
  category_id integer,
  value numeric NOT NULL,
  CONSTRAINT stat_pkey PRIMARY KEY (id ),
  CONSTRAINT stat_category_id_fkey FOREIGN KEY (category_id)
      REFERENCES category (id) ON DELETE CASCADE,
  CONSTRAINT stat_result_id_fkey FOREIGN KEY (result_id)
      REFERENCES result (id) ON DELETE CASCADE
);

CREATE INDEX stat_category_ix ON stat (category_id );

CREATE INDEX stat_result_ix ON stat (result_id );

--
-- Teams
--

-- Info

CREATE TABLE team
(
  id integer NOT NULL,
  league_id integer NOT NULL,
  fantasy_team_id smallint NOT NULL,
  name text NOT NULL,
  owner text NOT NULL,
  CONSTRAINT team_pkey PRIMARY KEY (id ),
  CONSTRAINT team_league_fk FOREIGN KEY (league_id)
      REFERENCES league (id) ON DELETE CASCADE,
  CONSTRAINT team_league_id_fantasy_team_id_key UNIQUE (league_id , fantasy_team_id )
);

CREATE INDEX team_league_ix ON team (league_id );

-- Lineups

CREATE TABLE lineup
(
  id integer NOT NULL,
  team_id integer NOT NULL,
  scoringperiod_id smallint,
  score numeric,
  CONSTRAINT lineup_pkey PRIMARY KEY (id ),
  CONSTRAINT lineup_team_id_fkey FOREIGN KEY (team_id)
      REFERENCES team (id) ON DELETE CASCADE,
  CONSTRAINT lineup_team_id_scoringperiod_id_key UNIQUE (team_id , scoringperiod_id )
);

CREATE INDEX lineup_team_ix ON lineup (team_id );

-- Slots

CREATE TABLE slot
(
  id integer NOT NULL,
  lineup_id integer NOT NULL,
  player_id integer,
  "position" character varying(10) NOT NULL,
  eligible boolean NOT NULL,
  CONSTRAINT slot_pkey PRIMARY KEY (id ),
  CONSTRAINT slot_lineup_id_fkey FOREIGN KEY (lineup_id)
      REFERENCES lineup (id) ON DELETE CASCADE
);

CREATE INDEX slot_lineup_ix ON slot (lineup_id );
  
--
-- Weeks
--

-- Info

CREATE TABLE week
(
  id integer NOT NULL,
  league_id integer NOT NULL,
  startperiod_id integer,
  endperiod_id integer,
  CONSTRAINT week_pkey PRIMARY KEY (id ),
  CONSTRAINT week_league_id_fkey FOREIGN KEY (league_id)
      REFERENCES league (id) ON DELETE CASCADE
);

CREATE INDEX week_league_ix ON week (league_id );

-- Matchups

CREATE TABLE matchup
(
  id integer NOT NULL,
  week_id integer NOT NULL,
  home_team_id smallint NOT NULL,
  away_team_id smallint NOT NULL,
  CONSTRAINT matchup_pkey PRIMARY KEY (id ),
  CONSTRAINT matchup_week_id_fkey FOREIGN KEY (week_id)
      REFERENCES week (id) ON DELETE CASCADE,
  CONSTRAINT matchup_week_id_home_team_id_away_team_id_key UNIQUE (week_id , home_team_id , away_team_id )
);

CREATE INDEX matchup_week_ix ON matchup (week_id );

--
-- Projections
--

CREATE TABLE projection
(
  id integer NOT NULL,
  player_id integer NOT NULL,
  category_id integer NOT NULL,
  value numeric NOT NULL,
  CONSTRAINT projection_pkey PRIMARY KEY (id ),
  CONSTRAINT projection_category_id_fkey FOREIGN KEY (category_id)
      REFERENCES category (id) ON DELETE CASCADE,
  CONSTRAINT projection_player_id_fkey FOREIGN KEY (player_id)
      REFERENCES player (id) ON DELETE CASCADE,
  CONSTRAINT projection_player_id_category_id_key UNIQUE (player_id , category_id )
);

CREATE INDEX projection_category_ix ON projection (category_id );

CREATE INDEX projection_player_ix ON projection (player_id );
