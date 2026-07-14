package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertKatzTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;

    private final OnKatzTestInserted callback;
    private final ContentResolver cr;

    public InsertKatzTest_AsyncTask(Context context,
                                    long idPatient,
                                    String questionsPosList,
                                    double evaluation,
                                    OnKatzTestInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Uri uri = Constant.URI_TABLE_KATZ_TEST;

        ContentValues values = new ContentValues();
        values.put(Constant.COL_KATZ_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_KATZ_TEST_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_KATZ_TEST_EVALUATION, evaluation);

        cr.insert(uri, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onKatzTestInserted();
    }

    public interface OnKatzTestInserted {
        void onKatzTestInserted();
    }
}
