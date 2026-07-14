package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 02/08/2016.
 */
public class CDRHelper {

    public static String getTestTypesText(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.cdr_tests);
        if (pagePosition < 0 || pagePosition >= array.length) {
           return "";
        }

        return array[pagePosition];
    }

    public static int getTotalItems(Context context) {
        return context.getResources().getStringArray(R.array.cdr_tests).length;
    }

    public static String[] getItemsList(Context context, int pagePosition) {
        String[] result = new String[]{};

        switch (pagePosition) {
            case 0:
                result = getMemoryItemsList(context);
                break;
            case 1:
                result = getOrientationItemsList(context);
                break;
            case 2:
                result = getJudgmentProblemsItemsList(context);
                break;
            case 3:
                result = getCommunityItemsList(context);
                break;
            case 4:
                result = getHomeHobbiesItemsList(context);
                break;
            case 5:
                result = getPersonalCareItemsList(context);
                break;
        }

        return result;
    }

    private static String[] getMemoryItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_memory);
    }

    private static String[] getOrientationItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_orientation);
    }

    private static String[] getJudgmentProblemsItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_judgment_and_problem_solution);
    }

    private static String[] getCommunityItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_community_role);
    }

    private static String[] getHomeHobbiesItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_home_and_hobbies);
    }

    private static String[] getPersonalCareItemsList(Context context) {
        return context.getResources().getStringArray(R.array.cdr_personal_care);
    }

    public static String getHelp(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.cdr_help);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return array[pagePosition];
    }

    public static String getItemsList(Context context, int pagePosition, int answerPosition) {
        String result = "";

        switch (pagePosition) {
            case 0:
                result = getMemoryItemText(context, answerPosition);
                break;
            case 1:
                result = getOrientationItemText(context, answerPosition);
                break;
            case 2:
                result = getJudgmentProblemsItemText(context, answerPosition);
                break;
            case 3:
                result = getCommunityItemText(context, answerPosition);
                break;
            case 4:
                result = getHomeHobbiesItemText(context, answerPosition);
                break;
            case 5:
                result = getPersonalCareItemText(context, answerPosition);
                break;
        }

        return result;
    }

    private static String getMemoryItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_memory);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getOrientationItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_orientation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getJudgmentProblemsItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_judgment_and_problem_solution);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getCommunityItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_community_role);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getHomeHobbiesItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_home_and_hobbies);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getPersonalCareItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.cdr_personal_care);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

}
