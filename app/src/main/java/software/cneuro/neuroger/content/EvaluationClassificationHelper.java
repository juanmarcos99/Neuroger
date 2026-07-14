package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationClassificationHelper {

    /**
     * Gets the text according to the value.
     *
     * @param context The context of the Activity
     * @param value   @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.healthy);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.in_risk);
        }
        return "";
    }

    public static int evaluate(
            double antecedentsEvaluation,
            int medicationQuantity,
            double subjectiveEvaluation,
            double katzEvaluation,
            double frailEvaluation,
            double lawtonEvaluation,
            double pasEvaluation,
            double pAffectiveEvaluation) {
        if (EvaluationPAntecedentsHelper.evaluate(antecedentsEvaluation) == Constant.RESULT_OK &&
                EvaluationPAntecedentsHelper.evaluateMedicationQuantity(medicationQuantity) == Constant.RESULT_OK &&
                subjectiveEvaluation == Constant.RESULT_OK &&
                katzEvaluation == Constant.RESULT_OK &&
                frailEvaluation == Constant.RESULT_OK &&
                lawtonEvaluation == Constant.RESULT_OK &&
                pasEvaluation == Constant.RESULT_OK &&
                pAffectiveEvaluation == Constant.RESULT_OK)
            return Constant.RESULT_OK;
        else return Constant.RESULT_NEGATIVE;
    }
}
