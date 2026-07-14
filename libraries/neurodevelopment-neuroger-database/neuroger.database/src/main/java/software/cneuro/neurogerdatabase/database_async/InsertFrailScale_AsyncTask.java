package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertFrailScale_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final long idPatient;
    private final int fatigue;
    private final int resistance;
    private final int wandering;
    private final String diseases;
    private final double weightCurrent;
    private final double weight1yearAgo;
    private final double evaluation;

    private final OnFrailScaleInserted callback;

    private final ContentResolver cr;

    public InsertFrailScale_AsyncTask(Context context,
                                      long idPatient,
                                      int fatigue,
                                      int resistance,
                                      int wandering,
                                      String diseases,
                                      double weightCurrent,
                                      double weight1yearAgo,
                                      double evaluation,
                                      OnFrailScaleInserted callback) {
        this.idPatient = idPatient;
        this.fatigue = fatigue;
        this.resistance = resistance;
        this.wandering = wandering;
        this.diseases = diseases;
        this.weightCurrent = weightCurrent;
        this.weight1yearAgo = weight1yearAgo;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... voids) {
//        Uri uri = Constant.URI_TABLE_FRAIL_TEST;
//        ContentValues[] contentValues = new ContentValues[diseases.size()];
//
//        for (int i = 0; i < diseases.size(); i++) {
//            int diseasesCheck = diseases.get(i);
//
//            ContentValues values = new ContentValues();
//            values.put(Constant.COL_FRAIL_TEST_PATIENT_ID, idPatient);
//            values.put(Constant.COL_FRAIL_TEST_FATIGUE, fatigue);
//            values.put(Constant.COL_FRAIL_TEST_RESISTANCE, resistance);
//            values.put(Constant.COL_FRAIL_TEST_WANDERING, wandering);
//            values.put(Constant.COL_FRAIL_TEST_DISEASES, diseasesCheck);
//            values.put(Constant.COL_FRAIL_TEST_WEIGHT_CURRENT, weightCurrent);
//            values.put(Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO, weight1yearAgo);
//            values.put(Constant.COL_FRAIL_TEST_EVALUATION, evaluation);
//
//            //cr.insert(uriSubEvaluationTest, values);
//            contentValues[i] = values;
//        }
//
//        return cr.bulkInsert(uri, contentValues);

        ContentValues values = new ContentValues();
        values.put(Constant.COL_FRAIL_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_FRAIL_TEST_FATIGUE, fatigue);
        values.put(Constant.COL_FRAIL_TEST_RESISTANCE, resistance);
        values.put(Constant.COL_FRAIL_TEST_WANDERING, wandering);
        values.put(Constant.COL_FRAIL_TEST_DISEASES, diseases);
        values.put(Constant.COL_FRAIL_TEST_WEIGHT_CURRENT, weightCurrent);
        values.put(Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO, weight1yearAgo);
        values.put(Constant.COL_FRAIL_TEST_EVALUATION, evaluation);

        Uri result = cr.insert(Constant.URI_TABLE_FRAIL_TEST, values);

        assert result != null;
        return Constant.getIdFromUri(result);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        callback.onFrailScaleInserted();
    }

    public interface OnFrailScaleInserted {
        void onFrailScaleInserted();
    }
}
