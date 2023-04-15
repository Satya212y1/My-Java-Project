package com.zenscale.zencrm_2.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonFunctions {


    public String checkNullString(String s) {

        if (s == null) {
            return "";
        } else {
            return s;
        }
    }




    public Timestamp getTimestampFromDateString(String format, String d) {

        Date date = getFromattedDate(format, d);
        Timestamp timestamp = getTimeStampFromDate(date);
        return timestamp;
    }




    public Date getCurrentDate(String format) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.format(date);
        return date;
    }




    public boolean isToDateGreaterThanFromDate(String date1, String date2, String format) {

        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        Date d1 = null, d2 = null;
        try {
            d1 = sdformat.parse(date1);
            d2 = sdformat.parse(date2);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        if (d1.compareTo(d2) < 0) { // Date 1 occurs before Date 2
            return true;
        }
        return false;
    }




    public int checkNullInteger(Integer i) {

        if (i == null) {
            return 0;
        } else {
            return i;
        }
    }




    public boolean isDateFormatted(String format, String value) {

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            //ex.printStackTrace();
        }
        return date != null;
    }




    public Date getDateFromMillis(String format, long millis) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(millis);
        dateFormat.format(date);
        return date;
    }




    public Date getFromattedDate(String format, String d) {

        //System.out.println("format = " + format + ", d = " + d);
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(d);
            //System.out.println("sdf.format(date) = " + sdf.format(date) + " / " + d);
            if (!d.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }




    public Timestamp getTimeStampFromDate(Date d) {

        // getting the timestamp object
        Timestamp ts = new Timestamp(d.getTime());
        return ts;
    }




    public String getTimeDifference(String date1, String date2, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String duration = "";
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            // Calculate time difference in milliseconds
            long difference_In_Time = d2.getTime() - d1.getTime();

            // Calculate time difference in seconds, minutes, hours, years, and days
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            if (difference_In_Years > 0) {
                duration += difference_In_Years + " yr ";
            }
            if (difference_In_Days > 0) {
                duration += difference_In_Days + " day ";
            }
            if (difference_In_Hours > 0) {
                duration += difference_In_Hours + " hr ";
            }
            if (difference_In_Minutes > 0) {
                duration += difference_In_Minutes + " min ";
            }
            if (difference_In_Seconds > 0) {
                duration += difference_In_Seconds + " sec ";
            }
            return duration.trim();
        } catch (ParseException e) {
            return "";
        }
    }

    public String convertStreamToString(InputStream is) {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        String resultString = "";
        byte[] buffer = new byte[1024];
        int length = 0;
        while (true) {
            try {
                if (!((length = is.read(buffer)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        try {
            resultString = result.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return resultString;
    }





    public RequestBody createRequestBody(String data) {

        RequestBody requestBody = null;
        try {
            requestBody = null;
            requestBody = RequestBody.create(MediaType.parse("text/plain"), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }


    public List<String> getDatesBetweenBetweenTwoDates(Date startDate, Date endDate) {

        List<String> datesInRange = new ArrayList<>();
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);
        DateFormat dateFormat = new SimpleDateFormat(CommonStrings.dateformat_hyphen_yyyyMMdd);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(dateFormat.format(result));
            calendar.add(Calendar.DATE, 1);
        }
        datesInRange.add(dateFormat.format(endDate));

        return datesInRange;
    }




    private Calendar getCalendarWithoutTime(Date date) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }




}
