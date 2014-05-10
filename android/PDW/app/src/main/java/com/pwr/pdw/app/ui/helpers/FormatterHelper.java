package com.pwr.pdw.app.ui.helpers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created
 */
public class FormatterHelper {
    public static String FORMAT_DATE_ISO = "yyyy-MM-dd HH:mm:ss";

    public static Date  fromISODateString(String isoDateString)  {
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_ISO, Locale.getDefault());
        try {
            return df.parse(isoDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getFormattedDate(Calendar cal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(cal.getTime());
    }
}
