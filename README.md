MLBFantasyStats
===============

Stats parser for an ESPN Fantasy Baseball League.

Current usage
-------------

`gradle jar` - builds the source into a jar file and runs PMD.

`gradle run` - builds the jar file, runs PMD, then runs the main class (effectively starting the application).

Services
--------

`GET /retrieve/todo` - Sends back an array of match-up titles that need scraping.

`GET /retrieve/scoring` - Get the list of scoring categories and how much each is worth.

`POST /retrieve/new` - Save the league info and scoring category information to the DB. Returns JSON of a FullLeague object.

`POST /retrieve/matchups/all` - Save all matchups that were completed or are in progress to the database.

Ideas for uses
--------------

What the hell do I eventually want to do with this? Well, here are some brainstormed ideas:

* Basic statistics: pitching points per week, hitting points per week, by position, by team, etc... all with some pretty D3 visualizations!
* Bad Luck Brian: Determine which teams have bad luck against their opponents. Using simple points against isn't really a sufficient indicator since, if you're a good team, you'll naturally have fewer points against since you don't play yourself. However, we can take the average per week for each team, and detect by how much teams exceed that average against certain teams. The guy whose opponents typically exceed their average is the Bad Luck Brian of the league.
* Where do I need help? Again, the differential between one of your positions and the rest of the league is a good indicator of where you need some aid. Might able to later expand this into figuring out who has excess at a certain position to perhaps facilitate trade ideas.

Temporary DDL for the PostgreSQL database
-----------------------------------------

Until I can get the dropwizard-migrations and Liquibase behavior working correctly, I'm just going to put all the DDL here:

    CREATE TABLE league
    (
      espn_id integer NOT NULL,
      year smallint NOT NULL,
      name text NOT NULL,
      url text NOT NULL,
      PRIMARY KEY (espn_id , year )
    );

    CREATE TABLE scoring
    (
      espn_id integer NOT NULL,
      year smallint NOT NULL,
      category character varying(5) NOT NULL,
      category_type character(1) NOT NULL,
      points numeric NOT NULL,
      PRIMARY KEY (espn_id , year , category , category_type )
    );

    CREATE TABLE team
    (
      espn_id integer NOT NULL,
      year smallint NOT NULL,
      espn_team_id smallint NOT NULL,
      owner text NOT NULL,
      name text NOT NULL,
      PRIMARY KEY (espn_id , year , espn_team_id )
    );
    
    CREATE TABLE matchup
    (
      espn_id integer NOT NULL,
      year smallint NOT NULL,
      home_espn_team_id smallint NOT NULL,
      away_espn_team_id smallint NOT NULL,
      start_date date NOT NULL,
      end_date date NOT NULL,
      PRIMARY KEY (espn_id , year , home_espn_team_id , start_date )
    );