package software.cneuro.neuroger.constant;

/**
 * Created by klaudia on 7/31/2015.
 */
public class Constant {

    public static final String[] devicesIds = new String[]
            {
                    "407dce0c19a3b90c",
                    "e5ef9a34dd619c30",
                    "20f42bd874c30e9d",
                    "97a317c42d318d32",
                    "3e8d27a3902b1b7b",
                    "75e81d0174b9d8fe",
                    "4caf251ecef9b5d3",
                    "6930c94f6ce359e"
            };

    public static final long NO_ID = Long.MIN_VALUE;
    public static final String NO_DATA = "-";

    public static final int SUBJECT_MALE_ID = 0;
    public static final int SUBJECT_FEMALE_ID = 1;

    public static final int SUBJECT_COMPENSATED_ID = 0;
    public static final int SUBJECT_NO_COMPENSATED_ID = 1;

    public static final int SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID = 1;
    public static final int SUBJECT_NO_TAKES_MORE_THAN_5_MEDICAMENT_ID = 0;

    public static final int FRAIL_DISEASES_ANSWER_WAS_NO = -1;

    public static final int NO_EVALUATED = Integer.MIN_VALUE;

    // SUBJECT GENERAL DETAIL
    public static final String CARD_DETAIL_SUBJECT = "card_detail_subject";
    public static final String CARD_DETAIL_CARER = "card_detail_carer";
    public static final String CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS = "card_detail_medical";
    public static final String CARD_DETAIL_PHYSICAL_EVALUATION = "card_detail_physical_evaluation";
    public static final String CARD_DETAIL_SUBJECTIVE_EVALUATION = "card_detail_subjective_evaluation";
    public static final String CARD_DETAIL_MINI_MENTAL_STATE = "card_detail_mini_mental_evaluation";
    public static final String CARD_DETAIL_CDR = "card_detail_cdr_evaluation";
    public static final String CARD_DETAIL_PFEIFFER = "card_detail_pfeiffer_evaluation";
    public static final String CARD_DETAIL_WARNING_SIGNS_STATE = "card_detail_alert_signs_evaluation";
    public static final String CARD_DETAIL_GDS = "card_detail_geriatric_depression_scale_evaluation";
    public static final String CARD_DETAIL_HHIES = "card_detail_hhies_evaluation";

    public static final String CARD_DETAIL_KATZ_INDEX = "card_detail_katz_index_evaluation";
    public static final String CARD_DETAIL_FRAIL_SCALE = "card_detail_frail_scale_evaluation";
    public static final String CARD_DETAIL_LAWTON_MODIFIED_SCALE = "card_detail_lawton_modified_scale_evaluation";
    public static final String CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE = "card_detail_psychofamily_assessment_scale_evaluation";
    public static final String CARD_DETAIL_PSYCHOAFFECTIVE_SCALE = "card_detail_psychoaffective_scale_evaluation";

    public static final String CARD_DETAIL_CLASSIFICATION = "card_classification";
    public static final String CARD_DETAIL_COGNITIVE_PERFORMANCE = "card_cognitive_performance";

    // PHYSICAL EVALUATION HELP
    public static final int HELP_IMC = 0;
    public static final int HELP_MARCH = 2;
    public static final int HELP_STATIC_BALANCE = 3;
    public static final int HELP_SQUAD = 4;
    public static final int HELP_GRIP_STRENGTH = 5;

    // PHYSICAL EVALUATION RESULTS
    public static final int RESULT_OK = 0;
    public static final int RESULT_NEGATIVE = 1;
    public static final int RESULT_REGULAR = 2;

    // PHYSICAL EVALUATION IMC RESULTS
    public static final int RESULT_MALNOURISHED = 0;
    public static final int RESULT_OBESE = 1;
    public static final int RESULT_NORMO_WEIGHT = 2;
    public static final int RESULT_OVERWEIGHT = 3;

    // PHYSICAL EVALUATION CHECKED
    public static final int RESULT_NOT_CHECKED = -1;
    public static final int RESULT_CHECKED = 1;

    // SUBJECTIVE EVALUATION HELP
    // CHECKBOXES HELP
    public static final int HELP_SV_1 = 0;
    public static final int HELP_SV_2 = 2;
    public static final int HELP_SV_3 = 3;
    public static final int HELP_SV_4 = 4;
    public static final int HELP_SV_5 = 5;

    // RADIO BUTTON HELP
    public static final int HELP_SV_6 = 6;
    public static final int HELP_SV_7 = 7;
    public static final int HELP_SV_8 = 8;
    public static final int HELP_SV_9 = 9;
    public static final int HELP_SV_10 = 10;
    public static final int HELP_SV_11 = 11;

    // PHYSICAL EVALUATION MAX SQUADS REPETITIONS
    public static final int MAX_SQUADS_REPETITIONS = 5;

    // MINI EVALUATION RESULTS
    public static final int RESULT_CI_WITH = 4;

    // MINI MENTAL TEST
    public static final int MATH_COUNTING_TEST = 0;
    public static final int MATH_BACKWARDS_SPELLING_TEST = 1;

    // MINI MENTAL HELP
    public static final int HELP_ORIENTATION_TEMPORAL = 0;
    public static final int HELP_ORIENTATION_SPATIAL = 1;
    public static final int HELP_RETENTION = 2;
    public static final int HELP_MATH_ATTENTION = 3;
    public static final int HELP_REMINDER = 4;
    public static final int HELP_DESIGN = 5;

    public static final int HELP_LANGUAGE_1 = 6;
    public static final int HELP_LANGUAGE_2 = 7;
    public static final int HELP_LANGUAGE_3 = 8;
    public static final int HELP_LANGUAGE_4 = 9;
    public static final int HELP_LANGUAGE_5 = 10;
    public static final int HELP_ORDER = 11;

    // QUESTIONNAIRES
    public static final int QUESTIONNAIRE_CDR = 12;
    public static final int QUESTIONNAIRE_PFEIFFER = 13;
    public static final int QUESTIONNAIRE_GDS = 14;
    public static final int QUESTIONNAIRE_HHIES = 15;

    public static final int QUESTIONNAIRE_KATZ = 16;
    public static final int QUESTIONNAIRE_LAWTON = 17;

    public static final int QUESTIONNAIRE_PSYCHOFAMILY = 18;
    public static final int QUESTIONNAIRE_PSYCHOAFFECTIVE = 19;
}
