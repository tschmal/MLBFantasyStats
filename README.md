MLBFantasyStats
===============

Stats parser for an ESPN Fantasy Baseball League.

Current usage
-------------

`gradle oneJar` - builds the singular jar file with everything we need.

`java -jar build/libs/MLBFantasyStats-standalone.jar server stats.yml` - starts the Jetty web server and resources.

Services
--------

`GET /retrieve/todo` - Sends back an array of match-up titles that need scraping.