package software.cneuro.neuroger.service;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import software.cneuro.neuroger.R;

public class JsonNames {
    public static String SCORE = "score";
    public static String EVALUATION_RESULT = "evaluation";

    public static String PATIENTS_LIST = "patients";
    public static String PATIENT_NAME = "name";
    public static String PATIENT_LAST_NAME = "lastName";
    public static String PATIENT_IDENTIFICATION = "ci";
    public static String PATIENT_GUID = "guid";
    public static String PATIENT_BIRTH = "birth";
    public static String PATIENT_SKIN_COLOR = "skinColor";
    public static String PATIENT_GENDER = "gender";
    public static String PATIENT_YEARS_STUDY = "yearsStudy";
    public static String PATIENT_OCCUPATION = "occupation";
    public static String PATIENT_PROFESSION = "profession";
    public static String PATIENT_CIVIL_STATUS = "civilStatus";
    public static String PATIENT_COEXISTENCE = "coexistence";
    public static String PATIENT_INSTITUTION = "institution";
    public static String PATIENT_COUNTRY = "country";
    public static String PATIENT_PROVINCE = "province";
    public static String PATIENT_MUNICIPALITY = "municipality";
    public static String PATIENT_ADDRESS = "address";
    public static String PATIENT_PHONE = "phone";
    public static String PATIENT_COMPENSATED = "compensated";
    public static String PATIENT_ORIGIN = "origin";
    public static String PATIENT_CLINIC_CLASSIFICATION = "clinicClassification";
    public static String PATIENT_VERSION = "version";
    public static String PATIENT_CREATION_DATE = "creationDate";

    public static String PATIENT_CARER = "carer";
    public static String CARER_NAME = "name";
    public static String CARER_LAST_NAME = "lastName";
    public static String CARER_AGE = "age";
    public static String CARER_GENDER = "gender";
    public static String CARER_FAMILIARITY = "familiarity";
    public static String CARER_PHONE = "phone";

    public static String PATIENT_EVALUATIONS_LIST = "evaluations";

    public static String EVALUATION_ANTECEDENTS = "antecedents";
    public static String ANTECEDENT = "antecedent";
    public static String EVALUATION_ANTECEDENTS_MEDICATION = "medications";

    public static String CARER_WARNING_SIGNS_LIST = "warningSigns";
    public static String SIGN = "sign";

    public static String EVALUATION_PHYSICAL_PERFORMANCE = "physicalPerformance";
    public static String PHYSICAL_PERFORMANCE_IMC = "imc";
    public static String PHYSICAL_PERFORMANCE_IMC_WEIGHT = "weight";
    public static String PHYSICAL_PERFORMANCE_IMC_SIZE = "size";
    public static String PHYSICAL_PERFORMANCE_MARCH = "march";
    public static String PHYSICAL_PERFORMANCE_MARCH_TIME = "time";
    public static String PHYSICAL_PERFORMANCE_MARCH_STEPS = "steps";
    public static String PHYSICAL_PERFORMANCE_STATIC_BALANCE = "static_balance";
    public static String PHYSICAL_PERFORMANCE_STATIC_BALANCE_PARALLEL = "parallel";
    public static String PHYSICAL_PERFORMANCE_STATIC_BALANCE_SEMI_TANDEM = "semiTandem";
    public static String PHYSICAL_PERFORMANCE_STATIC_BALANCE_TANDEM = "tandem";
    public static String PHYSICAL_PERFORMANCE_SQUAD = "squad";
    public static String PHYSICAL_PERFORMANCE_SQUAD_TIME = "time";
    public static String PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS = "repetitions";
    public static String PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS_COUNT = "repetitionsCount";
    public static String PHYSICAL_PERFORMANCE_GRIP_STRENGTH = "gripStrength";
    public static String PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_1 = "leftHand1";
    public static String PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_1 = "rightHand1";
    public static String PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_2 = "leftHand2";
    public static String PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_2 = "rightHand2";
    public static String PHYSICAL_GENERAL_SCORE = "general score";
    public static String PHYSICAL_GENERAL_RESULT = "general evaluation";

    public static String EVALUATION_CDR_LIST = "cdr";
    public static String EVALUATION_GDS_LIST = "dgr";
    public static String EVALUATION_HHIES_LIST = "hhies";
    public static String EVALUATION_PFEIFFER_LIST = "pfeiffer";
    public static String EVALUATION_KATZ_LIST = "katz";
    public static String EVALUATION_LAWTON_LIST = "lawton";
    public static String EVALUATION_PSYCHOFAMILY_LIST = "psychofamily";
    public static String EVALUATION_PSYCHOAFFECTIVE_LIST = "psychoaffective";

    public static String QUESTIONNAIRE_TEST_QUESTION_ID = "questionId";
    public static String QUESTIONNAIRE_TEST_QUESTION_TEXT = "questionText";
    public static String QUESTIONNAIRE_TEST_ANSWER_ID = "answerId";
    public static String QUESTIONNAIRE_TEST_ANSWER_TEXT = "answerText";

    public static String EVALUATION_SUBJECTIVE = "subjective";
    public static String SUBJECTIVE_HEALTH_STATE = "healthState";
    public static String SUBJECTIVE_PRESENT_MEMORY = "currentMemory";
    public static String SUBJECTIVE_PAST_MEMORY = "pastMemory";
    public static String SUBJECTIVE_PRESENT_DIFFICULTIES = "difficulties";
    public static String SUBJECTIVE_INIT_IMPAIRMENT = "initCognitiveImpairment";
    public static String SUBJECTIVE_COURSE_OF_DETERIORATION = "courseOfDeterioration";
    public static String SUBJECTIVE_EVOLUTION_TIME = "evolutionTime";

    public static String EVALUATION_FRAIL = "frail";
    public static String FRAIL_FATIGUE = "fatigue";
    public static String FRAIL_RESISTANCE = "resistance";
    public static String FRAIL_WANDERING = "wandering";
    public static String FRAIL_DISEASES = "diseases";
    public static String FRAIL_WEIGHT_CURRENT = "weightCurrent";
    public static String FRAIL_WEIGHT_YEAR_AGO = "weight1yearAgo";

    public static String EVALUATION_MMSE = "mmse";
    public static String MMSE_TEMPORAL_LIST = "temporal";
    public static String MMSE_SPATIAL_LIST = "spatial";
    public static String MMSE_RETENTION_LIST = "retention";
    public static String MMSE_MATH_ATTENTION_LIST = "math";
    public static String MMSE_SPELLING = "spelling";
    public static String MMSE_REMINDER_LIST = "reminder";
    public static String MMSE_LANGUAGE_LIST = "language";
    public static String MMSE_ORDER_LIST = "order";
    public static String MMSE_DESIGN_LIST = "design";

    public static String EVALUATIONS_LIST = "evaluations";

    public static String EVALUATION_ANTECEDENT = "antecedents";
    public static String ANTECEDENTS_LIST = "antecedents";
    public static String MEDICATION = "medications";

    public static List<String> getPatientsValues(Context context) {
        return Arrays.asList(
                context.getString(R.string.label_name),
                context.getString(R.string.label_lastName),
                context.getString(R.string.label_ci),
                context.getString(R.string.label_age),
                context.getString(R.string.label_birth),
                context.getString(R.string.label_skin_color),
                context.getString(R.string.label_gender),
                context.getString(R.string.label_study_years),
                context.getString(R.string.label_occupation),
                context.getString(R.string.label_profession),
                context.getString(R.string.label_civil_status),
                context.getString(R.string.label_coexistence),
                context.getString(R.string.label_hospital),
                context.getString(R.string.label_origin),
                context.getString(R.string.label_country),
                context.getString(R.string.label_province),
                context.getString(R.string.label_municipality),
                context.getString(R.string.label_address),
                context.getString(R.string.label_phone),
                context.getString(R.string.label_creation_date)
        );
    }
}
