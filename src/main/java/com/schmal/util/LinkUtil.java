package com.schmal.util;

import java.net.URL;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkUtil
{
    public static String getLinkURL(URL leagueURL, Elements links, String text)
    {
        for (Element link : links)
        {
            if (text.equals(link.ownText()))
            {
                return getDomain(leagueURL) + link.attr("href");
            }
        }

        return "NOPE";
    }

    public static String getLinkParameter(URL url, String parameter)
    {
        String value = null;

        for (String param : url.getQuery().split("&"))
        {
            String name = param.split("=")[0];
            if (parameter.equals(name))
            {
                value = param.split("=")[1];
                break;
            }
        }

        return value;
    }

    public static String getDomain(URL url)
    {
        return url.getProtocol() + "://" + url.getHost();
    }
}