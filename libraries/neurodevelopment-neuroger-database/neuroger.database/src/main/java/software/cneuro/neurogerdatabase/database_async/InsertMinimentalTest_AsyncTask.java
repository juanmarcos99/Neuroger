package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class InsertMinimentalTest_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final long idPatient;
    private final String temporalOrientation;
    private final String spatialOrientation;
    private final String retentionRegisterInformation;
    private final int mathAttentionFlag;
    private final String mathAttention;
    private final String remembering;
    private final String language;
    private final String order3;
    private final String design;
    private final double evaluation;

    private final OnMinimentalTestInserted callback;
    private final ContentResolver cr;

    public InsertMinimentalTest_AsyncTask(Context context,
                                          long idPatient,
                                          String temporalOrientation,
                                          String spatialOrientation,
                                          String retentionRegisterInformation,
                                          int mathAttentionFlag,
                                          String mathAttention,
                                          String remembering,
                                          String language,
                                          String order3,
                                          String design,
                                          double evaluation,
                                          OnMinimentalTestInserted callback) {
        this.idPatient = idPatient;
        this.temporalOrientation = temporalOrientation;
        this.spatialOrientation = spatialOrientation;
        this.retentionRegisterInformation = retentionRegisterInformation;
        this.mathAttentionFlag = mathAttentionFlag;
        this.mathAttention = mathAttention;
        this.remembering = remembering;
        this.language = language;
        this.order3 = order3;
        this.design = design;
        this.evaluation = evaluation;

        this.callback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... voids) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_MINIMENTAL_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION, temporalOrientation);
        values.put(Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION, spatialOrientation);
        values.put(Constant.COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION, retentionRegisterInformation);
        values.put(Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG, mathAttentionFlag);
        values.put(Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION, mathAttention);
        values.put(Constant.COL_MINIMENTAL_TEST_REMEMBERING, remembering);
        values.put(Constant.COL_MINIMENTAL_TEST_LANGUAGE, language);
        values.put(Constant.COL_MINIMENTAL_TEST_ORDER_3, order3);
        values.put(Constant.COL_MINIMENTAL_TEST_DESIGN, design);
        values.put(Constant.COL_MINIMENTAL_EVALUATION, evaluation);

        Uri result = cr.insert(Constant.URI_TABLE_MINIMENTAL_TEST, values);

        assert result != null;
        return Constant.getIdFromUri(result);
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        callback.onMinimentalTestInserted(aLong);
    }

    public interface OnMinimentalTestInserted {
        void onMinimentalTestInserted(long id);
    }
}
