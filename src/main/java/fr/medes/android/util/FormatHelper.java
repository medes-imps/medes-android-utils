package fr.medes.android.util;

import android.content.Context;
import android.location.Location;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.medes.android.R;

/**
 * Contains static methods to format values to/from string representation or to human readable string.
 *
 * @author MEDES-IMPS
 */
public class FormatHelper {

    private static final DecimalFormatSymbols DFS = new DecimalFormatSymbols();

    static {
        DFS.setDecimalSeparator('.');
    }

    private static DateFormat DATE_FORMAT = DateFormat.getDateInstance();
    private static DateFormat DATI_FORMAT = DateFormat.getDateTimeInstance();
    private static DateFormat TIME_FORMAT = DateFormat.getTimeInstance();

    private static final SimpleDateFormat CONSTRAINT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy",
            Locale.getDefault());
    private static final SimpleDateFormat CONSTRAINT_DATI_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm",
            Locale.getDefault());
    private static final SimpleDateFormat CONSTRAINT_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /**
     * Used to update the application {@link DateFormat}s when changing device configuration (ie: Locale).
     */
    public static void updateFormats() {
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
        DATI_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.FULL);
    }

    /**
     * Convert a {@link Date} representing a date to a string representation.
     *
     * @param time a {@link Date} to format as a date.
     * @return the formatted string.
     */
    public static String displayDate(Date time) {
        if (time == null) {
            return null;
        }
        return DATE_FORMAT.format(time);
    }

    /**
     * Convert a {@link Date} representing a date & time to a string representation.
     *
     * @param time a {@link Date} to format as a date & time.
     * @return the formatted string.
     */
    public static String displayDateTime(Date time) {
        if (time == null) {
            return null;
        }
        return DATI_FORMAT.format(time);
    }

    /**
     * Convert a {@link Date} representing a time to a string representation.
     *
     * @param time a {@link Date} to format as a time.
     * @return The formatted string.
     */
    public static String displayTime(Date time) {
        if (time == null) {
            return null;
        }
        return TIME_FORMAT.format(time);
    }

    /**
     * Convert a {@link Location} to a human readable string representation.
     *
     * @param context  The current context of the application.
     * @param location The location to represent.
     * @return The formatted string.
     */
    public static String displayLocation(Context context, Location location) {
        return context.getString(R.string.aml__location_format, location.getLatitude(), location.getLongitude());
    }

    /**
     * Parses the specified string as a signed decimal long value.
     *
     * @param str Parses the specified string as a signed decimal long value.
     * @return a {@link Long} instance containing the long value represented by string or {@code null}.
     */
    public static Long toLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Try to convert an object to a Long.
     *
     * @param value The object to convert.
     * @return a Long instance or {@code null}.
     */
    public static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return toLong((String) value);
        }
        return null;
    }

    /**
     * Parses the specified string as a {@link Date}. The string must be a number of milliseconds value representing a
     * date.
     *
     * @param str Parses the specified string as a signed decimal long value.
     * @return a {@link Date} instance containing the date value represented by string or {@code null}.
     */
    public static Date toDate(String str) {
        Long time = toLong(str);
        if (time != null) {
            return new Date(time);
        }
        return null;
    }

    /**
     * Try to convert an object to a Date.
     *
     * @param value The object to convert.
     * @return a Date instance or {@code null}
     */
    public static Date toDate(Object value) {
        if (value instanceof Date) {
            return (Date) value;
        } else if (value instanceof Number) {
            long longValue = ((Number) value).longValue();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(longValue);
            return cal.getTime();
        } else if (value instanceof String) {
            return toDate((String) value);
        }
        return null;
    }

    /**
     * Parses the specified string as a float value.
     *
     * @param str the string representation of a float value.
     * @return a {@link Float} instance containing the float value represented by string or {@code null}.
     */
    public static Float toFloat(String str) {
        try {
            return Float.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Try to convert an object to a Float.
     *
     * @param value The object to convert.
     * @return a Float instance or {@code null}.
     */
    public static Float toFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            return toFloat((String) value);
        }
        return null;
    }

    /**
     * Parses the specified string as a signed decimal integer value.
     *
     * @param str the string representation of an integer value.
     * @return an {@link Integer} instance containing the integer value represented by the string or {@code null}.
     */
    public static Integer toInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Try to convert an object to an Integer.
     *
     * @param value The object to convert.
     * @return an Integer instance or {@code null}.
     */
    public static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return toInteger((String) value);
        }
        return null;
    }

    /**
     * Parses the specified string as a boolean value.
     *
     * @param str the string representation of a boolean value.
     * @return {@code null} if the string is {@code null}, {@link Boolean#TRUE} if string is equal to "true" using case
     * insensitive comparison, {@link Boolean#FALSE} otherwise.
     */
    public static Boolean toBoolean(String str) {
        if (str == null) {
            return null;
        } else {
            return Boolean.valueOf(str);
        }
    }


    /**
     * Try to convert an object to a Boolean.
     *
     * @param value The object to convert.
     * @return a Boolean instance or {@code null}.
     */
    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        return null;
    }

    /**
     * Parses the specified string as a double value.
     *
     * @param str the string representation of a double value.
     * @return a {@link Double} instance containing the double value represented by string or {@code null}.
     */
    public static Double toDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Try to convert an object to a Double.
     *
     * @param value The object to convert.
     * @return a Double instance or {@code null}.
     */
    public static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return toDouble((String) value);
        }
        return null;
    }

    /**
     * Try to convert an object to a String.
     *
     * @param value The object to convert.
     * @return a String instance or {@code null}.
     */
    public static String toString(Object value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Try to convert an Object to a Map.
     *
     * @param value The object to convert.
     * @param <K>   The Key parameter type.
     * @param <V>   The value parameter type.
     * @return a Map instance or {@code null}.
     */
    public static <K, V> Map<K, V> toMap(Object value) {
        if (value instanceof Map) {
            return (Map) value;
        }
        return null;
    }

    /**
     * Try to convert an Object to a List.
     *
     * @param value The object to convert.
     * @param <T>   The value parameter type.
     * @return a List instance or {@code null}.
     */
    public static <T> List<T> toList(Object value) {
        if (value instanceof List) {
            return (List) value;
        }
        return null;
    }

    /**
     * Parses a date from the specified string using the format {@code dd/MM/yyyy}.
     *
     * @param str the string to parse.
     * @return the {@link Date} resulting from the parsing or {@code null}.
     */
    public static Date readDate(String str) {
        try {
            return CONSTRAINT_DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parses a date from the specified string using the format {@code dd/MM/yyyy HH:mm}.
     *
     * @param str the string to parse.
     * @return the {@link Date} resulting from the parsing or {@code null}.
     */
    public static Date readDateTime(String str) {
        try {
            return CONSTRAINT_DATI_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parses a date from the specified string using the format {@code HH:mm}.
     *
     * @param str The string to parse.
     * @return The {@link Date} resulting from the parsing or {@code null}.
     */
    public static Date readTime(String str) {
        try {
            return CONSTRAINT_TIME_FORMAT.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parses a {@link Location} from the specified string which must follow the format {@code "latitude;longitude"}.
     *
     * @param str The string to parse.
     * @return The {@link Location} resulting from the parsing or {@code null} if the string could not be parsed.
     */
    public static Location readLocation(String str) {
        if (str != null) {
            String[] values = str.split(";");
            if (values.length > 1) {
                Double latitude = FormatHelper.toDouble(values[0]);
                Double longitude = FormatHelper.toDouble(values[1]);
                if (latitude != null && longitude != null) {
                    Location location = new Location("gps");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    return location;
                }
            }
        }
        return null;
    }

}
