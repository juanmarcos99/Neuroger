package software.cneuro.neurogerdatabase.database;

import android.content.Context;

import software.cneuro.neurogerdatabase.database_async.UpdateClassificationByAntecedents_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdateCohabitant_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePatientCompensation_Async;
import software.cneuro.neurogerdatabase.database_async.UpdatePatient_AsyncTask;

/**
 * Created by exel.rodriguez on 03/12/2015.
 */
public class DatabaseUpdater {

    public static void updatePatient(Context context,
                                     long id_patient,
                                     String name,
                                     String lastname,
                                     String id,
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
                                     int patient_version,
                                     UpdatePatient_AsyncTask.OnPatientUpdated callback) {

        UpdatePatient_AsyncTask task = new UpdatePatient_AsyncTask(context, id_patient,
                name, lastname, id, birthdate, sex, occupation, years_studies, civil_status, hospital, origin, classification, country,
                prov_state, municip, address, skin_color, profession, coexistence,
                phone_number, patient_version, callback);

        task.execute();
    }

    public static void updatePatientCompensated(Context context, long id, int compensated,
                                                UpdatePatientCompensation_Async.OnPatientUpdated callback) {
        UpdatePatientCompensation_Async task = new UpdatePatientCompensation_Async(context, id, compensated, callback);
        task.execute();
    }

    public static void updateCohabitant(Context context, long cohabitant_id, long patient_id,
                                        String cohabitant_name,
                                        String cohabitant_lastname,
                                        String age,
                                        int cohabitant_sex,
                                        String familiarity,
                                        String cohabitant_phone,
                                        UpdateCohabitant_AsyncTask.OnCohabitantUpdated callback) {
        UpdateCohabitant_AsyncTask task = new UpdateCohabitant_AsyncTask(
                context, cohabitant_id, patient_id, cohabitant_name, cohabitant_lastname, age,
                cohabitant_sex, familiarity, cohabitant_phone, callback);

        task.execute();
    }

    public static void updatePathologicalAnt(Context context,
                                             long id,
                                             long id_paciente, //long id_prueba,
                                             String id_ant_patologicos,
                                             int medicationsQuantity,
                                             double evaluation,
                                             UpdatePathologicalAnt_AsyncTask.OnPathologicalAntUpdated callback) {
        UpdatePathologicalAnt_AsyncTask task = new UpdatePathologicalAnt_AsyncTask(
                context,
                id,
                id_paciente,
                /*id_prueba,*/ id_ant_patologicos,
                medicationsQuantity,
                evaluation, callback);
        task.execute();
    }

    public static void updateClassificationByAntecedents(Context context,
                                                         long id,
                                                         long subjectId,
                                                         double antecedentsEvaluation,
                                                         int medicationQuantity,
                                                         double evaluation,
                                                         UpdateClassificationByAntecedents_AsyncTask.OnClassificationUpdated callback) {
        UpdateClassificationByAntecedents_AsyncTask task = new UpdateClassificationByAntecedents_AsyncTask(
                context,
                id,
                subjectId,
                antecedentsEvaluation,
                medicationQuantity,
                evaluation,
                callback
        );
        task.execute();

    }
}
