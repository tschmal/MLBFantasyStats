package com.schmal;

import com.schmal.domain.*;
import com.schmal.resource.LeagueResource;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.Service;

public class MLBFantasyStatsService extends Service<MLBFantasyStatsConfig>
{
    private final HibernateBundle<MLBFantasyStatsConfig> hibernateBundle =
        new HibernateBundle<MLBFantasyStatsConfig>(
            League.class, Team.class, Result.class, Stat.class, Week.class, Matchup.class, Scoring.class,
            Player.class, Position.class, Projection.class, Category.class)
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
        bootstrap.addBundle(new AssetsBundle("/web", "/", "index.html"));
    }

    @Override
    public void run(MLBFantasyStatsConfig config, Environment environment)
    {
        environment.addResource(new LeagueResource(hibernateBundle));
    }
}