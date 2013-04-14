package com.schmal;

import com.schmal.domain.League;
import com.schmal.domain.ScoringCategory;
import com.schmal.resource.RetrieveResource;
import com.schmal.service.ESPNScraper;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.Service;

public class MLBFantasyStatsService extends Service<MLBFantasyStatsConfig>
{
    private final HibernateBundle<MLBFantasyStatsConfig> hibernateBundle =
        new HibernateBundle<MLBFantasyStatsConfig>(
            League.class, ScoringCategory.class)
        {
            @Override
            public DatabaseConfiguration getDatabaseConfiguration(MLBFantasyStatsConfig config)
            {
                return config.getDatabase();
            }
        };

    public static void main(String[] args) throws Exception
    {
        new MLBFantasyStatsService().run(args);
    }

    @Override
    public void initialize(Bootstrap<MLBFantasyStatsConfig> bootstrap)
    {
        bootstrap.setName("fantasy-stats");
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(MLBFantasyStatsConfig config, Environment environment)
    {
        environment.addResource(new RetrieveResource(hibernateBundle, config.getLeagueURL()));
    }
}