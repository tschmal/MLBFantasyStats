MLBFantasyStats
===============

Stats parser for an ESPN Fantasy Baseball League.

Current usage
-------------

`gradle jar` - builds the source into a jar file and runs PMD.

`gradle run` - builds the jar file, runs PMD, then runs the main class (effectively starting the application).

Services
--------

`GET /services/league/fantasyID/{fantasyID}`
Get all leagues with a particular fantasy ID (thus, all years of that league).

`GET /services/league/service/{fantasyService}`
Get all leagues for a particular fantasy service.

`POST /services/league`
Retrieve a league and save its basic information to the database. Requires URL parameters; see LeagueResource.

`POST /services/team`
Retrieve basic team information for a league and save it to the database. Requires a URL parameter; see TeamResource.

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
      id integer NOT NULL,
      fantasy_id integer NOT NULL,
      year smallint NOT NULL,
      name text NOT NULL,
      service text NOT NULL,
      PRIMARY KEY (id),
      UNIQUE (fantasy_id, year, service)
    )

    CREATE TABLE team
    (
      id integer NOT NULL,
      league_id integer NOT NULL,
      fantasy_team_id smallint NOT NULL,
      name text NOT NULL,
      owner text NOT NULL,
      PRIMARY KEY (id),
      UNIQUE (league_id, fantasy_team_id)
    )