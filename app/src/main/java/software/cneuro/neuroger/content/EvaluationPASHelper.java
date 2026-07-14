package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationPASHelper {

    public static int evaluate(double value) {
        if (value >= 10)
            return Constant.RESULT_NEGATIVE;
        else if (value >= 8)
            return Constant.RESULT_REGULAR;
        else
            return Constant.RESULT_OK;
    }

    public static String getEvaluationText(Context context, int value) {
        if (value == Constant.RESULT_NEGATIVE)
            return context.getString(R.string.social_situation_at_risk);
        else if (value == Constant.RESULT_REGULAR)
            return context.getString(R.string.social_situation_regular);
        else
            return context.getString(R.string.social_situation_ok);
    }
}
