package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by klaudia on 08/03/2018.
 */

public class UpdateCohabitant_AsyncTask extends AsyncTask<Void, Void, Long>{

    public interface OnCohabitantUpdated {
        void OnCohabitantUpdated(long _id);
    }

    private final long _id;
    private final long patient_id;
    private final String cohabitant_name;
    private final String cohabitant_lastname;
    private final String age;
    private final int cohabitant_sex;
    private final String familiarity;
    private final String cohabitant_phone;
    private final UpdateCohabitant_AsyncTask.OnCohabitantUpdated mCallback;

    private final ContentResolver cr;

    public UpdateCohabitant_AsyncTask(Context context,long _id, long patient_id, String cohabitant_name,
                                      String cohabitant_lastname, String age, int cohabitant_sex,
                                      String familiriarity, String cohabitant_phone,
                                      OnCohabitantUpdated callback) {

        this._id = _id;
        this.cohabitant_name = cohabitant_name;
        this.cohabitant_lastname = cohabitant_lastname;
        this.age = age;
        this.cohabitant_sex = cohabitant_sex;
        this.familiarity = familiriarity;
        this.cohabitant_phone = cohabitant_phone;
        this.patient_id = patient_id;
        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    @Override
    protected Long doInBackground(Void... params) {

        ContentValues values = new ContentValues();
        values.put(Constant.COL_COHABITANT_NAME, cohabitant_name);
        values.put(Constant.COL_COHABITANT_LASTNAME, cohabitant_lastname);
        values.put(Constant.COL_COHABITANT_AGE, age);
        values.put(Constant.COL_COHABITANT_SEX, cohabitant_sex);
        values.put(Constant.COL_COHABITANT_FAMILIARITY, familiarity);
        values.put(Constant.COL_COHABITANT_PHONE_NUMBER, cohabitant_phone);
        values.put(Constant.COL_COHABITANT_PATIENT_ID, patient_id);
        values.put(Constant.COL_COHABITANT_FULLNAME, cohabitant_name + " "
                + cohabitant_lastname);

        String where = Constant.COL_COHABITANT_ID + "=" + _id;

        cr.update(Constant.URI_TABLE_COHABITANT, values, where, null);
        return _id;
    }

    @Override
    protected void onPostExecute(Long _id) {
        super.onPostExecute(_id);
        mCallback.OnCohabitantUpdated(_id);
    }

}
