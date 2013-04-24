package com.schmal.dao;

import com.schmal.domain.Category;
import com.schmal.domain.League;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CategoryDAO extends AbstractDAO<Category>
{
    private final LeagueDAO leagueDAO;

    public CategoryDAO(SessionFactory factory)
    {
        super(factory);

        leagueDAO = new LeagueDAO(factory);
    }

    public List<Category> save(League league, List<Category> categories)
    {
        // Delete everything.
        league.getCategories().clear();
        leagueDAO.save(league);
        currentSession().flush();

        // Insert all the new categories.
        league.getCategories().addAll(categories);
        leagueDAO.save(league);

        return league.getCategories();
    }
}