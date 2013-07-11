package com.schmal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil
{
    /**
     * Get a Date object for the ESPN date format (e.g. "MAY 17").
     */
    public static Date getDate(String monthDay, int year) throws Exception
    {
        DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");

        String[] dateParts = monthDay.split(" ");
        String month = dateParts[0];
        String day = dateParts[1];

        String dateString = year + "-" + month + "-" + day;

        return formatter.parse(dateString);
    }

    public static Date[] getDateRange(String startMonthDay, String endMonthDay, int year) throws Exception
    {
        Date[] range = new Date[2];
        range[0] = getDate(startMonthDay, year);

        String[] endParts = endMonthDay.split(" ");

        if (endParts.length > 1)
        {
            range[1] = getDate(endMonthDay, year);
        }
        else
        {
            String[] startParts = startMonthDay.split(" ");
            range[1] = getDate(startParts[0] + " " + endMonthDay, year);
        }

        return range;
    }
}