package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class UpdateClassificationByAntecedents_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long id;
    private final long idPatient;
    private final double antecedentsScore;
    private final int medicationQuantity;
    private final double evaluation;

    private final OnClassificationUpdated callback;
    private final ContentResolver cr;

    public UpdateClassificationByAntecedents_AsyncTask(Context context,
                                                       long id,
                                                       long idPatient,
                                                       double antecedentsScore,
                                                       int medicationQuantity,
                                                       double evaluation,
                                                       OnClassificationUpdated callback) {
        this.id = id;
        this.idPatient = idPatient;
        this.antecedentsScore = antecedentsScore;
        this.medicationQuantity = medicationQuantity;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_CLASSIFICATION_PATIENT_ID, idPatient);
        values.put(Constant.COL_CLASSIFICATION_ANTECEDENTS_SCORE, antecedentsScore);
        values.put(Constant.COL_CLASSIFICATION_MEDICATION_QUANTITY, medicationQuantity);
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