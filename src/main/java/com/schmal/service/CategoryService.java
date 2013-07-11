package com.schmal.service;

import com.schmal.dao.CategoryDAO;
import com.schmal.domain.Category;
import com.schmal.domain.League;
import com.schmal.util.FantasyURLBuilder;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class CategoryService
{
    private final CategoryDAO dao;

    private final LeagueService leagueService;

    public CategoryService(HibernateBundle hibernateBundle)
    {
        dao = new CategoryDAO(hibernateBundle);
        leagueService = new LeagueService(hibernateBundle);
    }

    public List<Category> createCategories(long leagueID) throws Exception
    {
        List<Category> categories = new ArrayList<Category>();

        League league = leagueService.getLeagueByID(leagueID);
        URL settingsURL = FantasyURLBuilder.getSettingsURL(league);

        Pattern categoryPattern = Pattern.compile("\\((.*?)\\)");

        Document settingsDoc = Jsoup.connect(settingsURL.toString()).get();

        Elements categoryTypes = settingsDoc.select(".categoryName");
        for (Element categoryType : categoryTypes)
        {
            char type = categoryType.ownText().charAt(0);

            Elements statNames = categoryType.parent().select("td.statName");
            for (Element statName : statNames)
            {
                Matcher matcher = categoryPattern.matcher(statName.ownText());
                matcher.find();
                String categoryName = matcher.group(1);

                Element statPoints = statName.nextElementSibling();
                float categoryPoints = Float.parseFloat(statPoints.ownText());

                categories.add(new Category(league, categoryName, type, categoryPoints));
            }
        }

        return dao.save(league, categories);
    }
}