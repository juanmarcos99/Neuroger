package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 23/09/2016.
 */

public class EvaluationPfeifferHelper {

    /**
     * Gets the value on a predefined scale @{@link R.array#pfeiffer_scale} according to the answer position.
     *
     * @param context The context of the Activity.
     * @param position Answers position.
     * @return The scale value corresponding to the answer position.
     */
    public static int getScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.pfeiffer_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    public static int evaluate(double value) {
        if (value > 7.0) {
            return Constant.RESULT_NEGATIVE;
        }
        return Constant.RESULT_OK;
    }

    /**
     * Pfeiffer evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity.
     * @param value @{@link Constant#RESULT_OK} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.pfeiffer_result_ok);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.pfeiffer_result_negative);
        }
        return "";
    }
}
