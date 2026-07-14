package software.cneuro.neuroger.content;

import android.content.Context;
import android.text.TextUtils;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 9/16/2015.
 */
public class EvaluationPPHelper {

    /**
     * Physical index evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity.
     * @param value   @{@link Constant#RESULT_OK}, @{@link Constant#RESULT_REGULAR} or @{@link Constant#RESULT_NEGATIVE}
     * @return The corresponding string.
     */
    public static String getEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_OK:
                return context.getString(R.string.result_ok);
            case Constant.RESULT_REGULAR:
                return context.getString(R.string.result_regular);
            case Constant.RESULT_NEGATIVE:
                return context.getString(R.string.result_negative);
        }
        return "";
    }

    /**
     * IMC evaluation text. Gets the text according to the value.
     *
     * @param context The context of the Activity.
     * @param value   @{@link Constant#RESULT_MALNOURISHED}, @{@link Constant#RESULT_OBESE} or @{@link Constant#RESULT_OVERWEIGHT} or @{@link Constant#RESULT_NORMO_WEIGHT}
     * @return The corresponding string.
     */
    public static String getImcEvaluationText(Context context, int value) {
        switch (value) {
            case Constant.RESULT_NORMO_WEIGHT:
                return context.getString(R.string.normo_weight);
            case Constant.RESULT_OVERWEIGHT:
                return context.getString(R.string.overweight);
            case Constant.RESULT_OBESE:
                return context.getString(R.string.obese);
            case Constant.RESULT_MALNOURISHED:
                return context.getString(R.string.malnourished);
        }
        return "";
    }

    /**
     * General evaluation.
     *
     * @param generalScoreEvaluation The score provided by {@link EvaluationPPHelper#evaluationScoreGeneral}
     * @return The score.
     */
    public static int evaluateGeneral(double generalScoreEvaluation) {
        if (generalScoreEvaluation >= 9.0 && generalScoreEvaluation <= 12.0) {
            return Constant.RESULT_OK;
        } else if (generalScoreEvaluation >= 6.0 && generalScoreEvaluation <= 9.0) {
            return Constant.RESULT_REGULAR;
        } else {
            return Constant.RESULT_NEGATIVE;
        }
    }

    /**
     * Get the general score.
     *
     * @param evalScoreMarch         March value.
     * @param evalScoreStaticBalance Static balance value.
     * @param evalScoreRising        Rising value.
     * @param evalScoreForce         Force  value.
     * @return The sum of all params.
     */
    public static int evaluationScoreGeneral(int evalScoreMarch, int evalScoreStaticBalance, int evalScoreRising, int evalScoreForce) {
        return evalScoreMarch +
                evalScoreStaticBalance +
                evalScoreRising +
                evalScoreForce;
    }

    /**
     * IMC = weight(Kg) / (size(m))2
     *
     * @param size   Size value.
     * @param weight Weight value.
     * @return IMC
     */
    public static double imc(double size, double weight) {
        return weight / Math.pow(size, 2);
    }

    /**
     * IMC evaluation.
     *
     * @param imc IMC value.
     * @return @{@link Constant#RESULT_NORMO_WEIGHT}, @{@link Constant#RESULT_MALNOURISHED} or @{@link Constant#RESULT_OBESE}  or @{@link Constant#RESULT_OVERWEIGHT}
     */
    public static int evaluateIMC(double imc) {
        if (imc < 18.0) {
            return Constant.RESULT_MALNOURISHED;
        } else if (imc > 30.0) {
            return Constant.RESULT_OBESE;
        } else if (imc > 26.0) {
            return Constant.RESULT_OVERWEIGHT;
        }
        return Constant.RESULT_NORMO_WEIGHT;
    }

    /**
     * Walking speed = 4 / time.
     *
     * @param time Walking time.
     * @return Walking speed.
     */
    public static double walkSpeed(double time) {
        return (time == 0) ? 0 : 4.0 / time;
    }

    /**
     * Average step width = 4 / number of steps * 100.
     *
     * @param stepCount Step count.
     * @return Average step.
     */
    public static double averageStepExtend(int stepCount) {
        return (stepCount == 0) ? 0 : 4.0 / stepCount * 100.0;
    }

    /**
     * Cadence number of steps in a minute = 60 * (number of steps / time).
     *
     * @param time      The time it took.
     * @param stepCount Step count.
     * @return Number of steps in a given time.
     */
    public static double rateMinute(double time, int stepCount) {
        return (time == 0) ? 0 : 60.0 * (stepCount / time);
    }

    /**
     * Evaluation of march.
     *
     * @param time The time it took.
     * @return Evaluation of march.
     */
    public static int evaluateMarch(double time) {
        double speed = walkSpeed(time);
        if (speed > 0.83) {
            return Constant.RESULT_OK;
        } else if (speed < 0.46) {
            return Constant.RESULT_NEGATIVE;
        }
        return Constant.RESULT_REGULAR;
    }

    /**
     * Get the score, depending on the evaluation provided by {@link EvaluationPPHelper#evaluateMarch}
     *
     * @param evaluation The evaluation provided by {@link EvaluationPPHelper#evaluateMarch}
     * @return The score.
     */
    public static int evaluationScoreMarch(int evaluation) {
        if (evaluation == Constant.RESULT_OK) {
            return 5;
        } else if (evaluation == Constant.RESULT_REGULAR) {
            return 3;
        }
        return 1;
    }

    /**
     * Evaluation of static balance.
     *
     * @param parallelFeet       If could stand in parallel feet.
     * @param semiTandemPosition If could stand in semi tandem position.
     * @param tandemPosition     If could stand in tandem position.
     * @return Evaluation of static balance.
     */
    public static int evaluateStaticBalance(boolean parallelFeet, boolean semiTandemPosition, boolean tandemPosition) {
        if (parallelFeet && semiTandemPosition && tandemPosition) {
            return Constant.RESULT_OK;
        } else if (parallelFeet && (semiTandemPosition || tandemPosition) || semiTandemPosition && tandemPosition) {
            return Constant.RESULT_REGULAR;
        }
        return Constant.RESULT_NEGATIVE;
    }

    /**
     * Get the score, depending on the evaluation provided by {@link EvaluationPPHelper#evaluateStaticBalance}
     *
     * @param evaluation The evaluation provided by {@link EvaluationPPHelper#evaluateStaticBalance}
     * @return The score.
     */
    public static int evaluationScoreStaticBalance(int evaluation) {
        if (evaluation == Constant.RESULT_OK) {
            return 2;
        } else if (evaluation == Constant.RESULT_REGULAR) {
            return 1;
        }
        return 0;
    }

    /**
     * Evaluation of squads.
     *
     * @param time            The time it took.
     * @param repetitionsDone Repetitions done.
     * @return Evaluation of squads.
     */
    public static int evaluateRising(double time, boolean repetitionsDone) {
        if (!repetitionsDone) {
            return Constant.RESULT_NEGATIVE;
        }

        if (time <= 17.0) {
            return Constant.RESULT_OK;
        } else {
            return Constant.RESULT_REGULAR;
        }
    }

    /**
     * Get the score, depending on the evaluation provided by {@link EvaluationPPHelper#evaluateRising}
     *
     * @param evaluation The evaluation provided by {@link EvaluationPPHelper#evaluateRising}
     * @return The score.
     */
    public static int evaluationScoreRising(int evaluation) {
        if (evaluation == Constant.RESULT_OK) {
            return 2;
        } else if (evaluation == Constant.RESULT_REGULAR) {
            return 1;
        }
        return 0;
    }

    /**
     * Evaluates in both hands the grip strength.
     *
     * @param leftValue  Grip Strength in the left hand.
     * @param rightValue Grip Strength in the right hand.
     * @param sexFem     If is female.
     * @return Evaluation of grip strength.
     */
    public static int evaluateForce(double leftValue, double rightValue, boolean sexFem) {
        int evalLeft = evaluateForce(leftValue, sexFem);
        int evalRight = evaluateForce(rightValue, sexFem);

        if (evalLeft == Constant.RESULT_OK && evalRight == Constant.RESULT_OK) {
            return Constant.RESULT_OK;
        } else if ((evalLeft == Constant.RESULT_OK || evalLeft == Constant.RESULT_REGULAR) &&
                (evalRight == Constant.RESULT_OK || evalRight == Constant.RESULT_REGULAR)) {
            return Constant.RESULT_REGULAR;
        }
        return Constant.RESULT_NEGATIVE;
    }

    /**
     * Evaluation of grip strength.
     *
     * @param value  Grip strength value.
     * @param sexFem If is female.
     * @return Evaluation of grip strenth.
     */
    private static int evaluateForce(double value, boolean sexFem) {
        if (sexFem) {
            if (value >= 20) {
                return Constant.RESULT_OK;
            } else if (value >= 10) {
                return Constant.RESULT_REGULAR;
            } else {
                return Constant.RESULT_NEGATIVE;
            }
        } else {
            if (value >= 30) {
                return Constant.RESULT_OK;
            } else if (value >= 20) {
                return Constant.RESULT_REGULAR;
            } else {
                return Constant.RESULT_NEGATIVE;
            }
        }
    }

    /**
     * Get the score, depending on the evaluation provided by {@link EvaluationPPHelper#evaluateForce}
     *
     * @param evaluation The evaluation provided by {@link EvaluationPPHelper#evaluateForce}
     * @return The score.
     */
    public static int evaluationScoreForce(int evaluation) {
        if (evaluation == Constant.RESULT_OK) {
            return 3;
        } else if (evaluation == Constant.RESULT_REGULAR) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Make sure returns always a valid int
     *
     * @param value Value to parse.
     * @return 0 if the String is null or "".
     */
    public static int zeroIfEmpty(String value) {
        return TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
    }

    /**
     * Make sure returns always a valid double
     *
     * @param value Value to parse.
     * @return 0 if the String is null or "".
     */
    public static double zeroIfEmptyDouble(String value) {
        return TextUtils.isEmpty(value) ? 0 : Double.parseDouble(value);
    }
}
