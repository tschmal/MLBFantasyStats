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