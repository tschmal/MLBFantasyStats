package com.schmal.util;

import java.net.URL;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkUtil
{
    public static String getLinkURL(Elements links, String text)
    {
        for (Element link : links)
        {
            if (text.equals(link.ownText()))
            {
                return link.attr("href");
            }
        }

        return "/FUCK";
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
}