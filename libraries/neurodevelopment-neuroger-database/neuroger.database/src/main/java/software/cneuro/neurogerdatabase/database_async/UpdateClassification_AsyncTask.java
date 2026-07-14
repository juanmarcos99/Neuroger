package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class UpdateClassification_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long id;
    private final long idPatient;
    private final int antecedentsEvaluation;
    private final int medicationQuantity;
    private final int subjectiveEvaluation;
    private final int mmseScore;
    private final int katzScore;
    private final int frailScore;
    private final int lawtonScore;
    private final int pasEvaluation;
    private final int pAffectiveEvaluation;
    private final int evaluation;

    private final OnClassificationUpdated callback;
    private final ContentResolver cr;

    public UpdateClassification_AsyncTask(Context context,
                                          long id,
                                          long idPatient,
                                          int antecedentsEvaluation,
                                          int medicationQuantity,
                                          int subjectiveEvaluation,
                                          int mmseScore,
                                          int katzScore,
                                          int frailScore,
                                          int lawtonScore,
                                          int pasEvaluation,
                                          int pAffectiveEvaluation,
                                          int evaluation,
                                          OnClassificationUpdated callback) {
        this.id = id;
        this.idPatient = idPatient;
        this.antecedentsEvaluation = antecedentsEvaluation;
        this.medicationQuantity = medicationQuantity;
        this.subjectiveEvaluation = subjectiveEvaluation;
        this.mmseScore = mmseScore;
        this.katzScore = katzScore;
        this.frailScore = frailScore;
        this.lawtonScore = lawtonScore;
        this.pasEvaluation = pasEvaluation;
        this.pAffectiveEvaluation = pAffectiveEvaluation;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_CLASSIFICATION_PATIENT_ID, idPatient);
        values.put(Constant.COL_CLASSIFICATION_ANTECEDENTS_SCORE, antecedentsEvaluation);
        values.put(Constant.COL_CLASSIFICATION_MEDICATION_QUANTITY, medicationQuantity);
        values.put(Constant.COL_CLASSIFICATION_SUBJECTIVE_EVALUATION, subjectiveEvaluation);
        values.put(Constant.COL_CLASSIFICATION_KATZ_SCORE, katzScore);
        values.put(Constant.COL_CLASSIFICATION_FRAIL_SCORE, frailScore);
        values.put(Constant.COL_CLASSIFICATION_LAWTON_SCORE, lawtonScore);
        values.put(Constant.COL_CLASSIFICATION_PSYCHOFAMILY_SCORE, pasEvaluation);
        values.put(Constant.COL_CLASSIFICATION_PSYCHOAFFECTIVE_SCORE, pAffectiveEvaluation);
        values.put(Constant.COL_CLASSIFICATION_EVALUATION, evaluation);

        String where = Constant.COL_CLASSIFICATION_ID + "=" + id;

        cr.update(Constant.URI_TABLE_CLASSIFICATION, values, where, null);

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onClassificationUpdated();
    }

    public interface OnClassificationUpdated {
        void onClassificationUpdated();
    }
}
