package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 30/08/2016.
 */
public class InsertAlertSigns_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;

    private final OnAlertSignsInserted mCallback;

    private final ContentResolver cr;

    public InsertAlertSigns_AsyncTask(Context context, long idPatient,
                                      String questionsPosList,
                                      double alerts_evaluation,
                                      OnAlertSignsInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation = alerts_evaluation;

        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... params) {
        Uri result = insertAlertSign();
        assert result != null;
        return Constant.getIdFromUri(result);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        mCallback.onAlertSignsInserted();
    }

    private Uri insertAlertSign() {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_ALERT_SIGNS_COHABITANT_ID, idPatient);
        values.put(Constant.COL_ALERT_SIGNS_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_ALERT_SIGNS_EVALUATION, evaluation);

        return cr.insert(Constant.URI_TABLE_ALERT_SIGNS, values);
    }

    public interface OnAlertSignsInserted {
        void onAlertSignsInserted();
    }

}
