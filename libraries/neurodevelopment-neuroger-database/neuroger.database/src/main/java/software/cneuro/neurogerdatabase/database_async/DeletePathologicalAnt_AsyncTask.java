package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 04/12/2015.
 */
public class DeletePathologicalAnt_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;

    private final OnPathologicalAntDeleted mCallback;
    private final ContentResolver cr;

    public DeletePathologicalAnt_AsyncTask(Context context, long idPatient,
                                           OnPathologicalAntDeleted callback) {
        this.idPatient = idPatient;

        this.mCallback = callback;
        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String selection = Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + "=" + idPatient;
        cr.delete(Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS, selection, null);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onPathologicalAntDeleted();
    }

    public interface OnPathologicalAntDeleted {
        void onPathologicalAntDeleted();
    }
}
