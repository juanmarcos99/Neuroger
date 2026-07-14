package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 26/10/2016.
 */
public class InsertHHIESTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;

    private final OnHHIESTestInserted mCallback;
    private final ContentResolver cr;
    public InsertHHIESTest_AsyncTask(Context context,
                                     long idPatient,
                                     String questionsPosList,
                                     double hhie_evaluation,
                                     OnHHIESTestInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation = hhie_evaluation;

        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Uri uriHHIESTest = Constant.URI_TABLE_HHIES_TEST;

        ContentValues values = new ContentValues();
        values.put(Constant.COL_HHIES_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_HHIES_TEST_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_HHIES_TEST_EVALUATION, evaluation);

        cr.insert(uriHHIESTest, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onHHIESTestInserted();
    }

    public interface OnHHIESTestInserted {
        void onHHIESTestInserted();
    }
}
