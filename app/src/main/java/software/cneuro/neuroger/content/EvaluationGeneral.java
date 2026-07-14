package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationGeneral {

    public static String getEvaluation(Context context, int value) {
        if (value == Constant.RESULT_OK)
            return context.getString(R.string.healthy);
        return context.getString(R.string.in_risk);
    }
}
