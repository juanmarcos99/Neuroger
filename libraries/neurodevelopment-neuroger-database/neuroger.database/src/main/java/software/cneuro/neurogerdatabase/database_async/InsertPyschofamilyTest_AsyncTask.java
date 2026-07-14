package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertPyschofamilyTest_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final long idPatient;
    private final String questionsPosList;
    private final double evaluation;

    private final OnPsychofamilyTestInserted callback;
    private final ContentResolver cr;

    public InsertPyschofamilyTest_AsyncTask(Context context,
                                            long idPatient,
                                            String questionsPosList,
                                            double evaluation,
                                            OnPsychofamilyTestInserted callback) {
        this.idPatient = idPatient;
        this.questionsPosList = questionsPosList;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... voids) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST, questionsPosList);
        values.put(Constant.COL_PSYCHOFAMILY_TEST_EVALUATION, evaluation);

        Uri result = cr.insert(Constant.URI_TABLE_PSYCHOFAMILY_TEST, values);
        return result != null ? Constant.getIdFromUri(result) : -1;
    }

    @Override
    protected void onPostExecute(Long id) {
        super.onPostExecute(id);
        callback.onPsychofamilyTestInserted(id);
    }

    public interface OnPsychofamilyTestInserted {
        void onPsychofamilyTestInserted(long id);
    }
}
