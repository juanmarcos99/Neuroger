package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationPsychoaffectiveHelper {

    public static int evaluate(double value) {
        if (value > 3.0) {
            return Constant.RESULT_NEGATIVE;
        }
        return Constant.RESULT_OK;
    }

    /**
     * Psychoaffective evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity
     * @param value   @{@link Constant#RESULT_OK} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.result_without_psychoaffective_alteration);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.result_with_psychoaffective_alteration);
        }
        return "";
    }

    public static double evaluate(
            Context context,
            int sadness,
            int pessimism,
            int suicide,
            int sleepDisorder,
            int nervous,
            int lossSelfConfidence,
            int irritability) {
        double result = getSadnessScaleValue(context, sadness);
        result += getPessimismScaleValue(context, pessimism);
        result += getSuicideScaleValue(context, suicide);
        result += getSleepDisorderScaleValue(context, sleepDisorder);
        result += getNervousScaleValue(context, nervous);
        result += getLossSelfConfidenceScaleValue(context, lossSelfConfidence);
        result += getIrritabilityScaleValue(context, irritability);

        return result;
    }

    public static double evaluate(Context context, int questionPos, int answerPos) {
        switch (questionPos) {
            case 0:
                return getSadnessScaleValue(context, answerPos);
            case 1:
                return getPessimismScaleValue(context, answerPos);
            case 2:
                return getSuicideScaleValue(context, answerPos);
            case 3:
                return getSleepDisorderScaleValue(context, answerPos);
            case 4:
                return getNervousScaleValue(context, answerPos);
            case 5:
                return getLossSelfConfidenceScaleValue(context, answerPos);
            case 6:
                return getIrritabilityScaleValue(context, answerPos);
        }
        return -1;
    }

    private static int getSadnessScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_sadness_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getPessimismScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_pessimism_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static double getSuicideScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_suicide_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getSleepDisorderScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_sleep_disorder_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static int getNervousScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_nervous_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    private static double getLossSelfConfidenceScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_loss_self_confidence_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Double.parseDouble(array[position]);
    }

    private static double getIrritabilityScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.psychoaffective_irritability_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Double.parseDouble(array[position]);
    }
}
