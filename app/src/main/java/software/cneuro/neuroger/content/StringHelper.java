package software.cneuro.neuroger.content;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by Klaudia on 7/31/2015.
 * A helpful class to aid in the correct format of the text.
 */
public class StringHelper {

    /**
     * Save in a json string the list
     *
     * @param items to convert into String
     * @return a String
     */
    public static String getJsonString(ArrayList<Integer> items) {
        Gson gson = new Gson();
        return gson.toJson(items);
    }

    /**
     * Get from a json string an ArrayList of type Integer
     *
     * @param jsonString the string to get from
     * @return an ArrayList<Integer>
     */
    public static ArrayList<Integer> getArrayListFromJson(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        return gson.fromJson(jsonString, type);
    }

    /**
     * Checks if the text is empty or not.
     *
     * @param text to check
     * @return Constant.NO_DATA if the text is null or "", the text otherwise.
     */
    public static String isEmpty(String text) {
        return (TextUtils.isEmpty(text) ?
                Constant.NO_DATA : text);
    }

    /**
     * Gets the help text in the physical performance test.
     *
     * @param context The context of the Activity
     * @param helpId  one of [{@link Constant#HELP_IMC}, {@link Constant#HELP_MARCH}, {@link Constant#HELP_STATIC_BALANCE}, {@link Constant#HELP_SQUAD}, {@link Constant#HELP_GRIP_STRENGTH}]
     * @return corresponding help text
     */
    public static String getHelpText(Context context, int helpId) {
        switch (helpId) {
            case Constant.HELP_IMC:
                return context.getString(R.string.imc_help);
            case Constant.HELP_MARCH:
                return context.getString(R.string.march_help);
            case Constant.HELP_STATIC_BALANCE:
                return context.getString(R.string.static_balance_help);
            case Constant.HELP_SQUAD:
                return context.getString(R.string.squad_help);
            case Constant.HELP_GRIP_STRENGTH:
                return context.getString(R.string.grip_strength_help);
        }
        return "";
    }

    /**
     * Appends to the str two dost + the appendStr.
     *
     * @param str       The string that goes before de :
     * @param appendStr The string that goes after de :
     * @return str: appendStr.
     */
    public static String appendWithDots(String str, String appendStr) {
        return String.format("%s: %s", str, appendStr);
    }

    /**
     * Appends to the str two dost + the appendStr + appendMeasureUnit.
     *
     * @param str               The string that goes before de :
     * @param appendStr         The string that goes after de :
     * @param appendMeasureUnit The string unit measure
     * @return str: appendStr appendMeasureUnit.
     */
    public static String appendWithDots(String str, String appendStr, String appendMeasureUnit) {
        return String.format("%s: %s %s", str, appendStr, appendMeasureUnit);
    }

    /**
     * Appends to the str the appendMeasureUnit.
     *
     * @param str               The string that goes first
     * @param appendMeasureUnit The string unit measure that goes second
     * @return str appendMeasureUnit.
     */
    public static String appendMeasureUnit(String str, String appendMeasureUnit) {
        return String.format("%s %s", str, appendMeasureUnit);
    }

    /**
     * Classified according to the isChecked state.
     *
     * @param context   The context of the Activity
     * @param isChecked the selector
     * @return valid if true, fail otherwise.
     */
    public static String getClassificationText(Context context, boolean isChecked) {
        return isChecked ? context.getString(R.string.valid) : context.getString(R.string.failure);
    }

    /**
     * Gets the text according to the value.
     *
     * @param context The context of the Activity
     * @param value   [{@link Constant#RESULT_OK}], [{@link Constant#RESULT_REGULAR}] or [{@link Constant#RESULT_NEGATIVE}]
     * @return the corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.result_ok);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.result_regular);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.result_negative);
        }
        return "";
    }

    /**
     * Truncate a value using the [@link RoundingMode#CEILING]
     *
     * @param value the value to transform
     * @return a new value as "#.###"
     */
    public static String truncate(double value) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        return df.format(value);
    }

    /**
     * Gets the text if hint is empty
     *
     * @param editText to check for
     * @return text if is not empty, hint otherwise
     */
    public static String getHintIfEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString())
                ? editText.getHint().toString() : editText.getText().toString();
    }

    /**
     * Capitalize only the first letter of the word and lower the rest.
     *
     * @param word to modify
     * @return first letter capitalize and lower the rest.
     */
    public static String capitalizeOnlyFirstLetter(String word) {
        if (word.isEmpty()) return "";
        if (word.length() == 1) return word.toUpperCase(Locale.ROOT);

        return word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT);
    }

    /**
     * If the name has to values, capitalize first letter an lower de rest.
     *
     * @param name to format
     * @return first letter capitalize and lower the rest
     */
    public static String formatName(String name) {
        if (name.length() == 0) return "";

        String[] temp = name.split(" ");
        if (temp.length > 1) {
            return String.format("%s %s", capitalizeOnlyFirstLetter(temp[0]), capitalizeOnlyFirstLetter(temp[1]));
        }
        return capitalizeOnlyFirstLetter(temp[0]);
    }
}
