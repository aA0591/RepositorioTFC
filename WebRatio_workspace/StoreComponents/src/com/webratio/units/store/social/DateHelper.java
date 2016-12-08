package com.webratio.units.store.social;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateHelper {

    public static final String WEB_RATIO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TWITTER_DATE_TIME_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    public static final String FACEBOOK_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
    public static final String LINKEDIN_DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static final String GOOGLE_CALENDAR_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    public static GregorianCalendar getCalendarFromString(String stringDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            Date date = sdf.parse(stringDate);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateFromRFC3339String(String dateString) throws ParseException {
        // remove colon from string
        String beforeColon = dateString.substring(0, dateString.lastIndexOf(":"));
        String afterColon = dateString.substring(dateString.lastIndexOf(":") + 1);

        String formatString = beforeColon + afterColon;

        SimpleDateFormat sdf = new SimpleDateFormat(GOOGLE_CALENDAR_DATE_TIME_FORMAT);
        return sdf.parse(formatString);

    }

}
