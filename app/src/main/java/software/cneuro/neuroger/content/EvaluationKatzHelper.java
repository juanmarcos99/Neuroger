package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

public class EvaluationKatzHelper {

    public static int evaluate(double value) {
        if (value > 3.0) {
            return Constant.RESULT_NEGATIVE;
        } else if(value > 1.0) {
            return Constant.RESULT_REGULAR;
        }
        return Constant.RESULT_OK;
    }

    /**
     * katz index evaluation text. Gets the text according to the value.
     *
     * @param context
     * @param value @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_REGULAR} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.katz_result_ok);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.katz_result_regular);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.katz_result_negative);
        }
        return "";
    }
}
