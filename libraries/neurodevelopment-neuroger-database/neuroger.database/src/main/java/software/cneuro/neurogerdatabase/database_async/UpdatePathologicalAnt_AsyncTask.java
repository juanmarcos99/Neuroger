package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 03/12/2015.
 */
public class UpdatePathologicalAnt_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long id;
    private final long id_paciente;
    private final String id_ant_patologicos;
    private final int medicationsQuantity;
    private final double evaluation;

    private final OnPathologicalAntUpdated mCallback;

    private final ContentResolver cr;

    public UpdatePathologicalAnt_AsyncTask(Context context,
                                           long id,
                                           long id_paciente,
                                           String id_ant_patologicos,
                                           int medicationsQuantity,
                                           double evaluation,
                                           OnPathologicalAntUpdated callback) {
        this.id = id;
        this.id_paciente = id_paciente;
        this.id_ant_patologicos = id_ant_patologicos;
        this.medicationsQuantity = medicationsQuantity;
        this.evaluation = evaluation;

        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID, id_paciente);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID, id_ant_patologicos);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY, medicationsQuantity);
        values.put(Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION, evaluation);

        String where = Constant.COL_PATHOLOGICAL_ANTECEDENTS_ID + "=" + id;

        cr.update(Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS, values, where, null);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onPathologicalAntUpdated();
    }

    public interface OnPathologicalAntUpdated {
        void onPathologicalAntUpdated();
    }
}
