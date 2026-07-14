package software.cneuro.neurogerdatabase.database;

import android.content.Context;

import software.cneuro.neurogerdatabase.database_async.InsertAlertSigns_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertCDRTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertClassification_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertCognitive_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertCohabitant_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertDepressionTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertFrailScale_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertHHIESTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertKatzTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertLawton_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertMinimentalTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPatient_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPatient_AsyncTask.OnPatientInserted;
import software.cneuro.neurogerdatabase.database_async.InsertPfeifferTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPsychoaffective_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPyschofamilyTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertSubEvaluationTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertTestPP_AsyncTask;

/**
 * Created by exel.rodriguez on 16/06/2015.
 */
public class DatabaseInserter {
    public static void insertPatient(Context context,
                                     String name,
                                     String lastname,
                                     String id,
                                     String patient_guid,
                                     String birthdate,
                                     int sex,
                                     String occupation,
                                     String years_studies,
                                     String civil_status,
                                     String hospital,
                                     String origin,
                                     String classification,
                                     String country,
                                     String prov_state,
                                     String municip,
                                     String address,
                                     String skin_color,
                                     String profession,
                                     String coexistence,
                                     String phone_number,
                                     int compensated,
                                     OnPatientInserted callback) {

        InsertPatient_AsyncTask task = new InsertPatient_AsyncTask(context,
                name, lastname, id, patient_guid, birthdate, sex, occupation, years_studies, civil_status, hospital, origin, classification, country,
                prov_state, municip, address, skin_color, profession, coexistence,
                phone_number, compensated, callback);

        task.execute();
    }

    public static void insertCohabitant(Context context, long patient_id,
                                        String cohabitant_name,
                                        String cohabitant_lastname,
                                        String age,
                                        int cohabitant_sex,
                                        String familiriarity,
                                        String cohabitant_phone,
                                        InsertCohabitant_AsyncTask.OnCohabitantInserted callback) {
        InsertCohabitant_AsyncTask task = new InsertCohabitant_AsyncTask(
                context, patient_id, cohabitant_name, cohabitant_lastname, age,
                cohabitant_sex, familiriarity, cohabitant_phone, callback);
        task.execute();
    }

    public static void insertTestPP(Context context, long id_paciente,
                                    double m_tiempo_seg,
                                    int m_cant_pasos,
                                    int ee_pies_paralelos,
                                    int ee_semi_tandem,
                                    int ee_tandem,
                                    double l_tiempo_seg,
                                    int l_lograr_levantadas,
                                    int l_numero_levantadas,
                                    double fa_fuerza_mano_der1,
                                    double fa_fuerza_mano_izq1,
                                    double fa_fuerza_mano_der2,
                                    double fa_fuerza_mano_izq2,
                                    double mc_peso,
                                    double mc_talla,
                                    int evaluacion_gen,
                                    double puntaje,
                                    InsertTestPP_AsyncTask.OnTestPPInserted callback) {

        InsertTestPP_AsyncTask task = new InsertTestPP_AsyncTask(context,
                id_paciente, m_tiempo_seg, m_cant_pasos,
                ee_pies_paralelos, ee_semi_tandem, ee_tandem, l_tiempo_seg, l_lograr_levantadas,
                l_numero_levantadas, fa_fuerza_mano_der1, fa_fuerza_mano_izq1, fa_fuerza_mano_der2,
                fa_fuerza_mano_izq2, mc_peso, mc_talla, evaluacion_gen, puntaje, callback);

        task.execute();
    }


    public static void insertPathologicalAnt(Context context, long id_paciente,
                                             String id_ant_patologicos,
                                             int medicationsQuantity,
                                             double evaluation,
                                             InsertPathologicalAnt_AsyncTask.OnPathologicalAntInserted callback) {
        InsertPathologicalAnt_AsyncTask task = new InsertPathologicalAnt_AsyncTask(
                context,
                id_paciente,
                id_ant_patologicos,
                medicationsQuantity,
                evaluation,
                callback);

        task.execute();
    }

    public static void insertSubEvaluationTest(Context context,
                                               long idPatient,
                                               int healthState,
                                               int memoryCurrent,
                                               int memoryPast,
                                               String difficulties,
                                               int iniCognitiveImpairment,
                                               int courseOfDeterioration,
                                               int evolutionTime,
                                               int evaluation,
                                               InsertSubEvaluationTest_AsyncTask.OnSubEvaluationTestInserted callback) {

        InsertSubEvaluationTest_AsyncTask task = new InsertSubEvaluationTest_AsyncTask(
                context, idPatient, healthState, memoryCurrent, memoryPast, difficulties, iniCognitiveImpairment, courseOfDeterioration,
                evolutionTime, evaluation, callback);

        task.execute();
    }

    public static void insertCDRTest(Context context, long idPatient,
                                     int memory,
                                     int orientation,
                                     int judgement,
                                     int community,
                                     int hobbies,
                                     int personalCare,
                                     double evaluation,
                                     InsertCDRTest_AsyncTask.OnCDRTestInserted callback
    ) {
        InsertCDRTest_AsyncTask task = new InsertCDRTest_AsyncTask(
                context, idPatient, memory, orientation,
                judgement, community, hobbies, personalCare, evaluation, callback);
        task.execute();
    }

    public static void insertPfeifferTest(Context context,
                                          long idPatient,
                                          String questionsPosList,
                                          double pfeifferEvaluation,
                                          InsertPfeifferTest_AsyncTask.OnPfeifferTestInserted callback) {

        InsertPfeifferTest_AsyncTask task = new InsertPfeifferTest_AsyncTask(
                context,
                idPatient,
                questionsPosList,
                pfeifferEvaluation,
                callback);

        task.execute();
    }

    public static void insertAlertSigns(Context context, long id_cohabitant,
                                        String questionsPosList,
                                        double alertsEvaluation,
                                        InsertAlertSigns_AsyncTask.OnAlertSignsInserted callback) {

        InsertAlertSigns_AsyncTask task = new InsertAlertSigns_AsyncTask(
                context, id_cohabitant, questionsPosList, alertsEvaluation, callback);

        task.execute();
    }

    public static void insertDepressionTest(Context context, long id_patient,
                                            String questionsPosList,
                                            double depEvaluation,
                                            InsertDepressionTest_AsyncTask.OnDepressionTestInserted callback) {

        InsertDepressionTest_AsyncTask task = new InsertDepressionTest_AsyncTask(
                context, id_patient, questionsPosList, depEvaluation, callback);

        task.execute();
    }

    public static void insertHHIESTest(Context context,
                                       long id_patient,
                                       String questionsPosList,
                                       double hhieEvaluation,
                                       InsertHHIESTest_AsyncTask.OnHHIESTestInserted callback) {

        InsertHHIESTest_AsyncTask task = new InsertHHIESTest_AsyncTask(
                context,
                id_patient,
                questionsPosList,
                hhieEvaluation,
                callback);

        task.execute();
    }

    public static void insertKatzTest(Context context,
                                      long idPatient,
                                      String questionsPosList,
                                      double evaluation,
                                      InsertKatzTest_AsyncTask.OnKatzTestInserted callback) {

        InsertKatzTest_AsyncTask task = new InsertKatzTest_AsyncTask(context,
                idPatient,
                questionsPosList,
                evaluation,
                callback);
        task.execute();
    }

    public static void insertLawtonTest(Context context,
                                        long idPatient,
                                        String questionsPosList,
                                        double evaluation,
                                        InsertLawton_AsyncTask.OnLawtonTestInserted callback) {

        InsertLawton_AsyncTask task = new InsertLawton_AsyncTask(context,
                idPatient,
                questionsPosList,
                evaluation,
                callback);
        task.execute();
    }

    public static void insertFrailScale(Context context,
                                        long idPatient,
                                        int fatigue,
                                        int resistance,
                                        int wandering,
                                        String diseases,
                                        double weightCurrent,
                                        double weight1yearAgo,
                                        double evaluation,
                                        InsertFrailScale_AsyncTask.OnFrailScaleInserted callback) {
        InsertFrailScale_AsyncTask task = new InsertFrailScale_AsyncTask(context,
                idPatient,
                fatigue,
                resistance,
                wandering,
                diseases,
                weightCurrent,
                weight1yearAgo,
                evaluation,
                callback);
        task.execute();
    }

    public static void insertClassification(Context context,
                                            long idPatient,
                                            double antecedentsEvaluation,
                                            int medicationQuantity,
                                            double subjectiveEvaluation,
                                            double katzScore,
                                            double frailScore,
                                            double lawtonScore,
                                            double pasEvaluation,
                                            double pAffectiveEvaluation,
                                            double evaluation,
                                            InsertClassification_AsyncTask.OnClassificationInserted callback) {
        InsertClassification_AsyncTask task = new InsertClassification_AsyncTask(context,
                idPatient,
                antecedentsEvaluation,
                medicationQuantity,
                subjectiveEvaluation,
                katzScore,
                frailScore,
                lawtonScore,
                pasEvaluation,
                pAffectiveEvaluation,
                evaluation,
                callback
        );
        task.execute();
    }

    public static void insertCognitive(Context context,
                                       long idPatient,
                                       double cdrEvaluation,
                                       double gdsEvaluation,
                                       double hhiesEvaluation,
                                       double mmseEvaluation,
                                       double pfeifferEvaluation,
                                       double evaluation,
                                       InsertCognitive_AsyncTask.OnCognitiveInserted callback) {
        InsertCognitive_AsyncTask task = new InsertCognitive_AsyncTask(context,
                idPatient,
                cdrEvaluation,
                gdsEvaluation,
                hhiesEvaluation,
                mmseEvaluation,
                pfeifferEvaluation,
                evaluation,
                callback
        );
        task.execute();
    }

    public static void insertMinimentalTest(Context context,
                                            long idPatient,
                                            String temporalOrientation,
                                            String spatialOrientation,
                                            String retentionRegisterInformation,
                                            int mathAttentionFlag,
                                            String mathAttention,
                                            String remembering,
                                            String language,
                                            String order3,
                                            String design,
                                            double evaluation,
                                            InsertMinimentalTest_AsyncTask.OnMinimentalTestInserted callback) {
        InsertMinimentalTest_AsyncTask task = new InsertMinimentalTest_AsyncTask(
                context,
                idPatient,
                temporalOrientation,
                spatialOrientation,
                retentionRegisterInformation,
                mathAttentionFlag,
                mathAttention,
                remembering,
                language,
                order3,
                design,
                evaluation,
                callback
        );
        task.execute();
    }

    public static void insertPsychofamilyTest(Context context,
                                              long idPatient,
                                              String questionsPosList,
                                              double score,
                                              InsertPyschofamilyTest_AsyncTask.OnPsychofamilyTestInserted callback) {
        InsertPyschofamilyTest_AsyncTask task = new InsertPyschofamilyTest_AsyncTask(
                context,
                idPatient,
                questionsPosList,
                score,
                callback);
        task.execute();
    }

    public static void insertPsychoaffectiveTest(Context context,
                                                 long idPatient,
                                                 String questionsPosList,
                                                 double evaluation,
                                                 InsertPsychoaffective_AsyncTask.OnPsychoaffectiveTestInserted callback) {
        InsertPsychoaffective_AsyncTask task = new InsertPsychoaffective_AsyncTask(context,
                idPatient,
                questionsPosList,
                evaluation,
                callback);
        task.execute();
    }
}
