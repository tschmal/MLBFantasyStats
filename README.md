MLBFantasyStats
===============

Stats parser for an ESPN Fantasy Baseball League.

Current usage
-------------

`gradle oneJar` - builds the singular jar file with everything we need.

`java -jar build/libs/MLBFantasyStats-standalone.jar server stats.yml` - starts the Jetty web server and resources.

`gradle run` - Both builds the single jar and runs it. WARNING: On Windows it spawns two java processes. You may have to kill them to shut it completely down.

Services
--------

`GET /retrieve/todo` - Sends back an array of match-up titles that need scraping.

`GET /retrieve/scoring` - Get the list of scoring categories and how much each is worth.

`POST /retrieve/new` - Save the league info and scoring category information to the DB. Returns JSON of a FullLeague object.

Temporary DDL for the PostgreSQL database
-----------------------------------------

Until I can get the dropwizard-migrations and Liquibase behavior working correctly, I'm just going to put all the DDL here:

    drop table league;
    create table league (
        id integer not null,
        espn_id integer not null,
        name text not null,
        year smallint not null,
        url text not null,
        primary key (id),
        unique (espn_id, year)
    );

    drop table scoring;
    create table scoring (
        league_id integer not null,
        category varchar(5) not null,
        category_type char(1) not null,
        points numeric not null,
        primary key (league_id, category, category_type)
    );

    drop table team;
    create table team (
        id integer not null,
        league_id integer not null,
        espn_team_id smallint not null,
        year smallint not null,
        owner text not null,
        name text not null,
        primary key (id),
        unique (league_id, espn_team_id)
    );
