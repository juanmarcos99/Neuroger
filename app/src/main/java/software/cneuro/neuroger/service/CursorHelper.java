package software.cneuro.neuroger.service;

import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_EVALUATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_DESIGN;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_LANGUAGE;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_ORDER_3;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_REMEMBERING;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import software.cneuro.neurogerdatabase.constant.Constant;

public class CursorHelper {
    private final ContentResolver mContentResolver;

    public CursorHelper(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public Cursor getCursorPatients() {
        Uri uri = Constant.URI_TABLE_PATIENT;
        return mContentResolver.query(uri, null, null, null,
                null);
    }

    public Cursor getCursorPhysical(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PRUEBA_RF_MC_TALLA,
                Constant.COL_PRUEBA_RF_MC_PESO,
                Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS,
                Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS,
                Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS,
                Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM,
                Constant.COL_PRUEBA_RF_EE_TANDEM,
                Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS,
                Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS,
                Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2,
                Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL};

        Uri baseUri = Constant.URI_TABLE_PRUEBA_RF;

        String select = "((" + Constant.COL_PRUEBA_RF_PACIENTE_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorFrail(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_FRAIL_TEST_FATIGUE,
                Constant.COL_FRAIL_TEST_RESISTANCE,
                Constant.COL_FRAIL_TEST_WANDERING,
                Constant.COL_FRAIL_TEST_DISEASES,
                Constant.COL_FRAIL_TEST_WEIGHT_CURRENT,
                Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO,
                Constant.COL_FRAIL_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_FRAIL_TEST;

        String select = "((" + Constant.COL_FRAIL_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    public Cursor getCursorSubjective(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_SE_TEST_HEALTH_STATE,
                Constant.COL_SE_TEST_MEMORY_CURRENT,
                Constant.COL_SE_TEST_MEMORY_PAST,
                Constant.COL_SE_TEST_DIFFICULTIES,
                Constant.COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT,
                Constant.COL_SE_TEST_COURSE_OF_DETERIORATION,
                Constant.COL_SE_TEST_EVOLUTION_TIME,
                Constant.COL_SE_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST;

        String select = "((" + Constant.COL_SE_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    public Cursor getCursorLawton(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_LAWTON_TEST_ANSWERS_LIST,
                Constant.COL_LAWTON_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_LAWTON_TEST;

        String select = "((" + Constant.COL_LAWTON_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorKatz(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_KATZ_TEST_ANSWERS_LIST,
                Constant.COL_KATZ_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_KATZ_TEST;

        String select = "((" + Constant.COL_KATZ_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorHhies(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_HHIES_TEST_ANSWERS_LIST,
                Constant.COL_HHIES_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_HHIES_TEST;

        String select = "((" + Constant.COL_HHIES_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorGds(int id) {
        return mContentResolver.query(
                Constant.URI_TABLE_DEPRESSION_TEST,
                new String[]{
                        Constant.COL_DEPRESSION_TEST_ANSWERS_LIST,
                        Constant.COL_DEPRESSION_TEST_EVALUATION
                },
                "((" + Constant.COL_DEPRESSION_TEST_PATIENT_ID + " = " + id + " ))",
                null,
                null);
    }

    public Cursor getCursorCdr(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_CDR_TEST_PATIENT_ID,
                Constant.COL_CDR_TEST_MEMORY,
                Constant.COL_CDR_TEST_ORIENTATION,
                Constant.COL_CDR_TEST_JUDGEMENT,
                Constant.COL_CDR_TEST_COMMUNITY,
                Constant.COL_CDR_TEST_HOBBIES,
                Constant.COL_CDR_TEST_PERSONAL_CARE,
                Constant.COL_CDR_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_CDR_TEST;

        String select = "((" + Constant.COL_CDR_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorAntecedents(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID,
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY,
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    public Cursor getCursorPfeiffer(int idCarer) {
        String[] PROJECTION = new String[]{
                Constant.COL_PFEIFFER_TEST_ANSWERS_LIST,
                Constant.COL_PFEIFFER_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PFEIFFER_TEST;

        String select = "((" + Constant.COL_PFEIFFER_TEST_PATIENT_ID + " = " + idCarer + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorMinimental(int id) {
        String[] PROJECTION = new String[]{
                COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION,
                COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION,
                COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION,
                COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG,
                COL_MINIMENTAL_TEST_MATH_ATTENTION,
                COL_MINIMENTAL_TEST_REMEMBERING,
                COL_MINIMENTAL_TEST_LANGUAGE,
                COL_MINIMENTAL_TEST_ORDER_3,
                COL_MINIMENTAL_TEST_DESIGN,
                COL_MINIMENTAL_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_MINIMENTAL_TEST;

        String select = "((" + Constant.COL_MINIMENTAL_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorPsychofamily(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST,
                Constant.COL_PSYCHOFAMILY_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PSYCHOFAMILY_TEST;

        String select = "((" + Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorPsychoaffective(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST,
                Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PSYCHOAFFECTIVE_TEST;

        String select = "((" + Constant.COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public void closeCursor(Cursor... cursor) {
        for (Cursor c : cursor) {
            if (c != null)
                c.close();
        }
    }

    public Cursor getCursorClassification(int id) {
        String[] PROJECTION = new String[]{
                software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION
        };

        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CLASSIFICATION;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public Cursor getCursorCognitive(int id) {
        String[] PROJECTION = new String[]{
                software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION
        };
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_COGNITIVE;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);

    }
}
