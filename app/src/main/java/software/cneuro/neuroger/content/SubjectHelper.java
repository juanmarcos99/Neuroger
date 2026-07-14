package software.cneuro.neuroger.content;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 22/09/2016.
 */

public class SubjectHelper {
    private static final int STUDY_YEARS_DEFAULT_VALUE = 41;

    /**
     * UUID
     *
     * @return A randomly generated UUID
     */
    public static String getGuid() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Gets the name + lastName.
     *
     * @param name Name
     * @param lastName LastName
     * @param withEnter \n
     * @return If withEnter is true returns the name with a \n before the lastName in case there is a lastName, the name only otherwise.
     * Else if withEnter is false returns the name with a space before the lastname in case there is a lastName, the name only otherwise.
     */
    public static String getName(String name, String lastName, boolean withEnter) {
        return (TextUtils.isEmpty(lastName) ?
                name :
                String.format(withEnter ? "%s\n%s" : "%s %s", name, lastName));
    }

    /**
     * Gets the age corresponding to birthDate.
     *
     * @param context   Activity
     * @param birthDate the date of birth.
     * @return the age + the text "years old".
     */
    public static String getAge(Context context, Date birthDate) {
        int age = CalendarHelper.getAge(birthDate);

        return String.format("%s %s", age, context.getResources().getString(R.string.label_age_suffix));
    }

    /**
     * Gets the age corresponding to birthDate.
     *
     * @param context   Activity
     * @param birthDate the string date of birth.
     * @return the age + the text "years old".
     */
    public static String getAge(Context context, String birthDate) {
        int age = CalendarHelper.getAge(birthDate);

        return String.format("%s %s", age, context.getResources().getString(R.string.label_age_suffix));
    }

    /**
     * Gets the gender text base on the id.
     *
     * @param context Activity
     * @param id      to match with the Constant.SUBJECT_FEMALE_ID or the Constant.SUBJECT_MALE_ID
     * @return Female or Male.
     */
    public static String getGenderText(Context context, int id) {
        return id == Constant.SUBJECT_FEMALE_ID ? context.getResources().getString(R.string.rdb_gender_female) :
                context.getResources().getString(R.string.rdb_gender_male);
    }

    /**
     * Gets the gender text base on the id.
     *
     * @param context Activity
     * @param id      to match with the Constant.SUBJECT_COMPENSATED_ID or the Constant.SUBJECT_NOT_COMPENSATED_ID
     * @return compensated or not.
     */
    public static String getCompensatedText(Context context, int id) {
        return id == Constant.SUBJECT_COMPENSATED_ID ? context.getResources().getString(R.string.compensated) :
                context.getResources().getString(R.string.not_compensated);
    }

    public static boolean matchWithBirth(Date birth, String ci) {
        return CalendarHelper.getDateToCompareWithCi(birth).compareToIgnoreCase(ci.substring(0, 6)) == 0;
    }

    public static String getCivilStatusText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.civil_state);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getFamiliarityText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.familiarity);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getCountryText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.country);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getProvinceText(Context context, int countryPos, int position) {
        String[] array = getProvince(context, countryPos);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String[] getProvince(Context context, int countryPos) {
        String[] array = context.getResources().getStringArray(R.array.country);
        if (countryPos < 0 || countryPos >= array.length) {
            return new String[]{};
        }

        array = new String[]{};
        if (countryPos == 0) {
            array = context.getResources().getStringArray(R.array.cuba_provinces);
        }
        if (countryPos == 1) {
            array = context.getResources().getStringArray(R.array.mexico_provinces);
        }

        return array;
    }

    public static String getMunicipalityText(Context context, int countryPos, int provincePos, int position) {
        String[] array = getMunicipality(context, countryPos, provincePos);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String[] getMunicipality(Context context, int countryPos, int provincePos) {
        String[] array = getProvince(context, countryPos);
        if (provincePos < 0 || provincePos >= array.length) {
            return new String[]{};
        }

        array = new String[]{};
        if (countryPos == 0) {
            if (provincePos == 0) {
                array = context.getResources().getStringArray(R.array.havana_municipalities);
            } else if (provincePos == 1) {
                array = context.getResources().getStringArray(R.array.matanzas_municipalities);
            }
        } else if (countryPos == 1) {
            if (provincePos == 10) {
                array = context.getResources().getStringArray(R.array.mexico_state_municipalities);
            }
        }

        return array;
    }

    public static String[] getPathologicalAntecedents(Context context) {
        return context.getResources().getStringArray(R.array.pathological_antecedents);
    }

    public static String getPathologicalAntecedentsText(Context context, int position) {
        String[] array = getPathologicalAntecedents(context);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getOccupationText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.occupation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getSkinColorText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.skin_color);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getCoexistenceText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.coexistence);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHospitalText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.hospitals);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getOriginText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.origin);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getClassificationText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.clinic_classification);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static int getHospitalPosition(Context context, String hospital) {
        String[] array = context.getResources().getStringArray(R.array.hospitals);

        for (int i = 0; i < array.length; i++) {
            if (hospital.equalsIgnoreCase(array[i])) {
                return i;
            }
        }

        return -1;
    }

    public static String[] getStudyYears() {
        String[] result = new String[STUDY_YEARS_DEFAULT_VALUE];

        for (int i = 0; i < STUDY_YEARS_DEFAULT_VALUE; i++)
            result[i] = String.valueOf(i);

        return result;
    }

    public static String getLevelOfSchoolingText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.level_of_schooling);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position].split("\\(")[0];
    }
}
