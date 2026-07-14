package software.cneuro.neuroger.content;

import android.content.Context;

import java.util.Locale;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 19/08/2016.
 */
public class PfeifferHelper {


    public static int getTotalItems(Context context) {
        return context.getResources().getStringArray(R.array.pfeiffer_questions).length;
    }

    public static String getQuestionText(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.pfeiffer_questions);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return array[pagePosition];
    }

    public static String getQuestionText(Context context, int pagePosition, String patientName) {
        String[] array = context.getResources().getStringArray(R.array.pfeiffer_questions);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return String.format(Locale.getDefault(), array[pagePosition], patientName);
    }

    public static String[] getAnswerList(Context context) {
        return context.getResources().getStringArray(R.array.pfeiffer_answer);
    }

    public static String getAnswerText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.pfeiffer_answer);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelp(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.pfeiffer_help);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return array[0];
        }

        return array[pagePosition];
    }
}
