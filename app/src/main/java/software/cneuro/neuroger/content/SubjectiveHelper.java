package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 22/09/2016.
 */

public class SubjectiveHelper {
    public static String getAnswer0Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_health_state);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer1AText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_1a);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer1BText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_1b);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer2Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_2);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer3Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_3);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer4Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_4);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getAnswer5Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_answer_5);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelpText(Context context, int helpID) {
        switch (helpID) {
            case Constant.HELP_SV_1:
                return getHelp1Text(context, 0);
            case Constant.HELP_SV_2:
                return getHelp1Text(context, 1);
            case Constant.HELP_SV_3:
                return getHelp1Text(context, 2);
            case Constant.HELP_SV_4:
                return getHelp1Text(context, 3);
            case Constant.HELP_SV_5:
                return getHelp1Text(context, 4);

            case Constant.HELP_SV_6:
                return getHelp2Text(context, 0);
            case Constant.HELP_SV_7:
                return getHelp2Text(context, 1);
            case Constant.HELP_SV_8:
                return getHelp2Text(context, 2);


            case Constant.HELP_SV_9:
                return getHelp3Text(context, 0);
            case Constant.HELP_SV_10:
                return getHelp3Text(context, 1);
            case Constant.HELP_SV_11:
                return getHelp3Text(context, 2);
        }

        return "";
    }

    public static String getHelp1Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_help_1);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelp2Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_help_2);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelp3Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.subjective_evaluation_help_3);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }
}
