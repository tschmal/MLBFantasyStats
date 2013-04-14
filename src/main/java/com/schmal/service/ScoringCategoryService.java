package com.schmal.service;

import com.schmal.dao.ScoringCategoryDAO;
import com.schmal.domain.LeagueKey;
import com.schmal.domain.ScoringCategory;
import com.schmal.domain.ScoringCategoryKey;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScoringCategoryService
{
    private final ScoringCategoryDAO categoryDAO;

    public ScoringCategoryService(HibernateBundle hibernateBundle)
    {
        categoryDAO = new ScoringCategoryDAO(hibernateBundle.getSessionFactory());
    }

    public List<ScoringCategory> saveScoringCategories(
        LeagueKey leagueKey,
        Document leagueDoc,
        URL leagueURL) throws Exception
    {
        List<ScoringCategory> categories = new ArrayList<ScoringCategory>();
        Pattern categoryPattern = Pattern.compile("\\((.*?)\\)");

        Elements leagueMenuLinks = leagueDoc.select("ul#games-subnav-links > li > a");
        Document settingsDoc = Jsoup.connect(LinkUtil.getLinkURL(leagueURL, leagueMenuLinks, "Settings")).get();

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

                ScoringCategory newCategory = new ScoringCategory(
                    new ScoringCategoryKey(leagueKey, categoryName, type), categoryPoints);
                categories.add(newCategory);
            }
        }

        return categoryDAO.save(categories);
    }
}