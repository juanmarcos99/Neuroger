package software.cneuro.neurogerdatabase.constant;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
//import androidx.collection.LongSparseArray;
import androidx.collection.LongSparseArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import software.cneuro.neurogerdatabase.database_async.DeletePathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.DeletePatient_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.SearchCohabitant_AsyncTask;

/**
 * Created by exel.rodriguez on 03/06/2015.
 */
public class Constant {

    public static final String AUTHORITY = "software.cneuro.neuroger.provider";

    public static final String DATABASE_NAME = "NeurogerMCI_DB.db";
    public static final String DATABASE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "NeuroGerPesquisa";
    public static final String DATABASE_FULL_NAME_PATH = DATABASE_PATH
            + File.separator + DATABASE_NAME;
    // region definicion de tablas y columnas


    //TABLA PACIENTE
    public static final String TABLE_PATIENT = "patient";
    public static final String COL_PATIENT_ID = "_id";
    public static final String COL_PATIENT_GUID = "_guid";
    public static final String COL_PATIENT_NAME = "name";
    public static final String COL_PATIENT_LASTNAME = "lastname";
    public static final String COL_PATIENT_ID_NUMBER = "id_number";
    public static final String COL_PATIENT_BIRTHDATE = "birthdate";
    public static final String COL_PATIENT_SEX = "sex";
    public static final String COL_PATIENT_YEARS_STUDIES = "years_studies";
    public static final String COL_PATIENT_PROFESSION = "profession";
    public static final String COL_PATIENT_CIVIL_STATUS = "civil_status";
    public static final String COL_PATIENT_HOSPITAL = "hospital";
    public static final String COL_PATIENT_ORIGIN = "origin";
    public static final String COL_PATIENT_CLINIC_CLASSIFICATION = "clinic_classification";
    public static final String COL_PATIENT_COUNTRY = "country";
    public static final String COL_PATIENT_PROVINCE_STATE = "province_state";
    public static final String COL_PATIENT_MUNICIPALITY = "municipality";
    public static final String COL_PATIENT_ADDRESS = "address";
    public static final String COL_PATIENT_SKIN_COLOR = "skin_color";
    public static final String COL_PATIENT_OCCUPATION = "occupation";
    public static final String COL_PATIENT_COEXISTENCE = "coexistence";
    public static final String COL_PATIENT_PHONE_NUMBER = "phone_number";
    public static final String COL_PATIENT_INSERT_DATE = "ins_date";
    public static final String COL_PATIENT_VERSION = "version";
    public static final String COL_PATIENT_FULLNAME = "full_name";

    public static final String COL_PATIENT_COMPENSATED = "compensated";

    // endregion


    //TABLA CUIDADOR
    public static final String TABLE_COHABITANT = "cohabitant";
    public static final String COL_COHABITANT_ID = "_id";
    public static final String COL_COHABITANT_NAME = "name";
    public static final String COL_COHABITANT_LASTNAME = "lastname";
    public static final String COL_COHABITANT_AGE = "age";
    public static final String COL_COHABITANT_SEX = "sex";
    public static final String COL_COHABITANT_FAMILIARITY = "familiarity";
    public static final String COL_COHABITANT_PHONE_NUMBER = "phone_number";
    public static final String COL_COHABITANT_PATIENT_ID = "patient_id";
    public static final String COL_COHABITANT_FULLNAME = "fullname";

    // endregion


    //TABLA ANTECEDENTES PATOLOGICOS
    public static final String TABLE_PATHOLOGICAL_ANTECEDENTS = "pathological_antecedents";
    public static final String COL_PATHOLOGICAL_ANTECEDENTS_ID = "id_pathological_antecedents";
    public static final String COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID = "id_patient";  //paciente
    public static final String COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID = "id_check_pathological_ant";  //ID del checkbox
    public static final String COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY = "medication_quantity";
    public static final String COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION = "evaluation";

    // endregion


    //TABLA PRUEBA VALORACION SUBJETIVA DEL PACIENTE
    public static final String TABLE_SUBJECTIVE_EVALUATION_TEST = "sub_evaluation_test";
    public static final String COL_SE_TEST_ID = "_id";
    public static final String COL_SE_TEST_PATIENT_ID = "patient_id";
    public static final String COL_SE_TEST_HEALTH_STATE = "question_0";
    public static final String COL_SE_TEST_MEMORY_CURRENT = "question_1a";
    public static final String COL_SE_TEST_MEMORY_PAST = "question_1b";
    public static final String COL_SE_TEST_DIFFICULTIES = "question_2"; //ID del checkbox
    public static final String COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT = "question_3";
    public static final String COL_SE_TEST_COURSE_OF_DETERIORATION = "question_4";
    public static final String COL_SE_TEST_EVOLUTION_TIME = "question_5";
    public static final String COL_SE_TEST_EVALUATION = "evaluation";
    // endregion


    //TABLA PRUEBA DE RESISTENCIA FISICA
    public static final String TABLE_PRUEBA_RF = "prueba_rf";
    public static final String COL_PRUEBA_RF_ID = "_id_prueba";
    public static final String COL_PRUEBA_RF_PACIENTE_ID = "paciente_id";
    public static final String COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS = "m_tiempo_segundos";
    public static final String COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS = "m_cantidad_pasos";
    public static final String COL_PRUEBA_RF_EE_PIES_PARALELOS = "ee_pies_paralelos";
    public static final String COL_PRUEBA_RF_EE_SEMI_TANDEM = "ee_semi_tandem";
    public static final String COL_PRUEBA_RF_EE_TANDEM = "ee_tandem";
    public static final String COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS = "l_tiempo_segundos";
    public static final String COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS = "l_lograr_levantadas";
    public static final String COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS = "l_numero_levantadas";
    public static final String COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1 = "fa_fuerza_mano_derecha1";
    public static final String COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1 = "fa_fuerza_mano_izquierda1";
    public static final String COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2 = "fa_fuerza_mano_derecha2";
    public static final String COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2 = "fa_fuerza_mano_izquierda2";
    public static final String COL_PRUEBA_RF_MC_PESO = "mc_peso";
    public static final String COL_PRUEBA_RF_MC_TALLA = "mc_talla";
    public static final String COL_PRUEBA_RF_EVALUACION_GENERAL = "evaluacion_general";
    public static final String COL_PRUEBA_RF_PUNTAJE_GENERAL = "puntaje_general";

    // endregion


    //TABLA PRUEBA CDR
    public static final String TABLE_CDR_TEST = "cdr_test";
    public static final String COL_CDR_TEST_ID = "_id";
    public static final String COL_CDR_TEST_PATIENT_ID = "patient_id";
    public static final String COL_CDR_TEST_MEMORY = "memory";
    public static final String COL_CDR_TEST_ORIENTATION = "orientation";
    public static final String COL_CDR_TEST_JUDGEMENT = "judgement";
    public static final String COL_CDR_TEST_COMMUNITY = "community";
    public static final String COL_CDR_TEST_HOBBIES = "hobbies";
    public static final String COL_CDR_TEST_PERSONAL_CARE = "personal_care";
    public static final String COL_CDR_TEST_EVALUATION = "evaluation";

    // endregion


    //TABLA PRUEBA PFEIFFER
    public static final String TABLE_PFEIFFER_TEST = "pfeiffer_test";
    public static final String COL_PFEIFFER_TEST_ID = "_id";
    public static final String COL_PFEIFFER_TEST_PATIENT_ID = "patient_id";
    public static final String COL_PFEIFFER_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_PFEIFFER_TEST_EVALUATION = "evaluation";

    // endregion

    //TABLA SIGNOS DE ALERTA
    public static final String TABLE_ALERT_SIGNS = "alert_signs";
    public static final String COL_ALERT_SIGNS_ID = "id_alert_signs";
    public static final String COL_ALERT_SIGNS_COHABITANT_ID = "cohabitant_id";  //cuidador
    public static final String COL_ALERT_SIGNS_ANSWERS_LIST = "answers_list_positions";  //ID del checkbox
    public static final String COL_ALERT_SIGNS_EVALUATION = "evaluation";

    // endregion

    //TABLA PRUEBA ESCALA GERIATRICA DE DEPRESION
    public static final String TABLE_DEPRESSION_TEST = "depression_test";
    public static final String COL_DEPRESSION_TEST_ID = "_id";
    public static final String COL_DEPRESSION_TEST_PATIENT_ID = "patient_id";
    public static final String COL_DEPRESSION_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_DEPRESSION_TEST_EVALUATION = "evaluation";

    //endregion

    //TABLA PRUEBA HHIE-S
    public static final String TABLE_HHIES_TEST = "hhies_test";
    public static final String COL_HHIES_TEST_ID = "_id";
    public static final String COL_HHIES_TEST_PATIENT_ID = "patient_id";
    public static final String COL_HHIES_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_HHIES_TEST_EVALUATION = "evaluation";

    //TABLE KATZ
    public static final String TABLE_KATZ_TEST = "katz_test";
    public static final String COL_KATZ_TEST_ID = "_id";
    public static final String COL_KATZ_TEST_PATIENT_ID = "patient_id";
    public static final String COL_KATZ_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_KATZ_TEST_EVALUATION = "evaluation";

    //TABLE LAWTON
    public static final String TABLE_LAWTON_TEST = "lawton_test";
    public static final String COL_LAWTON_TEST_ID = "_id";
    public static final String COL_LAWTON_TEST_PATIENT_ID = "patient_id";
    public static final String COL_LAWTON_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_LAWTON_TEST_EVALUATION = "evaluation";

    //TABLE FRAIL
    public static final String TABLE_FRAIL_TEST = "frail_test";
    public static final String COL_FRAIL_TEST_ID = "_id";
    public static final String COL_FRAIL_TEST_PATIENT_ID = "patient_id";
    public static final String COL_FRAIL_TEST_FATIGUE = "fatigue";
    public static final String COL_FRAIL_TEST_RESISTANCE = "resistance"; //position of the checkbox in res/arrays
    public static final String COL_FRAIL_TEST_WANDERING = "wandering";
    public static final String COL_FRAIL_TEST_DISEASES = "diseases";
    public static final String COL_FRAIL_TEST_WEIGHT_CURRENT = "weight_current";
    public static final String COL_FRAIL_TEST_WEIGHT_YEAR_AGO = "weight_year_ago";
    public static final String COL_FRAIL_TEST_EVALUATION = "evaluation";

    //TABLE CLASSIFICATION
    public static final String TABLE_CLASSIFICATION = "classification";
    public static final String COL_CLASSIFICATION_ID = "_id";
    public static final String COL_CLASSIFICATION_PATIENT_ID = "patient_id";
    public static final String COL_CLASSIFICATION_ANTECEDENTS_SCORE = "antecedents_count";
    public static final String COL_CLASSIFICATION_MEDICATION_QUANTITY = "medication_quantity";
    public static final String COL_CLASSIFICATION_SUBJECTIVE_EVALUATION = "subjective_evaluation";
    public static final String COL_CLASSIFICATION_KATZ_SCORE = "katz_evaluation";
    public static final String COL_CLASSIFICATION_FRAIL_SCORE = "frail_evaluation";
    public static final String COL_CLASSIFICATION_LAWTON_SCORE = "lawton_evaluation";
    public static final String COL_CLASSIFICATION_PSYCHOFAMILY_SCORE = "pas_evaluation";
    public static final String COL_CLASSIFICATION_PSYCHOAFFECTIVE_SCORE = "paffective_evaluation";
    public static final String COL_CLASSIFICATION_EVALUATION = "evaluation";

    //TABLE COGNITIVE
    public static final String TABLE_COGNITIVE = "cognitive";
    public static final String COL_COGNITIVE_ID = "_id";
    public static final String COL_COGNITIVE_PATIENT_ID = "patient_id";
    public static final String COL_COGNITIVE_CDR = "cdr_evaluation";
    public static final String COL_COGNITIVE_MMSE = "mmse_evaluation";
    public static final String COL_COGNITIVE_GDS = "gds_evaluation";
    public static final String COL_COGNITIVE_HHIES = "hhies_evaluation";
    public static final String COL_COGNITIVE_PFEIFFER = "pfeiffer_evaluation";
    public static final String COL_COGNITIVE_EVALUATION = "evaluation";

    //TABLE MINIMENTAL
    public static final String TABLE_MINIMENTAL_TEST = "minimental_test";
    public static final String COL_MINIMENTAL_TEST_ID = "_id";
    public static final String COL_MINIMENTAL_TEST_PATIENT_ID = "patient_id";
    public static final String COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION = "temporal_orientation";
    public static final String COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION = "spatial_orientation";
    public static final String COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION = "retention_register_information";
    public static final String COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG = "math_attention_flag";
    public static final String COL_MINIMENTAL_TEST_MATH_ATTENTION = "math_attention";
    public static final String COL_MINIMENTAL_TEST_REMEMBERING = "remembering";
    public static final String COL_MINIMENTAL_TEST_LANGUAGE = "language";
    public static final String COL_MINIMENTAL_TEST_ORDER_3 = "order_3";
    public static final String COL_MINIMENTAL_TEST_DESIGN = "design";
    public static final String COL_MINIMENTAL_EVALUATION = "evaluation";

    //TABLE PSYCHOFAMILY
    public static final String TABLE_PSYCHOFAMILY_TEST = "psychofamily_test";
    public static final String COL_PSYCHOFAMILY_TEST_ID = "_id";
    public static final String COL_PSYCHOFAMILY_TEST_PATIENT_ID = "patient_id";
    public static final String COL_PSYCHOFAMILY_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_PSYCHOFAMILY_TEST_EVALUATION = "evaluation";

    //TABLE PSYCHOAFFECTIVE
    public static final String TABLE_PSYCHOAFFECTIVE_TEST = "psychoaffective_test";
    public static final String COL_PSYCHOAFFECTIVE_TEST_ID = "_id";
    public static final String COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID = "patient_id";
    public static final String COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST = "answers_list_positions";
    public static final String COL_PSYCHOAFFECTIVE_TEST_EVALUATION = "evaluation";
    // endregion

    // region uris

    public static final Uri URI_TABLE_PATIENT = Uri.parse("content://" + AUTHORITY
            + "/" + TABLE_PATIENT);

    public static final Uri URI_TABLE_COHABITANT = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_COHABITANT);

    public static final Uri URI_TABLE_PATHOLOGICAL_ANTECEDENTS = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PATHOLOGICAL_ANTECEDENTS);

    public static final Uri URI_TABLE_SUBJECTIVE_EVALUATION_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_SUBJECTIVE_EVALUATION_TEST);

    public static final Uri URI_TABLE_PRUEBA_RF = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PRUEBA_RF);

    public static final Uri URI_TABLE_CDR_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_CDR_TEST);

    public static final Uri URI_TABLE_PFEIFFER_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PFEIFFER_TEST);

    public static final Uri URI_TABLE_ALERT_SIGNS = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_ALERT_SIGNS);

    public static final Uri URI_TABLE_DEPRESSION_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_DEPRESSION_TEST);

    public static final Uri URI_TABLE_HHIES_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_HHIES_TEST);

    public static final Uri URI_TABLE_KATZ_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_KATZ_TEST);

    public static final Uri URI_TABLE_LAWTON_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_LAWTON_TEST);

    public static final Uri URI_TABLE_FRAIL_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_FRAIL_TEST);

    public static final Uri URI_TABLE_PSYCHOFAMILY_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PSYCHOFAMILY_TEST);

    public static final Uri URI_TABLE_PSYCHOAFFECTIVE_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PSYCHOAFFECTIVE_TEST);

    public static final Uri URI_TABLE_CLASSIFICATION = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_CLASSIFICATION);

    public static final Uri URI_TABLE_COGNITIVE = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_COGNITIVE);

    //MINIMENTAL
    public static final Uri URI_TABLE_MINIMENTAL_TEST = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_MINIMENTAL_TEST);


    public static void deletePatient(Context context, LongSparseArray<Boolean> ids,
                                     long[] mCohIds,
                                     DeletePatient_AsyncTask.OnPatientDeleted mCallback) {

        DeletePatient_AsyncTask task = new DeletePatient_AsyncTask(context,
                ids, mCohIds, mCallback);

        task.execute();

    }

    public static void SearchCohabitant(Context context, LongSparseArray<Boolean> ids,
                                        SearchCohabitant_AsyncTask.OnCohabitantSearched mCallback) {

        SearchCohabitant_AsyncTask task = new SearchCohabitant_AsyncTask(context,
                ids, mCallback);

        task.execute();
    }

    public static void DeletePathologicalAnt(Context context, long id_paciente, //long id_prueba,
                                             DeletePathologicalAnt_AsyncTask.OnPathologicalAntDeleted mCallback) {

        DeletePathologicalAnt_AsyncTask task = new DeletePathologicalAnt_AsyncTask(context,
                id_paciente, /*id_prueba, */mCallback);

        task.execute();

    }

    // endregion

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static long getIdFromUri(Uri result) {
        // si el resultado es vacio, es que no inserto.
        boolean isEmptyUri = result.equals(Uri.EMPTY);
        if (isEmptyUri) {
            return -1;
        } else {
            String last = result.getLastPathSegment();
            assert last != null;
            return Long.parseLong(last);
        }
    }


}
