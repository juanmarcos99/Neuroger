package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 26/09/2017.
 */

public class EvaluationSubValHelper {

    public static int evaluate(boolean badMemory, int memoryComparison, boolean difficulties) {
        if (badMemory || (memoryComparison > 1) || difficulties) {
            return Constant.RESULT_NEGATIVE;
        }
        return Constant.RESULT_OK;
    }

    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.result_robust);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.in_risk);
        }
        return "";
    }
}
