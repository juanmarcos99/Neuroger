package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationCognitiveHelper {
    /**
     * Gets the text according to the value.
     *
     * @param context The context of the Activity
     * @param value   @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_REGULAR}, @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.healthy);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.alert);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.in_risk);
        }
        return "";
    }

    public static int evaluate(int cdrEvaluation,
                               int gdsEvaluation,
                               int hhiesEvaluation,
                               int mmseEvaluation,
                               int pfeifferEvaluation) {
        // Healthy
        if (isOk(cdrEvaluation) && isOk(gdsEvaluation) && isOk(hhiesEvaluation) && isOk(mmseEvaluation) && isOk(pfeifferEvaluation))
            return Constant.RESULT_OK;
        // Risk
        return Constant.RESULT_NEGATIVE;
    }

    private static boolean isOk(int evaluation) {
        return evaluation == Constant.RESULT_OK;
    }
}
