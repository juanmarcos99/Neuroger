package software.cneuro.neuroger.content;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.Map;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 23/09/2016.
 */

public class EvaluationCDRHelper {
    /**
     * Evaluates the CDR according to a given scale, the memory value and the secondary values of the test.
     *
     * @param memoryValue The memory value.
     * @param values      The secondaries values.
     * @return The CDR value.
     */
    public static double evaluate(String[] scaleValues, double memoryValue, double... values) {
        LinkedHashMap<Double, Double> secondaryValues = new LinkedHashMap<>();
        int i = 0;
        for (String str : scaleValues) {
            double secondaryValue = Double.parseDouble(str);
            secondaryValues.put(secondaryValue, values[i]);
            i++;
        }

        if (Double.compare(memoryValue, 0.5) == 0) {
            // at least there are 3 values that are >= 1
            if (isAtLeastWithValue(secondaryValues, 1, 3)) {
                return 1;
            } else {
                return 0.5;
            }
        } else if (Double.compare(memoryValue, 0) == 0) {
            // at least there are 2 values >= 0.5
            if (isAtLeastWithValue(secondaryValues, 0.5, 2)) {
                return 0.5;
            } else {
                return 0;
            }
        } else {
            secondaryValues = new LinkedHashMap<>();
            for (String str : scaleValues) {
                double secondaryValue = Double.parseDouble(str);
                secondaryValues.put(secondaryValue, repeatedCount(secondaryValue, values));
            }
            // value of the scales that repeated 3 times
            double value3 = getValueThatRepeat(secondaryValues, 3);
            if (Double.compare(value3, -1) == 0) { // there is no value
                // value of the scales that repeated 2 times
                //double value2 = getValueThatRepeat(secondaryValues, 2);
                double value2 = getTiedScoreClosestToM(memoryValue, secondaryValues);
                if (Double.compare(value2, -1) == 0) // there is no value
                    return memoryValue;
                else {
                    //return roundForOneOfTheValues(averageOfTheRest(memoryValue, secondaryValues, value2));
                    return value2;
                }
            } else {
                if (Double.compare(value3, memoryValue) == 0) {
                    return memoryValue;
                } else if (Double.compare(value3, memoryValue) > 0) {
                    if (isRestLessMoreThan(memoryValue, secondaryValues, value3, OPER_MORE_EQUAL_THAN, 3)) {
                        return memoryValue;
                    } else {
                        return value3;
                    }
                } else if (isRestLessMoreThan(memoryValue, secondaryValues, value3, OPER_LESS_EQUAL_THAN, 3)) {
                    return memoryValue;
                } else if (Double.compare(value3, 0) == 0) {
                    return 0.5;
                } else {
                    return value3;
                }
            }
        }
    }

    private static double roundForOneOfTheValues(double averageOfTheRest) {
        if (averageOfTheRest <= 0.25)
            return 0.0;

        if (averageOfTheRest <= 0.5)
            return 0.5;

        averageOfTheRest = Math.round(averageOfTheRest);

        if (averageOfTheRest == 1.0)
            return 1.0;

        if (averageOfTheRest == 2.0)
            return 2.0;

        if (averageOfTheRest == 3.0)
            return 3.0;

        return 3.0;
    }

    private static boolean isRestLessMoreThan(double memoryValue, LinkedHashMap<Double, Double> secondaryValues,
                                              double valueToExclude, int operator, int repetition) {
        for (Map.Entry<Double, Double> entry : secondaryValues.entrySet()) {
            if (entry.getValue() != 0 &&
                    (entry.getKey() != valueToExclude ||
                            (entry.getKey() == valueToExclude && Math.abs(entry.getValue() % repetition) >= 1)) &&
                    compare(entry.getKey(), memoryValue, operator))
                return false;
        }
        return true;
    }

    private static final int OPER_LESS_THAN = 0; // <
    private static final int OPER_LESS_EQUAL_THAN = 1; // <=
    private static final int OPER_MORE_THAN = 2; // >
    private static final int OPER_MORE_EQUAL_THAN = 3; // >=

    /**
     * Compares two values according with the operator({@link #OPER_LESS_THAN},
     * {@link #OPER_LESS_EQUAL_THAN}, {@link #OPER_MORE_THAN}, {@link #OPER_MORE_EQUAL_THAN}) provided.
     *
     * @param value1   The first value to compare.
     * @param value2   The second value to compare with.
     * @param operator The operator.
     * @return #value1 #operator #value2.
     */
    private static boolean compare(double value1, double value2, int operator) {
        boolean answer = false;
        switch (operator) {
            case OPER_LESS_THAN:
                answer = value1 < value2;
                break;
            case OPER_LESS_EQUAL_THAN:
                answer = value1 <= value2;
                break;
            case OPER_MORE_THAN:
                answer = value1 > value2;
                break;
            case OPER_MORE_EQUAL_THAN:
                answer = value1 >= value2;
                break;
        }
        return answer;
    }

    private static double averageOfTheRest(double memoryValue, LinkedHashMap<Double, Double> secondaryValues,
                                           double valueToExclude) {
        int sum = 0;
        for (Map.Entry<Double, Double> entry : secondaryValues.entrySet()) {
            if (entry.getKey() != valueToExclude) {
                sum += entry.getValue();
            }
        }
        return (sum + memoryValue) / (secondaryValues.entrySet().size() - 1);
    }

    private static double getValueThatRepeat(LinkedHashMap<Double, Double> secondaryValues, int repetitions) {
        for (Map.Entry<Double, Double> entry : secondaryValues.entrySet()) {
            if (entry.getValue() >= repetitions) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * With ties in the secondary categories on one side of M,
     * choose the tied scores closest to M for CDR (eg, M and another
     * secondary category = 3, two secondary categories = 2, and two
     * secondary categories = 1 ; CDR = 2).
     *
     * @param secondaryValues Secondary categories
     * @return Value closest to M
     */
    private static double getTiedScoreClosestToM(double memoryValue, LinkedHashMap<Double, Double> secondaryValues) {
        double[] pairedValues = new double[4];
        int pairedCount = 0;
        boolean otherValueAsM = false;
        for (Map.Entry<Double, Double> entry : secondaryValues.entrySet()) {
            if (entry.getKey() == memoryValue && entry.getValue() == 1) {
                otherValueAsM = true;
            } else if (entry.getValue() == 2) {
                pairedValues[pairedCount++] = entry.getKey();
            }
        }

        return (otherValueAsM && pairedCount == 2) ?
                (Math.abs(memoryValue - pairedValues[0]) ==
                        Math.min(Math.abs(memoryValue - pairedValues[0]),
                                Math.abs(memoryValue - pairedValues[1])) ? pairedValues[0] : pairedValues[1]) :
                -1;
    }

    private static boolean isAtLeastWithValue(LinkedHashMap<Double, Double> secondaryValues,
                                              double valueToCompareWith, int repetitions) {
        int count = 0;
        for (Map.Entry<Double, Double> entry : secondaryValues.entrySet()) {
            if (entry.getValue() >= valueToCompareWith) {
                count++;
                if (count >= repetitions)
                    return true;
            }
        }
        return false;
    }

    private static double repeatedCount(double scaleValue, double[] secondaryValues) {
        double count = 0;
        for (double value : secondaryValues) {
            if (value == scaleValue)
                count++;
        }

        return count;
    }

    /**
     * Gets the mode value and the number of times it is repeated.
     *
     * @param a The array of values.
     * @return In position 0 returns the mode number and in position 1 the number of times it is repeated.
     */
    private static double[] mode(double[] a) {
        double maxValue = 0, maxCount = 0;

        for (double v : a) {
            int count = 0;
            for (double value : a) {
                if (value == v) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = v;
            }
        }

        return new double[]{maxValue, maxCount};
    }

    /**
     * Gets the value on a predefined scale according to the answer position.
     *
     * @param context  The context of the Activity.
     * @param position Answers position.
     * @return scale value
     */
    public static double getScaleValue(Context context, int position) {
        String[] array = getScaleValue(context);
        if (position < 0 || position >= array.length) {
            return -1;
        }

        return Double.parseDouble(array[position]);
    }

    public static String[] getScaleValue(Context context) {
        return context.getResources().getStringArray(R.array.cdr_scale);
    }

    public static double evaluate(double value) {
        return value == 0.0 ? Constant.RESULT_OK : Constant.RESULT_NEGATIVE;
    }

    public static String getEvaluationText(Context context, double value) {
        if (value == 0.0) return context.getString(R.string.cdr_result_0);
        else if (value == 0.5) return context.getString(R.string.cdr_result_0_5);
        else if (value == 1.0) return context.getString(R.string.cdr_result_1);
        else if (value == 2.0) return context.getString(R.string.cdr_result_2);
        else return context.getString(R.string.cdr_result_3);
    }
}
