package com.schmal.resource;

import com.schmal.domain.Category;
import com.schmal.service.CategoryService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CategoryResource
{
    private final CategoryService service;

    public CategoryResource(HibernateBundle hibernateBundle)
    {
        service = new CategoryService(hibernateBundle);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Category> createCategories(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.createCategories(leagueID);
    }
}