package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationLawtonHelper {
    public static int evaluate(double value, boolean isFemale) {
        return isFemale ? getFemaleEvaluation(value) : getMaleEvaluation(value);
    }

    private static int getFemaleEvaluation(double value) {
        if (value >= 8.0) return Constant.RESULT_OK;
        return Constant.RESULT_NEGATIVE;
    }

    private static int getMaleEvaluation(double value) {
        if (value >= 5.0) return Constant.RESULT_OK;
        return Constant.RESULT_NEGATIVE;
    }

    public static double evaluate(
            Context context,
            int phone,
            int shopping,
            int food,
            int housekeeping,
            int laundry,
            int transportation,
            int medication,
            int finance) {
        int result = getPhoneScaleValue(context, phone);
        result += getShoppingScaleValue(context, shopping);
        result += getFoodScaleValue(context, food);
        result += getHousekeepingScaleValue(context, housekeeping);
        result += getLaundryScaleValue(context, laundry);
        result += getTransportationScaleValue(context, transportation);
        result += getMedicationScaleValue(context, medication);
        result += getFinanceScaleValue(context, finance);

        return result;
    }

    public static double evaluate(Context context, int questionPos, int answerPos) {
        switch (questionPos) {
            case 0:
                return getPhoneScaleValue(context, answerPos);
            case 1:
                return getShoppingScaleValue(context, answerPos);
            case 2:
                return getFoodScaleValue(context, answerPos);
            case 3:
                return getHousekeepingScaleValue(context, answerPos);
            case 4:
                return getLaundryScaleValue(context, answerPos);
            case 5:
                return getTransportationScaleValue(context, answerPos);
            case 6:
                return getMedicationScaleValue(context, answerPos);
            case 7:
                return getFinanceScaleValue(context, answerPos);
        }
        return -1;
    }

    public static String getEvaluationText(Context context, boolean isFemale, double value) {
        return isFemale ? getFemaleEvaluationText(context, value) : getMaleEvaluationText(context, value);
    }

    private static String getFemaleEvaluationText(Context context, double value) {
        if (value >= 8.0) return context.getString(R.string.autonomous_female);
        else if (value >= 6.0) return context.getString(R.string.light_dependency);
        else if (value >= 4.0)
            return context.getString(R.string.moderate_dependency);
        else if (value >= 2.0)
            return context.getString(R.string.severe_dependency);
        return context.getString(R.string.total_dependency);
    }

    private static String getMaleEvaluationText(Context context, double value) {
        if (value >= 5.0) return context.getString(R.string.autonomous_male);
        else if (value == 4.0)
            return context.getString(R.string.light_dependency);
        else if (value >= 2.0)
            return context.getString(R.string.moderate_dependency);
        else if (value == 1.0)
            return context.getString(R.string.severe_dependency);
        return context.getString(R.string.total_dependency);
    }

    private static int getFinanceScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_finances_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getMedicationScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_medication_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getTransportationScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_transportation_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getLaundryScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_clothing_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getHousekeepingScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_house_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getFoodScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_food_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getShoppingScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_shopping_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getPhoneScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_phone_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }
}
