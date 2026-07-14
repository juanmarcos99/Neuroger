package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

public class UpdatePatientCompensation_Async extends AsyncTask<Void, Void, Long> {

    private final long _id;
    private final int compensated;
    private final OnPatientUpdated callback;
    private final ContentResolver cr;

    public UpdatePatientCompensation_Async(Context context, long _id, int compensated, OnPatientUpdated callback) {
        this._id = _id;
        this.compensated = compensated;

        this.cr = context.getContentResolver();
        this.callback = callback;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        Uri uri = Constant.URI_TABLE_PATIENT;
        ContentValues values = new ContentValues();

        values.put(Constant.COL_PATIENT_COMPENSATED, compensated);

        String where = Constant.COL_PATIENT_ID + "=" + _id;
        cr.update(uri, values, where, null);
        return _id;
    }

    @Override
    protected void onPostExecute(Long _id) {
        super.onPostExecute(_id);
        callback.OnPatientUpdated(_id);
    }

    public interface OnPatientUpdated {
        void OnPatientUpdated(long _id);
    }
}
