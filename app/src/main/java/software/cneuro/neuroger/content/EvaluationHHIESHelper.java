package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 24/10/2016.
 */

public class EvaluationHHIESHelper {

    /**
     * Gets the value on a predefined scale @{@link R.array#hhies_scale} according to the answer position.
     *
     * @param context The context of the Activity.
     * @param position Answers position.
     * @return scale value
     */
    public static int getScaleValue(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.hhies_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[position]);
    }

    public static int evaluate(double value) {
        if (value > 25.0) {
            return Constant.RESULT_NEGATIVE;
        } else if(value > 9.0) {
            return Constant.RESULT_REGULAR;
        }
        return Constant.RESULT_OK;
    }

    /**
     * Geriatric depression scale evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity.
     * @param value @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_REGULAR} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.hhies_result_ok);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.hhies_result_regular);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.hhies_result_negative);
        }
        return "";
    }
}
