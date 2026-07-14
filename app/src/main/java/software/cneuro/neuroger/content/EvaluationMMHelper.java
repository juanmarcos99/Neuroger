package software.cneuro.neuroger.content;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 15/06/2016.
 */
public class EvaluationMMHelper {
    public static final int INIT_MATH_DEFAULT_VALUE = 100;
    public static final int MATH_SUBTRACTION_DEFAULT_VALUE = 7;

    public static int evaluate(double value) {
        return Constant.RESULT_OK;
    }

    public static int evaluate(double value, int age, int levelOfSchooling) {
        if (80 <= age) {
            if (levelOfSchooling == 0)
                return value < 25 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 1)
                return value < 25 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 2)
                return value < 26 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
        } else if (70 <= age) {
            if (levelOfSchooling == 0)
                return value < 25 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 1)
                return value < 28 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 2)
                return value < 26 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
        } else {
            if (levelOfSchooling == 0)
                return value < 26 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 1)
                return value < 27 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
            if (levelOfSchooling == 2)
                return value < 28 ? Constant.RESULT_CI_WITH : Constant.RESULT_OK;
        }
        return -1;
    }

    /**
     * Minimental evaluation text. Gets the text according to the value.
     *
     * @param context The context of the activity
     * @param value   @{@link software.cneuro.neuroger.constant.Constant#RESULT_CI_WITH} or @{@link software.cneuro.neuroger.constant.Constant#RESULT_OK}
     * @return the corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_CI_WITH:
                return context.getString(R.string.result_with_minimental);
            case Constant.RESULT_OK:
                return context.getString(R.string.result_without_minimental);
        }
        return "";
    }

    public static int getMathCount(List<Integer> values) {
        int result = 0;
        int math = INIT_MATH_DEFAULT_VALUE - MATH_SUBTRACTION_DEFAULT_VALUE;
        for (Integer value : values) {
            if (value != math) {
                math = value;
            } else {
                result++;
            }
            math = value - MATH_SUBTRACTION_DEFAULT_VALUE;
        }

        return result;
    }

    public static int getSpellingCount(String word) {
        String[] mundo = new String[]{"o", "d", "n", "u", "m"};

        String wordInLowerCase = word.toLowerCase(Locale.ROOT);

        int count = 0;
        if (word.length() > 5) return count;

        for (int i = 0; i < mundo.length; i++) {
            int wordLetterPosition = wordInLowerCase.indexOf(mundo[i]);
            count += wordLetterPosition == i ? 1 : 0;
        }
        return count;
    }

    public static int getValidValue(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
