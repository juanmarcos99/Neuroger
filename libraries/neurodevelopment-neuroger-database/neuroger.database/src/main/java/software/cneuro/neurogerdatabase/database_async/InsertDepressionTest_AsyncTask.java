package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 26/10/2016.
 */
public class InsertDepressionTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;

    private final OnDepressionTestInserted mCallback;

    private final ContentResolver cr;

    public InsertDepressionTest_AsyncTask(Context context,
                                          long idPatient,
                                          String questionsPosList,
                                          double dep_evaluation,
                                          OnDepressionTestInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation= dep_evaluation;

        this.mCallback = callback;
        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_DEPRESSION_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_DEPRESSION_TEST_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_DEPRESSION_TEST_EVALUATION, evaluation);

        cr.insert(Constant.URI_TABLE_DEPRESSION_TEST, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onDepressionTestInserted();
    }

    public interface OnDepressionTestInserted {
        void onDepressionTestInserted();
    }
}
