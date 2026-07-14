package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertClassification_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final double antecedentsEvaluation;
    private final int medicationQuantity;
    private final double subjectiveEvaluation;
    private final double katzScore;
    private final double frailScore;
    private final double lawtonScore;
    private final double pasEvaluation;
    private final double pAffectiveEvaluation;
    private final double evaluation;

    private final OnClassificationInserted callback;
    private final ContentResolver cr;

    public InsertClassification_AsyncTask(Context context,
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
                                          OnClassificationInserted callback) {
        this.idPatient = idPatient;
        this.antecedentsEvaluation = antecedentsEvaluation;
        this.medicationQuantity = medicationQuantity;
        this.subjectiveEvaluation = subjectiveEvaluation;
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

        cr.insert(Constant.URI_TABLE_CLASSIFICATION, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onClassificationInserted();
    }

    public interface OnClassificationInserted {
        void onClassificationInserted();
    }
}
