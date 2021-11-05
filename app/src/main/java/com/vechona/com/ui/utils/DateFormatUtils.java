package com.vechona.com.ui.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    public static String format(String dateFormat) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputformat = new SimpleDateFormat("dd MMM, hh:mm aa");
        String date = dateFormat.split("T")[0] + " " + dateFormat.split("T")[1].split("\\.")[0];
        String output = null;
        try {
            Date date1 = df.parse(date);
            output = outputformat.format(date1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getDate(String dateFormat) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputformat = new SimpleDateFormat("dd MMM yyyy");
        String date = dateFormat.split("T")[0] + " " + dateFormat.split("T")[1].split("\\.")[0];
        String output = null;
        try {
            Date date1 = df.parse(date);
            output = outputformat.format(date1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getDateWithYear(String dateFormat) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputformat = new SimpleDateFormat("dd MMMM yyyy, hh:mm aa");
        String date = dateFormat.split("T")[0] + " " + dateFormat.split("T")[1].split("\\.")[0];
        String output = null;
        try {
            Date date1 = df.parse(date);
            output = outputformat.format(date1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }
}