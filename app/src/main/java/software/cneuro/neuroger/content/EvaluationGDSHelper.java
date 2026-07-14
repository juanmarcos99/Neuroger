package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 24/10/2016.
 */

public class EvaluationGDSHelper {
    private static final String SCALE_SEPARATOR = "\\|";

    public static boolean isAffirmativeAnswer(int value) {
        return value == 0;
    }

    /**
     * Gets the value on a predefined scale @{@link R.array#geriatric_depression_scale}
     * according to the answer position.
     *
     * @param context  The context of the Activity.
     * @param position Answers position.
     * @return Scale value.
     */
    public static int getScaleValue(Context context, int question, int position) {
        String[] array = context.getResources().getStringArray(R.array.geriatric_depression_scale);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Integer.parseInt(array[question].split(SCALE_SEPARATOR)[position]);
    }

    public static int evaluate(double value) {
        if (value >= 2.0) {
            return Constant.RESULT_NEGATIVE;
        }
        return Constant.RESULT_OK;
    }

    /**
     * Geriatric depression scale evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity.
     * @param value   @{@link Constant#RESULT_OK} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        if (value == Constant.RESULT_OK) return context.getString(R.string.gds_result_ok);
        else return context.getString(R.string.gds_result_negative);
    }
}
