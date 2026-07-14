package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 29/08/2016.
 */
public class InsertPfeifferTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;


    private final OnPfeifferTestInserted mCallback;

    private final ContentResolver cr;

    public InsertPfeifferTest_AsyncTask(Context context,
                                        long idPatient,
                                        String questionsPosList,
                                        double pfeiffer_evaluation,
                                        OnPfeifferTestInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation = pfeiffer_evaluation;

        this.mCallback = callback;
        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Uri uriPfeifferTest = Constant.URI_TABLE_PFEIFFER_TEST;

        ContentValues values = new ContentValues();
        values.put(Constant.COL_PFEIFFER_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_PFEIFFER_TEST_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_PFEIFFER_TEST_EVALUATION, evaluation);

        cr.insert(uriPfeifferTest, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onPfeifferTestInserted();
    }

    public interface OnPfeifferTestInserted {
        void onPfeifferTestInserted();
    }
}
