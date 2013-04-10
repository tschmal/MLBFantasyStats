package com.schmal;

import com.schmal.resource.RetrieveResource;
import com.schmal.service.ESPNScraper;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.Service;

public class MLBFantasyStatsService extends Service<MLBFantasyStatsConfig>
{
    public static void main(String[] args) throws Exception
    {
        new MLBFantasyStatsService().run(args);
    }

    @Override
    public void initialize(Bootstrap<MLBFantasyStatsConfig> bootstrap)
    {
        bootstrap.setName("fantasy-stats");
    }

    @Override
    public void run(MLBFantasyStatsConfig config, Environment environment)
    {
        environment.addResource(new RetrieveResource(config.getLeagueURL()));
    }
}