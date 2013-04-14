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

Ideas for uses
--------------

What the hell do I eventually want to do with this? Well, here are some brainstormed ideas:

* Basic statistics: pitching points per week, hitting points per week, by position, by team, etc... all with some pretty D3 visualizations!
* Bad Luck Brian: Determine which teams have bad luck against their opponents. Using simple points against isn't really a sufficient indicator since, if you're a good team, you'll naturally have fewer points against since you don't play yourself. However, we can take the average per week for each team, and detect by how much teams exceed that average against certain teams. The guy whose opponents typically exceed their average is the Bad Luck Brian of the league.
* Where do I need help? Again, the differential between one of your positions and the rest of the league is a good indicator of where you need some aid. Might able to later expand this into figuring out who has excess at a certain position to perhaps facilitate trade ideas.

Temporary DDL for the PostgreSQL database
-----------------------------------------

Until I can get the dropwizard-migrations and Liquibase behavior working correctly, I'm just going to put all the DDL here:

    create table league (
        espn_id integer not null,
        year smallint not null,
        name text not null,
        url text not null,
        primary key (espn_id, year)
    );

    create table scoring (
        espn_id integer not null,
        year smallint not null,
        category varchar(5) not null,
        category_type char(1) not null,
        points numeric not null,
        primary key (espn_id, year, category, category_type)
    );
