package software.cneuro.neuroger.content;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import software.cneuro.neurogertheme.chronometer.ChronometerWithMilliseconds;

/**
 * Created by klaudia on 7/31/2015.
 * <p/>
 * CalendarHelper is a class with static methods for handling work with dates.
 */
public class CalendarHelper {
    public static int NO_VALUE = Integer.MAX_VALUE;
    public static int CALENDAR_MINUS_YEARS = 50;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy", Locale.getDefault());
    public static SimpleDateFormat dateFormatYearFirst = new SimpleDateFormat(
            "yyyy/MM/dd", Locale.getDefault());
    public static SimpleDateFormat chronometerTimeFormat = new SimpleDateFormat(
            "HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateFormatCI = new SimpleDateFormat(
            "yyMMdd", Locale.getDefault());

    /**
     * Using the text as a string date match it to the format.
     *
     * @param text   date
     * @param format format of the date
     * @return the calendar
     */
    public static Calendar parseCalendar(CharSequence text,
                                         SimpleDateFormat format) {
        try {
            Date parsedDate = format.parse(text.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            return calendar;
        } catch (java.text.ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    /**
     * Using the text as the string date match it to the format.
     *
     * @param date
     * @return the age
     */
    public static int getAge(CharSequence date) {
        return getAge(parseCalendar(date, dateFormatYearFirst).getTime());
    }

    /**
     * Get the age using the birth date.
     *
     * @param birthDate the date to use as reference.
     * @return the age.
     */
    public static int getAge(Date birthDate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);

        GregorianCalendar cal = new GregorianCalendar();

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(birth.get(Calendar.YEAR), birth.get(Calendar.MONTH), birth.get(Calendar.DAY_OF_MONTH));
        int a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    /**
     * Gets the init date to begin with.
     *
     * @return today's date - CalendarHelper.CALENDAR_MINUS_YEARS.
     */
    public static Calendar getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - CalendarHelper.CALENDAR_MINUS_YEARS;
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }

    /**
     * Get the duration in minutes + seconds.
     * Expects the format of the chronometer to be as {@link CalendarHelper#chronometerTimeFormat}
     *
     * @param chronometer The chronometer from witch ger the time.
     * @return minutes + "." + seconds.
     */
    public static String getDurationInSeconds(ChronometerWithMilliseconds chronometer) {
        String value = chronometer.getText().toString();
        String[] parts = value.split(":");

        // Wrong format, no value for you.
        if (parts.length < 2 || parts.length > 3)
            return "";

        int milliseconds = 0, seconds = 0, minutes = 0;

        if (parts.length == 2) {
            milliseconds = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[0]);
        } else if (parts.length == 3) {
            milliseconds = Integer.parseInt(parts[2]);
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        }

        return (seconds + (minutes * 60)) + "." + milliseconds;
    }

    public static Date getInitRangeDate(int years) {
        Calendar today = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, today.get(Calendar.YEAR) - years);

        return calendar.getTime();
    }

    public static Date getEndRangeDate(int years) {
        Calendar today = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.YEAR, today.get(Calendar.YEAR) - years);

        return calendar.getTime();
    }

    public static String getDateToSaveSQLite(Date date) {
        return dateFormatYearFirst.format(date);
    }

    public static String getDateToShow(Date date) {
        return dateFormat.format(date);
    }

    public static String getDateToShow(String date) {
        return dateFormat.format(parseCalendar(date, dateFormatYearFirst).getTime());
    }

    public static String getDateToCompareWithCi(Date date) {
        return dateFormatCI.format(date);
    }

    public static String getBirthDateFromCIToSaveSQLite(String ci) {
        if (ci.length() != 11) return "";

        String yearStr = ci.substring(0, 2);
        String monthStr = ci.substring(2, 4);
        String dayStr = ci.substring(4, 6);

        int century;
        try {
            century = Integer.parseInt(String.valueOf(ci.charAt(6)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (century >= 6 && century <= 8)
            yearStr = "20" + yearStr;
        else if (century >= 0 && century <= 5) {
            yearStr = "19" + yearStr;
        } else
            yearStr = "18" + yearStr;

        return yearStr + "/" + monthStr + "/" + dayStr;
    }
}
