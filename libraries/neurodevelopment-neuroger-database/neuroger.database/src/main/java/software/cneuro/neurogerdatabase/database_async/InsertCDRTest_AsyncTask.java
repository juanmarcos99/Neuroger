package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 18/08/2016.
 */
public class InsertCDRTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final int memory;
    private final int orientation;
    private final int judgement;
    private final int comunity;
    private final int hobbies;
    private final int personalCare;
    private final double evaluation;

    private final OnCDRTestInserted mCallback;

    private final ContentResolver cr;

    public InsertCDRTest_AsyncTask(Context context, long idPatient,
                                   int memory,
                                   int orientation,
                                   int judgement,
                                   int community,
                                   int hobbies,
                                   int personalCare,
                                   double cdr_evaluation, OnCDRTestInserted callback) {
        this.idPatient = idPatient;
        this.memory = memory;
        this.orientation = orientation;
        this.judgement = judgement;
        this.comunity = community;
        this.hobbies = hobbies;
        this.personalCare = personalCare;
        this.evaluation = cdr_evaluation;

        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_CDR_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_CDR_TEST_MEMORY, memory);
        values.put(Constant.COL_CDR_TEST_ORIENTATION, orientation);
        values.put(Constant.COL_CDR_TEST_JUDGEMENT, judgement);
        values.put(Constant.COL_CDR_TEST_COMMUNITY, comunity);
        values.put(Constant.COL_CDR_TEST_HOBBIES, hobbies);
        values.put(Constant.COL_CDR_TEST_PERSONAL_CARE, personalCare);
        values.put(Constant.COL_CDR_TEST_EVALUATION, evaluation);

        cr.insert(Constant.URI_TABLE_CDR_TEST, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onCDRTestInserted();
    }

    public interface OnCDRTestInserted {
        void onCDRTestInserted();
    }
}
