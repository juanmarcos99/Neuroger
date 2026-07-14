package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
//import androidx.collection.LongSparseArray;
import androidx.collection.LongSparseArray;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 25/06/2015.
 */
public class DeletePatient_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final ContentResolver cr;

    private final LongSparseArray<Boolean> mIds;
//    private final long[] mCohabitantIds;
    private final OnPatientDeleted mCallback;

    public DeletePatient_AsyncTask(Context context, LongSparseArray<Boolean> ids, long[] Cohabitant_Ids,
                                   OnPatientDeleted callback) {
        this.mIds = ids;
//        this.mCohabitantIds = Cohabitant_Ids;

        this.cr = context.getContentResolver();
        this.mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Uri uriPatient = Constant.URI_TABLE_PATIENT;
//        Uri uriCohabitant = Constant.URI_TABLE_COHABITANT;
        Uri uriPathologicalA = Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;
        Uri uriPruebaRf = Constant.URI_TABLE_PRUEBA_RF;
        Uri uriSubjetiveE = Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST;
        Uri uriMinimental = Constant.URI_TABLE_MINIMENTAL_TEST;
        Uri uriCDR = Constant.URI_TABLE_CDR_TEST;
        Uri uriPfeiffer = Constant.URI_TABLE_PFEIFFER_TEST;
//        Uri uriAlertSigns = Constant.URI_TABLE_ALERT_SIGNS;
        Uri uriHhies = Constant.URI_TABLE_HHIES_TEST;
        Uri uriDepression = Constant.URI_TABLE_DEPRESSION_TEST;

        Uri uriKatz = Constant.URI_TABLE_KATZ_TEST;
        Uri uriFrail = Constant.URI_TABLE_FRAIL_TEST;
        Uri uriLawton = Constant.URI_TABLE_LAWTON_TEST;
        Uri uriPsychofamily = Constant.URI_TABLE_PSYCHOFAMILY_TEST;
        Uri uriPsychoaffective = Constant.URI_TABLE_PSYCHOAFFECTIVE_TEST;
        Uri uriClassification = Constant.URI_TABLE_CLASSIFICATION;
        Uri uriCognitive = Constant.URI_TABLE_COGNITIVE;

        for (int i = 0; i < mIds.size(); i++) {
            long idPatient = mIds.keyAt(i);

//            String selection = Constant.COL_COHABITANT_PATIENT_ID + "=" + idPatient;
//            cr.delete(uriCohabitant, selection, null);

            String selection = Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + "=" + idPatient;
            cr.delete(uriPathologicalA, selection, null);

            selection = Constant.COL_PRUEBA_RF_PACIENTE_ID + "=" + idPatient;
            cr.delete(uriPruebaRf, selection, null);

            selection = Constant.COL_SE_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriSubjetiveE, selection, null);

            selection = Constant.COL_MINIMENTAL_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriMinimental, selection, null);

            selection = Constant.COL_PFEIFFER_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriPfeiffer, selection, null);

            selection = Constant.COL_CDR_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriCDR, selection, null);

            selection = Constant.COL_HHIES_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriHhies, selection, null);

            selection = Constant.COL_DEPRESSION_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriDepression, selection, null);

            selection = Constant.COL_KATZ_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriKatz, selection, null);

            selection = Constant.COL_FRAIL_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriFrail, selection, null);

            selection = Constant.COL_LAWTON_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriLawton, selection, null);

            selection = Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriPsychofamily, selection, null);

            selection = Constant.COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID + "=" + idPatient;
            cr.delete(uriPsychoaffective, selection, null);

            selection = Constant.COL_CLASSIFICATION_PATIENT_ID + "=" + idPatient;
            cr.delete(uriClassification, selection, null);

            selection = Constant.COL_COGNITIVE_PATIENT_ID + "=" + idPatient;
            cr.delete(uriCognitive, selection, null);

            selection = Constant.COL_PATIENT_ID + "=" + idPatient;
            cr.delete(uriPatient, selection, null);
        }

//        for (long id_cohabitant : mCohabitantIds) {
//            String selection = Constant.COL_PFEIFFER_TEST_COHABITANT_ID + "=" + id_cohabitant;
//            cr.delete(uriPfeiffer, selection, null);
//
//            selection = Constant.COL_ALERT_SIGNS_COHABITANT_ID + "=" + id_cohabitant;
//            cr.delete(uriAlertSigns, selection, null);
//        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onPatientDeleted();
    }

    public interface OnPatientDeleted {
        void onPatientDeleted();
    }
}
