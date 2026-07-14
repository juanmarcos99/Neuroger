package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 23/10/2015.
 */
public class InsertPathologicalAnt_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final long idPatient;
    private final String questionsPosList;
    private final int medicationsQuantity;
    private final double evaluation;

    private final OnPathologicalAntInserted callback;
    private final ContentResolver cr;

    public InsertPathologicalAnt_AsyncTask(Context context, long idPatient,
                                           String questionsPosList,
                                           int medicationsQuantity,
                                           double evaluation,
                                           OnPathologicalAntInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.medicationsQuantity = medicationsQuantity;
        this.evaluation = evaluation;

        this.callback = callback;
        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID, idPatient);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID, questionsPosList);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY, medicationsQuantity);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION, evaluation);

        Uri result = cr.insert(Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS, values);

        assert result != null;
        return Constant.getIdFromUri(result);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        callback.onPathologicalAntInserted();
    }

    public interface OnPathologicalAntInserted {
        void onPathologicalAntInserted();
    }

}
