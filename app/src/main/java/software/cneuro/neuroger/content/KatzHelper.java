package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

public class KatzHelper {
    public static int getTotalItems(Context context) {
        return context.getResources().getStringArray(R.array.katz_index_questions).length;
    }

    public static String getQuestionText(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.katz_index_questions);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return array[pagePosition];
    }

    public static String[] getAnswerList(Context context) {
        return context.getResources().getStringArray(R.array.katz_index_answer);
    }

    public static String getAnswerText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.katz_index_answer);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelp(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.katz_index_help);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return context.getString(R.string.katz_help_prefix) + array[pagePosition];
    }
}
