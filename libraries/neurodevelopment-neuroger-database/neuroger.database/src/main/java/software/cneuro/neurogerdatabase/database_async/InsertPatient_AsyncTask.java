package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 19/06/2015.
 */
public class InsertPatient_AsyncTask extends AsyncTask<Void, Void, Long> {

    private final String occupation;

    private final String name;
    private final String lastname;
    private final String id;
    private final String guid;
    private final String birthdate;
    private final int sex;
    private final String profession;
    private final String years_studies;
    private final String civil_status;
    private final String hospital;
    private final String origin;
    private final String classification;
    private final String country;
    private final String prov_state;
    private final String municip;
    private final String address;
    private final String skin_color;
    private final String coexistence;
    private final String phone_number;
    private final int patient_version;
    private final int compensated;
    private final OnPatientInserted mCallback;
    private final ContentResolver cr;


    public InsertPatient_AsyncTask(Context context, String name, String lastname, String id,
                                   String guid, String birthdate, int sex, String occupation, String years_studies,
                                   String civil_status, String hospital, String origin,
                                   String classification, String country, String prov_state, String municip,
                                   String address, String skin_color, String profession,
                                   String coexistence, String phone_number, int compensated,
                                   OnPatientInserted callback) {
        this.name = name;
        this.lastname = lastname;
        this.id = id;
        this.guid = guid;
        this.birthdate = birthdate;
        this.sex = sex;
        this.occupation = occupation;
        this.years_studies = years_studies;
        this.civil_status = civil_status;
        this.hospital = hospital;
        this.origin = origin;
        this.classification = classification;
        this.country = country;
        this.prov_state = prov_state;
        this.municip = municip;
        this.address = address;
        this.skin_color = skin_color;
        this.profession = profession;
        this.coexistence = coexistence;
        this.phone_number = phone_number;
        this.patient_version = 1;
        this.compensated = compensated;
        this.mCallback = callback;

        this.cr = context.getContentResolver();

    }

    private Uri insertPatient() {
        ContentValues values = new ContentValues();

        values.put(Constant.COL_PATIENT_NAME, name);
        values.put(Constant.COL_PATIENT_LASTNAME, lastname);
        values.put(Constant.COL_PATIENT_ID_NUMBER, id);
        values.put(Constant.COL_PATIENT_GUID, guid);
        values.put(Constant.COL_PATIENT_BIRTHDATE, birthdate);
        values.put(Constant.COL_PATIENT_SEX, sex);
        values.put(Constant.COL_PATIENT_OCCUPATION, occupation);
        values.put(Constant.COL_PATIENT_YEARS_STUDIES, years_studies);
        values.put(Constant.COL_PATIENT_CIVIL_STATUS, civil_status);
        values.put(Constant.COL_PATIENT_HOSPITAL, hospital);
        values.put(Constant.COL_PATIENT_ORIGIN, origin);
        values.put(Constant.COL_PATIENT_CLINIC_CLASSIFICATION, classification);
        values.put(Constant.COL_PATIENT_COUNTRY, country);
        values.put(Constant.COL_PATIENT_PROVINCE_STATE, prov_state);
        values.put(Constant.COL_PATIENT_MUNICIPALITY, municip);
        values.put(Constant.COL_PATIENT_ADDRESS, address);
        values.put(Constant.COL_PATIENT_SKIN_COLOR, skin_color);
        values.put(Constant.COL_PATIENT_PROFESSION, profession);
        values.put(Constant.COL_PATIENT_COEXISTENCE, coexistence);
        values.put(Constant.COL_PATIENT_PHONE_NUMBER, phone_number);
        values.put(Constant.COL_PATIENT_INSERT_DATE, Constant.getCurrentDate());
        values.put(Constant.COL_PATIENT_VERSION, patient_version);
        values.put(Constant.COL_PATIENT_FULLNAME, name + " "
                + lastname);
        values.put(Constant.COL_PATIENT_COMPENSATED, compensated);

        // insercion a la base de datos mediante la URI y el conjunto de valores.

        return cr.insert(Constant.URI_TABLE_PATIENT, values);

    }

    @Override
    protected Long doInBackground(Void... params) {
        Uri result = insertPatient();
        return Constant.getIdFromUri(result);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        mCallback.onPatientInserted(result);
    }

    public interface OnPatientInserted {
        void onPatientInserted(long id);
    }
}
