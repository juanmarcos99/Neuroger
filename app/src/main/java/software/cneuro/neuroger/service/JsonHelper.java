package software.cneuro.neuroger.service;

import static software.cneuro.neuroger.service.JsonNames.CARER_AGE;
import static software.cneuro.neuroger.service.JsonNames.CARER_FAMILIARITY;
import static software.cneuro.neuroger.service.JsonNames.CARER_GENDER;
import static software.cneuro.neuroger.service.JsonNames.CARER_LAST_NAME;
import static software.cneuro.neuroger.service.JsonNames.CARER_NAME;
import static software.cneuro.neuroger.service.JsonNames.CARER_PHONE;
import static software.cneuro.neuroger.service.JsonNames.CARER_WARNING_SIGNS_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_ANTECEDENTS;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_ANTECEDENTS_MEDICATION;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_CDR_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_FRAIL;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_GDS_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_HHIES_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_KATZ_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_LAWTON_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_MMSE;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_PFEIFFER_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_PHYSICAL_PERFORMANCE;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_PSYCHOAFFECTIVE_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_PSYCHOFAMILY_LIST;
import static software.cneuro.neuroger.service.JsonNames.EVALUATION_SUBJECTIVE;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_DISEASES;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_FATIGUE;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_RESISTANCE;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_WANDERING;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_WEIGHT_CURRENT;
import static software.cneuro.neuroger.service.JsonNames.FRAIL_WEIGHT_YEAR_AGO;
import static software.cneuro.neuroger.service.JsonNames.MMSE_DESIGN_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_LANGUAGE_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_MATH_ATTENTION_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_ORDER_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_REMINDER_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_RETENTION_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_SPATIAL_LIST;
import static software.cneuro.neuroger.service.JsonNames.MMSE_SPELLING;
import static software.cneuro.neuroger.service.JsonNames.MMSE_TEMPORAL_LIST;
import static software.cneuro.neuroger.service.JsonNames.PATIENTS_LIST;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_ADDRESS;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_BIRTH;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_CIVIL_STATUS;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_COEXISTENCE;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_COUNTRY;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_CREATION_DATE;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_EVALUATIONS_LIST;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_GENDER;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_GUID;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_IDENTIFICATION;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_INSTITUTION;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_LAST_NAME;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_MUNICIPALITY;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_NAME;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_OCCUPATION;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_ORIGIN;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_PHONE;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_PROFESSION;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_PROVINCE;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_SKIN_COLOR;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_VERSION;
import static software.cneuro.neuroger.service.JsonNames.PATIENT_YEARS_STUDY;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_GRIP_STRENGTH;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_1;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_2;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_1;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_2;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_IMC;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_IMC_SIZE;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_IMC_WEIGHT;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_MARCH;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_MARCH_STEPS;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_MARCH_TIME;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_SQUAD;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS_COUNT;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_SQUAD_TIME;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_STATIC_BALANCE;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_STATIC_BALANCE_PARALLEL;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_STATIC_BALANCE_SEMI_TANDEM;
import static software.cneuro.neuroger.service.JsonNames.PHYSICAL_PERFORMANCE_STATIC_BALANCE_TANDEM;
import static software.cneuro.neuroger.service.JsonNames.QUESTIONNAIRE_TEST_ANSWER_ID;
import static software.cneuro.neuroger.service.JsonNames.QUESTIONNAIRE_TEST_ANSWER_TEXT;
import static software.cneuro.neuroger.service.JsonNames.QUESTIONNAIRE_TEST_QUESTION_ID;
import static software.cneuro.neuroger.service.JsonNames.QUESTIONNAIRE_TEST_QUESTION_TEXT;
import static software.cneuro.neuroger.service.JsonNames.SCORE;
import static software.cneuro.neuroger.service.JsonNames.SUBJECTIVE_HEALTH_STATE;
import static software.cneuro.neuroger.service.JsonNames.SUBJECTIVE_INIT_IMPAIRMENT;
import static software.cneuro.neuroger.service.JsonNames.SUBJECTIVE_PAST_MEMORY;
import static software.cneuro.neuroger.service.JsonNames.SUBJECTIVE_PRESENT_DIFFICULTIES;
import static software.cneuro.neuroger.service.JsonNames.SUBJECTIVE_PRESENT_MEMORY;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by klaudia on 19/01/2016.
 */
public class JsonHelper {
    private static final String LOG_TAG = "JsonHelper";

    public static JSONObject getJsonPatientsToExport(JSONArray subjectsArray) {
        JSONObject obj = new JSONObject();

        try {
            obj.put(PATIENTS_LIST, subjectsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static JSONObject toJsonSubject(
            String name, String last_name,
            String identification,
            String guid,
            String birth,
            String occupation,
            String skinColor,
            String gender,
            String yearsStudy,
            String profession,
            String civilStatus,
            String coexistence,
            String hospital,
            String origin,
            String clinicClassification,
            String country,
            String province,
            String municipality,
            String address,
            String phone,
            String compensated,
            String patient_version,
            String creationDate,
            ArrayList<JSONObject> evaluations) {
        JSONObject subject = new JSONObject();

        try {
            subject.put(PATIENT_NAME, name);
            subject.put(PATIENT_LAST_NAME, last_name);
            subject.put(PATIENT_IDENTIFICATION, identification);
            subject.put(PATIENT_GUID, guid);
            subject.put(PATIENT_BIRTH, birth);
            subject.put(PATIENT_SKIN_COLOR, skinColor);
            subject.put(PATIENT_GENDER, gender);
            subject.put(PATIENT_YEARS_STUDY, yearsStudy);
            subject.put(PATIENT_OCCUPATION, occupation);
            subject.put(PATIENT_PROFESSION, profession);
            subject.put(PATIENT_CIVIL_STATUS, civilStatus);
            subject.put(PATIENT_COEXISTENCE, coexistence);
            subject.put(PATIENT_INSTITUTION, hospital);
            subject.put(PATIENT_ORIGIN, origin);
//            subject.put(PATIENT_CLINIC_CLASSIFICATION, clinicClassification);
            subject.put(PATIENT_COUNTRY, country);
            subject.put(PATIENT_PROVINCE, province);
            subject.put(PATIENT_MUNICIPALITY, municipality);
            subject.put(PATIENT_ADDRESS, address);
            subject.put(PATIENT_PHONE, phone);
//            subject.put(PATIENT_COMPENSATED, compensated);
            subject.put(PATIENT_VERSION, patient_version);
            subject.put(PATIENT_CREATION_DATE, creationDate);
            subject.put(PATIENT_EVALUATIONS_LIST, new JSONArray(evaluations));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return subject;
    }

    public static JSONObject toJsonCarer(
            String name, String lastName,
            String age,
            String gender,
            String familiarity,
            String phone,
            ArrayList<String> warningSigns,
            ArrayList<JSONObject> pfeiffer) {
        JSONObject subject = new JSONObject();

        try {
            subject.put(CARER_NAME, name);
            subject.put(CARER_LAST_NAME, lastName);
            subject.put(CARER_AGE, age);
            subject.put(CARER_GENDER, gender);
            subject.put(CARER_FAMILIARITY, familiarity);
            subject.put(CARER_PHONE, phone);

            subject.put(CARER_WARNING_SIGNS_LIST, new JSONArray(warningSigns));

            subject.put(EVALUATION_PFEIFFER_LIST, new JSONArray(pfeiffer));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return subject;
    }

    public static JSONObject toJsonEvaluation(JSONObject antecedents,
                                              JSONObject physicalPerformance,
                                              JSONObject subjective,
                                              JSONObject frail,
                                              JSONObject mmse,
                                              ArrayList<JSONObject> pfeiffer,
                                              ArrayList<JSONObject> cdr,
                                              ArrayList<JSONObject> gds,
                                              ArrayList<JSONObject> hhies,
                                              ArrayList<JSONObject> katz,
                                              ArrayList<JSONObject> lawton,
                                              ArrayList<JSONObject> family,
                                              ArrayList<JSONObject> affective) {
        JSONObject evaluation = new JSONObject();

        try {
            evaluation.put(EVALUATION_ANTECEDENTS, antecedents);
            evaluation.put(EVALUATION_PHYSICAL_PERFORMANCE, physicalPerformance);
            evaluation.put(EVALUATION_SUBJECTIVE, subjective);
            evaluation.put(EVALUATION_FRAIL, frail);
            evaluation.put(EVALUATION_MMSE, mmse);
            evaluation.put(EVALUATION_PFEIFFER_LIST, new JSONArray(pfeiffer));
            evaluation.put(EVALUATION_CDR_LIST, new JSONArray(cdr));
            evaluation.put(EVALUATION_GDS_LIST, new JSONArray(gds));
            evaluation.put(EVALUATION_HHIES_LIST, new JSONArray(hhies));
            evaluation.put(EVALUATION_KATZ_LIST, new JSONArray(katz));
            evaluation.put(EVALUATION_LAWTON_LIST, new JSONArray(lawton));
            evaluation.put(EVALUATION_PSYCHOFAMILY_LIST, new JSONArray(family));
            evaluation.put(EVALUATION_PSYCHOAFFECTIVE_LIST, new JSONArray(affective));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return evaluation;
    }

    public static JSONObject toJsonPathologicalAntecedents(ArrayList<String> antecedentsList, String medication) {
        JSONObject antecedents = new JSONObject();

        try {
            antecedents.put(EVALUATION_ANTECEDENTS, new JSONArray(antecedentsList));
            antecedents.put(EVALUATION_ANTECEDENTS_MEDICATION, medication);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return antecedents;
    }

    public static JSONObject toJsonPhysicalPerformance(
            double imc_weight, double imc_size,
            double march_time, int march_steps,
            int static_balance_parallel, int static_balance_semi_tandem, int static_balance_tandem,
            double squad_time, int squad_repetitions, int squad_repetitions_count,
            double grip_strength_left_hand_1, double grip_strength_left_hand_2,
            double grip_strength_right_hand_1, double grip_strength_right_hand_2,
            double score) {
        JSONObject physical_performance = new JSONObject();

        try {
            // create imc
            JSONObject imc = new JSONObject();
            imc.put(PHYSICAL_PERFORMANCE_IMC_WEIGHT, imc_weight);
            imc.put(PHYSICAL_PERFORMANCE_IMC_SIZE, imc_size);

            // create march
            JSONObject march = new JSONObject();
            march.put(PHYSICAL_PERFORMANCE_MARCH_TIME, march_time);
            march.put(PHYSICAL_PERFORMANCE_MARCH_STEPS, march_steps);

            // create static balance
            JSONObject static_balance = new JSONObject();
            static_balance.put(PHYSICAL_PERFORMANCE_STATIC_BALANCE_PARALLEL, static_balance_parallel);
            static_balance.put(PHYSICAL_PERFORMANCE_STATIC_BALANCE_SEMI_TANDEM, static_balance_semi_tandem);
            static_balance.put(PHYSICAL_PERFORMANCE_STATIC_BALANCE_TANDEM, static_balance_tandem);

            // create squad
            JSONObject squad = new JSONObject();
            squad.put(PHYSICAL_PERFORMANCE_SQUAD_TIME, squad_time);
            squad.put(PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS, squad_repetitions);
            squad.put(PHYSICAL_PERFORMANCE_SQUAD_REPETITIONS_COUNT, squad_repetitions_count);

            // create grip strength
            JSONObject grip_strength = new JSONObject();
            grip_strength.put(PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_1, grip_strength_left_hand_1);
            grip_strength.put(PHYSICAL_PERFORMANCE_GRIP_STRENGTH_LEFT_HAND_2, grip_strength_left_hand_2);
            grip_strength.put(PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_1, grip_strength_right_hand_1);
            grip_strength.put(PHYSICAL_PERFORMANCE_GRIP_STRENGTH_RIGHT_HAND_2, grip_strength_right_hand_2);

            physical_performance.put(PHYSICAL_PERFORMANCE_IMC, imc);
            physical_performance.put(PHYSICAL_PERFORMANCE_MARCH, march);
            physical_performance.put(PHYSICAL_PERFORMANCE_STATIC_BALANCE, static_balance);
            physical_performance.put(PHYSICAL_PERFORMANCE_SQUAD, squad);
            physical_performance.put(PHYSICAL_PERFORMANCE_GRIP_STRENGTH, grip_strength);

            physical_performance.put(SCORE, score);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return physical_performance;
    }

    public static JSONObject toJsonQuestionnaire(String questionText,
                                                 int questionId,
                                                 String answerText,
                                                 int answer) {
        JSONObject questionnaire = new JSONObject();

        try {
            questionnaire.put(QUESTIONNAIRE_TEST_QUESTION_ID, questionId);
            questionnaire.put(QUESTIONNAIRE_TEST_QUESTION_TEXT, questionText);
            questionnaire.put(QUESTIONNAIRE_TEST_ANSWER_ID, answer);
            questionnaire.put(QUESTIONNAIRE_TEST_ANSWER_TEXT, answerText);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return questionnaire;
    }

    public static JSONObject toJsonSubjective(String healthState,
                                              String memoryCurrent,
                                              String memoryPast,
                                              ArrayList<String> difficulties,
                                              String iniCognitiveImpairment,
                                              String courseOfDeterioration,
                                              String evolutionTime,
                                              double score) {
        JSONObject subjective = new JSONObject();

        try {
            subjective.put(SUBJECTIVE_HEALTH_STATE, healthState);
            subjective.put(SUBJECTIVE_PRESENT_MEMORY, memoryCurrent);
            subjective.put(SUBJECTIVE_PAST_MEMORY, memoryPast);
            subjective.put(SUBJECTIVE_PRESENT_DIFFICULTIES, new JSONArray(difficulties));
            subjective.put(SUBJECTIVE_INIT_IMPAIRMENT, iniCognitiveImpairment);
//            subjective.put(SUBJECTIVE_COURSE_OF_DETERIORATION, courseOfDeterioration);
//            subjective.put(SUBJECTIVE_EVOLUTION_TIME, evolutionTime);
            subjective.put(SCORE, score);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return subjective;
    }

    public static JSONObject toJsonFrail(int fatigue,
                                         int resistance,
                                         int wandering,
                                         ArrayList<String> diseases,
                                         int weightCurrent,
                                         int weightYearAgo,
                                         double score) {
        JSONObject frail = new JSONObject();

        try {
            frail.put(FRAIL_FATIGUE, fatigue);
            frail.put(FRAIL_RESISTANCE, resistance);
            frail.put(FRAIL_WANDERING, wandering);
            frail.put(FRAIL_DISEASES, new JSONArray(diseases));
            frail.put(FRAIL_WEIGHT_CURRENT, weightCurrent);
            frail.put(FRAIL_WEIGHT_YEAR_AGO, weightYearAgo);
            frail.put(SCORE, score);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return frail;
    }

    public static JSONObject toJsonMMSE(ArrayList<String> temporal,
                                        ArrayList<String> spatial,
                                        ArrayList<String> math, String spelling,
                                        ArrayList<String> retention,
                                        ArrayList<String> memory,
                                        ArrayList<String> language,
                                        ArrayList<String> order,
                                        ArrayList<String> design,
                                        double score) {
        JSONObject mmse = new JSONObject();

        try {
            mmse.put(MMSE_TEMPORAL_LIST, new JSONArray(temporal));
            mmse.put(MMSE_SPATIAL_LIST, new JSONArray(spatial));
            mmse.put(MMSE_MATH_ATTENTION_LIST, new JSONArray(math));
            mmse.put(MMSE_SPELLING, spelling);
            mmse.put(MMSE_RETENTION_LIST, new JSONArray(retention));
            mmse.put(MMSE_REMINDER_LIST, new JSONArray(memory));
            mmse.put(MMSE_LANGUAGE_LIST, new JSONArray(language));
            mmse.put(MMSE_ORDER_LIST, new JSONArray(order));
            mmse.put(MMSE_DESIGN_LIST, new JSONArray(design));
            mmse.put(SCORE, score);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return mmse;
    }

    public static JSONObject toJsonScore(double score) {
        JSONObject scoreJ = new JSONObject();

        try {
            scoreJ.put(SCORE, score);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return scoreJ;
    }
}
