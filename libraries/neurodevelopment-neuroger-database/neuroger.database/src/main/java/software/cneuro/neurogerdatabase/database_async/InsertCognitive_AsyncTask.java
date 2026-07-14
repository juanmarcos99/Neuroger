package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertCognitive_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final double cdrEvaluation;
    private final double gdsEvaluation;
    private final double hhiesEvaluation;
    private final double mmseEvaluation;
    private final double pfeifferEvaluation;
    private final double evaluation;

    private final OnCognitiveInserted callback;
    private final ContentResolver cr;

    public InsertCognitive_AsyncTask(Context context,
                                     long idPatient,
                                     double cdrEvaluation,
                                     double gdsEvaluation,
                                     double hhiesEvaluation,
                                     double mmseEvaluation,
                                     double pfeifferEvaluation,
                                     double evaluation,
                                     OnCognitiveInserted callback) {

        this.idPatient = idPatient;
        this.cdrEvaluation = cdrEvaluation;
        this.gdsEvaluation = gdsEvaluation;
        this.hhiesEvaluation = hhiesEvaluation;
        this.mmseEvaluation = mmseEvaluation;
        this.pfeifferEvaluation = pfeifferEvaluation;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_COGNITIVE_PATIENT_ID, idPatient);
        values.put(Constant.COL_COGNITIVE_CDR, cdrEvaluation);
        values.put(Constant.COL_COGNITIVE_GDS, gdsEvaluation);
        values.put(Constant.COL_COGNITIVE_HHIES, hhiesEvaluation);
        values.put(Constant.COL_COGNITIVE_MMSE, mmseEvaluation);
        values.put(Constant.COL_COGNITIVE_PFEIFFER, pfeifferEvaluation);
        values.put(Constant.COL_COGNITIVE_EVALUATION, evaluation);

        cr.insert(Constant.URI_TABLE_COGNITIVE, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onCognitiveInserted();
    }

    public interface OnCognitiveInserted {
        void onCognitiveInserted();
    }
}
