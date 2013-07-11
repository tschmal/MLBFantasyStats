package com.schmal.dao;

import com.schmal.domain.Category;
import com.schmal.domain.League;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.util.List;
import org.hibernate.Query;

public class CategoryDAO extends AbstractDAO<Category>
{
    private final LeagueDAO leagueDAO;

    private final HibernateBundle hibernateBundle;

    public CategoryDAO(HibernateBundle hibernateBundle)
    {
        super(hibernateBundle.getSessionFactory());

        leagueDAO = new LeagueDAO(hibernateBundle.getSessionFactory());
        this.hibernateBundle = hibernateBundle;
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

    public List<Category> getAllCategories()
    {
        return list(namedQuery("allCategories"));
    }

    public Category getSingleCategory(League league, String category, char categoryType)
    {
        Query query = namedQuery("singleCategory")
            .setParameter("league", league)
            .setParameter("category", category)
            .setParameter("categoryType", categoryType);

        List<Category> categories = query.list();

        return categories.get(0);
    }
}