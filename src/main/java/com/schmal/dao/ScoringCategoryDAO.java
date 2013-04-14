package com.schmal.dao;

import com.schmal.domain.ScoringCategory;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class ScoringCategoryDAO extends AbstractDAO<ScoringCategory>
{
    public ScoringCategoryDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<ScoringCategory> save(List<ScoringCategory> categories)
    {
        for (ScoringCategory category : categories)
        {
            persist(category);
        }

        return categories;
    }
}