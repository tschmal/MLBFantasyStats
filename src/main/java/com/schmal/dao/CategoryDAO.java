package com.schmal.dao;

import com.schmal.domain.Category;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CategoryDAO extends AbstractDAO<Category>
{
    public CategoryDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<Category> save(List<Category> categories)
    {
        for (Category category : categories)
        {
            persist(category);
        }

        return categories;
    }
}