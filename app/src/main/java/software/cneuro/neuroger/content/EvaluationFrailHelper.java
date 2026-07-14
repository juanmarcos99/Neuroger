package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationFrailHelper {

    public static int evaluate(double value) {
        if (value >= 3.0) {
            return Constant.RESULT_NEGATIVE;
        } else if (value >= 1.0) {
            return Constant.RESULT_REGULAR;
        }
        return Constant.RESULT_OK;
    }

    /**
     * Frail scale evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity
     * @param value   @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_REGULAR} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.result_robust);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.result_pre_fragile);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.result_fragile);
        }
        return "";
    }

    public static int getEvaluation(int fatigue, int resistance, int wandering, int totalDiseases, double weightCurrent, double weightYearAgo) {
        int score = fatigue <= 1 ? 1 : 0;
        score += EvaluationFrailHelper.getResistanceScaleValue(resistance);
        score += EvaluationFrailHelper.getWanderingScaleValue(wandering);
        score += totalDiseases >= 5 ? 1 : 0;

        double weight = ((weightYearAgo - weightCurrent) / weightYearAgo) * 100;
        if (weight >= 5) score += 1;

        return score;
    }

    private static int getResistanceScaleValue(int position) {
        return position == 0 ? 1 : 0;
    }

    public static int getWanderingScaleValue(int position) {
        return position == 0 ? 1 : 0;
    }
}
