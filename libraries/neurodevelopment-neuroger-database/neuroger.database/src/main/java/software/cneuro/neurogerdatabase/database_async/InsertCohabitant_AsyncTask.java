package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 22/06/2015.
 */
public class InsertCohabitant_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final String familiarity;

    private final long patient_id;
    private final String cohabitant_name;
    private final String cohabitant_lastname;
    private final String age;
    private final int cohabitant_sex;
    public InsertCohabitant_AsyncTask(Context context, long patient_id,
                                      String cohabitant_name, String cohabitant_lastname, String age,
                                      int cohabitant_sex, String familiarity,
                                      String cohabitant_phone, OnCohabitantInserted callback) {
        this.cohabitant_name = cohabitant_name;
        this.cohabitant_lastname = cohabitant_lastname;
        this.age = age;
        this.cohabitant_sex = cohabitant_sex;
        this.familiarity = familiarity;
        this.cohabitant_phone = cohabitant_phone;
        this.patient_id = patient_id;
        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }
    private final String cohabitant_phone;
    private final OnCohabitantInserted mCallback;

    private final ContentResolver cr;

    @Override
    protected Long doInBackground(Void... params) {
        Uri result = insertCohabitant();
        return Constant.getIdFromUri(result);
    }

    private Uri insertCohabitant() {

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

        return cr.insert(Constant.URI_TABLE_COHABITANT, values);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        mCallback.onCohabitantInserted(result);
    }

    public interface OnCohabitantInserted {
        void onCohabitantInserted(long id);
    }
}
