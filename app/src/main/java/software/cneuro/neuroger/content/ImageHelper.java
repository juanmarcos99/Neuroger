package software.cneuro.neuroger.content;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 9/16/2015.
 */
public class ImageHelper {
    public static final int NO_DRAWABLE_VALUE = Integer.MIN_VALUE;

    public static int getResultDrawable(int evaluationResult) {
        if (evaluationResult == Constant.RESULT_OK) {
            return R.drawable.result_fine;
        } else if (evaluationResult == Constant.RESULT_NEGATIVE) {
            return R.drawable.result_bad;
        } else if (evaluationResult == Constant.RESULT_REGULAR) {
            return R.drawable.result_regular;
        }
        return NO_DRAWABLE_VALUE;
    }

    public static int getImcResultDrawable(int evaluationResult) {
        if (evaluationResult == Constant.RESULT_MALNOURISHED) {
            return R.drawable.desnutrido;
        } else if (evaluationResult == Constant.RESULT_OBESE) {
            return R.drawable.obeso;
        } else if (evaluationResult == Constant.RESULT_OVERWEIGHT) {
            return R.drawable.sobrepeso;
        } else if (evaluationResult == Constant.RESULT_NORMO_WEIGHT) {
            return R.drawable.normo_peso;
        }
        return NO_DRAWABLE_VALUE;
    }

    public static int getMiniMentalStateImageHelpResourceId(int helpID) {
        switch (helpID) {
            case Constant.HELP_LANGUAGE_1:
                return R.drawable.clock;
            case Constant.HELP_LANGUAGE_2:
                return R.drawable.lapiz;
            case Constant.HELP_LANGUAGE_4:
                return R.drawable.cierre_los_ojos;
            case Constant.HELP_DESIGN:
                return R.drawable.copy_design;
        }

        return NO_DRAWABLE_VALUE;
    }

    public static int getEvaluationImage(double evaluation) {
        if (evaluation == Constant.RESULT_OK)
            return R.drawable.ic_ok;
        else if(evaluation == Constant.RESULT_REGULAR)
            return R.drawable.ic_regular;
        else
            return R.drawable.ic_bad;
    }
}
