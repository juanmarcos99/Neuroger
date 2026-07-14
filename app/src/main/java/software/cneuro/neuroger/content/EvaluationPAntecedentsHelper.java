package software.cneuro.neuroger.content;

import android.content.Context;

import java.util.List;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationPAntecedentsHelper {

    public static int evaluate(double value) {
        if (value < 3)
            return Constant.RESULT_OK;
        else
            return Constant.RESULT_NEGATIVE;
    }

    public static int evaluateMedicationQuantity(int value) {
        if (value < 5)
            return Constant.RESULT_OK;
        else
            return Constant.RESULT_NEGATIVE;
    }

    public static double evaluate(Context context, List<Integer> antecedents) {
        String[] array = context.getResources().getStringArray(R.array.pathological_antecedents_scale);
        double result = 0;
        for (Integer position : antecedents) {
            if (position < 0 || position >= array.length) {
                return -1;
            }
            result += Integer.parseInt(array[position]);
        }
        return result;
    }

    public static String getEvaluationText(Context context, double value) {
        if (value < 3)
            return context.getString(R.string.antecedents_result_ok);
        else
            return context.getString(R.string.antecedents_result_negative);
    }

    public static String getMedicamentEvaluationText(Context context, double value) {
        if (value < 5)
            return context.getString(R.string.medicaments_result_ok);
        else
            return context.getString(R.string.medicaments_result_negative);
    }
}
