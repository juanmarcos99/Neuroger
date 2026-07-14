package software.cneuro.neuroger.ui.input.subject;

import static android.app.Activity.RESULT_OK;
import static software.cneuro.neuroger.content.CalendarHelper.dateFormatYearFirst;
import static software.cneuro.neuroger.content.CalendarHelper.parseCalendar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.PreferenceManager;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.SubjectGeneralEvaluationActivity;
import software.cneuro.neuroger.ui.adapter.InputSpinnerAdapter;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neuroger.ui.dialog.TimePickerFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database.DatabaseUpdater;
import software.cneuro.neurogerdatabase.database_async.InsertPatient_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePatient_AsyncTask;

/**
 * A placeholder fragment containing the subject input info.
 */
public class SubjectAddEditFragment extends Fragment implements
        TimePickerFragment.TimePickerCallbacks,
        InsertPatient_AsyncTask.OnPatientInserted,
        View.OnClickListener,
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks,
        LoaderManager.LoaderCallbacks<Cursor>,
        UpdatePatient_AsyncTask.OnPatientUpdated {
    public static final String ARGUMENT_EDIT_PATIENT_ID = "edit_patient_id";
    private static final String ARG_VERSION = "version";

    static final int LOADER_SEARCH_SUBJECT_BY_ID = 123;
    private static final int CI_BIRTH_MISMATCH_DIALOG_ID = 890;
    static final int LOADER_SEARCH_SUBJECT_BY_CI = 134;

    // These are the Subjects rows to be retrieve.
    public static final String[] SUBJECTS_SHORT_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_NAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_LASTNAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID_NUMBER,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_SEX};

    static final String[] SUBJECTS_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_NAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_LASTNAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID_NUMBER,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_BIRTHDATE,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_SEX,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_YEARS_STUDIES,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_CIVIL_STATUS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_HOSPITAL,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ORIGIN,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_CLINIC_CLASSIFICATION,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PROFESSION,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_COUNTRY,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PROVINCE_STATE,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_MUNICIPALITY,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ADDRESS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_SKIN_COLOR,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_OCCUPATION,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_COEXISTENCE,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PHONE_NUMBER,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_VERSION};

    private long mSubjectId = Constant.NO_ID;

    private EditText mName, mLastName, mID, mProfession, mAddress, mPhone;
    private Button mBirthDate;
    private RadioButton mRdbMale;
    private Spinner mSkinColor,
            mOccupation,
            mCoexistence,
            mStudyYears,
            mCivilStatus,
            mHospital,
            mOrigin,
            mClassification,
            mCountry,
            mProvince,
            mMunicipality;
    private int mSkinColorPosSelected,
            mOccupationPosSelected,
            mCoexistencePosSelected,
            mStudyYearsPosSelected,
            mCivilPosSelected,
            mHospitalPosSelected,
            mOriginPosSelected,
            mClassificationPosSelected,
            mCountryPosSelected,
            mProvincePosSelected,
            mMunicipalityPosSelected;
    private Date mDate;

    private PreferenceManager mPreferenceManager;
    private int mVersion;

    public SubjectAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getContext() != null;
        mPreferenceManager = new PreferenceManager(getContext().getApplicationContext());

        mHospitalPosSelected = mPreferenceManager.getPreferenceHospital();
        mCountryPosSelected = mPreferenceManager.getPreferenceCountry();
        mProvincePosSelected = mPreferenceManager.getPreferenceProvince();
        mMunicipalityPosSelected = mPreferenceManager.getPreferenceMunicipality();

        if (getArguments() != null && getArguments().containsKey(ARGUMENT_EDIT_PATIENT_ID)) {
            mSubjectId = getArguments().getLong(ARGUMENT_EDIT_PATIENT_ID, Constant.NO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subject_input, container, false);

        mName = rootView.findViewById(R.id.edit_subject_name);
        mLastName = rootView.findViewById(R.id.edit_subject_last_name);
        mID = rootView.findViewById(R.id.edit_subject_id);
        mProfession = rootView.findViewById(R.id.edit_subject_profession);
        mAddress = rootView.findViewById(R.id.edit_subject_address);
        mPhone = rootView.findViewById(R.id.edit_subject_phone);

        mBirthDate = rootView.findViewById(R.id.btn_subject_birth);
        mBirthDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Calendar calendar = CalendarHelper.getInitialDate();
                if (mDate != null) {
                    calendar.setTime(mDate);
                }
                showDialogDateFragment(calendar.getTime());
            }
        });

        mRdbMale = rootView.findViewById(R.id.rdb_male);

        mOccupation = rootView.findViewById(R.id.spinner_subject_occupation);
        mOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOccupationPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSkinColor = rootView.findViewById(R.id.spinner_subject_skin_color);
        mSkinColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSkinColorPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mStudyYears = rootView.findViewById(R.id.spinner_subject_study_years);
        mStudyYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStudyYearsPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCivilStatus = rootView.findViewById(R.id.spinner_subject_civil_status);
        mCivilStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCivilPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCoexistence = rootView.findViewById(R.id.spinner_subject_coexistence);
        mCoexistence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCoexistencePosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mHospital = rootView.findViewById(R.id.spinner_subject_hospital);
        mHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHospitalPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mOrigin = rootView.findViewById(R.id.spinner_subject_provenance);
        mOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOriginPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mClassification = rootView.findViewById(R.id.spinner_subject_classification);
        mClassification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClassificationPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCountry = rootView.findViewById(R.id.spinner_subject_country);
        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCountryPosSelected = position;

                assert getActivity() != null;
                mProvince.setAdapter(new InputSpinnerAdapter(getActivity(),
                        R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                        SubjectHelper.getProvince(getActivity(), mCountryPosSelected)));
                mProvince.setSelection(mProvincePosSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mProvince = rootView.findViewById(R.id.spinner_subject_province);
        mProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProvincePosSelected = position;

                assert getActivity() != null;
                mMunicipality.setAdapter(new InputSpinnerAdapter(getActivity(),
                        R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                        SubjectHelper.getMunicipality(getActivity(), mCountryPosSelected, mProvincePosSelected)));
                mMunicipality.setSelection(mMunicipalityPosSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMunicipality = rootView.findViewById(R.id.spinner_subject_municipality);
        mMunicipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMunicipalityPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onDataSet(CalendarHelper.getInitialDate().getTime());

        assert getActivity() != null;
        mOccupation.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.occupation));

        mSkinColor.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.skin_color));

        mStudyYears.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.level_of_schooling));

        mCivilStatus.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.civil_state));

        mCoexistence.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.coexistence));

        mHospital.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.hospitals));
        mHospital.setSelection(mHospitalPosSelected);

        mOrigin.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.origin));
        mOrigin.setSelection(mOriginPosSelected);

        mClassification.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.clinic_classification));
        mClassification.setSelection(mClassificationPosSelected);

        mCountry.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.country));
        mCountry.setSelection(mCountryPosSelected);

        mProvince.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                SubjectHelper.getProvince(getActivity(), mCountryPosSelected)));
        mProvince.setSelection(mProvincePosSelected);

        mMunicipality.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                SubjectHelper.getMunicipality(getActivity(), mCountryPosSelected, mProvincePosSelected)));
        mMunicipality.setSelection(mMunicipalityPosSelected);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_VERSION,
                mVersion);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            if (!isNewPatient()) {
                populatePatient();
            }
        } else {
            mVersion = savedInstanceState.getInt(ARG_VERSION);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PATIENT;

        String[] projection = SUBJECTS_SUMMARY_PROJECTION;
        String selection = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID + " = " + mSubjectId + " ))";
        if (id == LOADER_SEARCH_SUBJECT_BY_CI) {
            projection = SUBJECTS_SHORT_SUMMARY_PROJECTION;
            selection = software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID_NUMBER + "=" + mID.getText().toString();
        }

        assert getActivity() != null;
        return new CursorLoader(getActivity(), baseUri, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == LOADER_SEARCH_SUBJECT_BY_ID) {
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_NAME
                        ));
                String lastName = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_LASTNAME
                        ));
                String id = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID_NUMBER
                        ));
                String birth = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_BIRTHDATE
                        ));
                String profession = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PROFESSION
                        ));
                String skinColor = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_SKIN_COLOR
                        ));
                int genderId = cursor.getInt(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_SEX
                        ));
                String studyYears = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_YEARS_STUDIES
                        ));
                String occupation = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_OCCUPATION
                        ));
                String civilStatus = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_CIVIL_STATUS
                        ));
                String coexistence = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_COEXISTENCE
                        ));
                String hospital = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_HOSPITAL
                        ));
                String origin = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ORIGIN
                        ));
                String classification = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_CLINIC_CLASSIFICATION
                        ));
                String country = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_COUNTRY
                        ));
                String province = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PROVINCE_STATE
                        ));
                String municipality = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_MUNICIPALITY
                        ));
                String address = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ADDRESS
                        ));
                String phone = cursor.getString(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_PHONE_NUMBER
                        ));
                mVersion = cursor.getInt(
                        cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_VERSION
                        ));

                updateContent(id, name, lastName, birth, skinColor, genderId, studyYears, profession, occupation,
                        civilStatus, coexistence, hospital,
                        origin, classification, country, province, municipality,
                        address, phone);
            }
        } else {
            // only inserts if the subject doesn't exist
            if (cursor != null && cursor.moveToFirst()) {
                // needs to make sure it doesn't get call again when the subject is inserted,
                // otherwise when the data change the loader get notified and the Toast of
                // subject exist will be shown.
                getLoaderManager().destroyLoader(loader.getId());

                showSubjectExistDialog();
            } else {
                DatabaseInserter.insertPatient(getActivity(),
                        StringHelper.formatName(mName.getText().toString()),
                        StringHelper.formatName(mLastName.getText().toString()),
                        mID.getText().toString(),
                        SubjectHelper.getGuid(),
                        /*CalendarHelper.getDateToSaveSQLite(mDate),*/
                        getBirthdayDate(),
                        getGenderId(),
                        String.valueOf(mOccupationPosSelected),
                        String.valueOf(mStudyYearsPosSelected),
                        String.valueOf(mCivilPosSelected),
                        String.valueOf(mHospitalPosSelected),
                        String.valueOf(mOriginPosSelected),
                        String.valueOf(mClassificationPosSelected),
                        String.valueOf(mCountryPosSelected),
                        String.valueOf(mProvincePosSelected),
                        String.valueOf(mMunicipalityPosSelected),
                        mAddress.getText().toString(),
                        String.valueOf(mSkinColorPosSelected),
                        StringHelper.getHintIfEmpty(mProfession),
                        String.valueOf(mCoexistencePosSelected),
                        mPhone.getText().toString(),
                        Constant.SUBJECT_COMPENSATED_ID,
                        this);
            }
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onDataSet(Date date) {
        mDate = date;
        mBirthDate.setText(CalendarHelper.dateFormat.format(date));

        //mID.setText(CalendarHelper.dateFormatCI.format(date));
    }

    @Override
    public void onClick(View v) {
        saveContent();
    }

    @Override
    public void onPatientInserted(long id) {
        sendToActivity(id);
    }

    @Override
    public void onPatientUpdated(long id) {
        sendToActivity(id);
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == CI_BIRTH_MISMATCH_DIALOG_ID) {
            saveToDb();
        }
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    private void updateContent(String id,
                               String name,
                               String lastName,
                               String birth,
                               String skinColor,
                               int genderId,
                               String studyYears,
                               String profession,
                               String occupation,
                               String civilStatus,
                               String coexistence,
                               String hospital,
                               String origin,
                               String classification,
                               String country,
                               String province,
                               String municipality,
                               String address,
                               String phone) {
        mName.setText(name);
        mLastName.setText(lastName);
        mID.setText(id);
        onDataSet(parseCalendar(birth, dateFormatYearFirst).getTime());

        if (!profession.isEmpty() && !profession.equals(getString(R.string.editText_profession_hint)))
            mProfession.setText(profession);

        mAddress.setText(address);
        mPhone.setText(phone);

        mRdbMale.setChecked(genderId == Constant.SUBJECT_MALE_ID);

        mOccupationPosSelected = EvaluationPPHelper.zeroIfEmpty(occupation);
        mOccupation.setSelection(mOccupationPosSelected);

        mSkinColorPosSelected = EvaluationPPHelper.zeroIfEmpty(skinColor);
        mSkinColor.setSelection(mSkinColorPosSelected);

        mStudyYearsPosSelected = EvaluationPPHelper.zeroIfEmpty(studyYears);
        mStudyYears.setSelection(mStudyYearsPosSelected);

        mCivilPosSelected = EvaluationPPHelper.zeroIfEmpty(civilStatus);
        mCivilStatus.setSelection(mCivilPosSelected);

        mCoexistencePosSelected = EvaluationPPHelper.zeroIfEmpty(coexistence);
        mCoexistence.setSelection(mCoexistencePosSelected);

        mHospitalPosSelected = EvaluationPPHelper.zeroIfEmpty(hospital);
        mHospital.setSelection(mHospitalPosSelected);

        mOriginPosSelected = EvaluationPPHelper.zeroIfEmpty(origin);
        mOrigin.setSelection(mOriginPosSelected);

        mClassificationPosSelected = EvaluationPPHelper.zeroIfEmpty(classification);
        mClassification.setSelection(mClassificationPosSelected);

        mCountryPosSelected = EvaluationPPHelper.zeroIfEmpty(country);
        mCountry.setSelection(mCountryPosSelected);

        mProvincePosSelected = EvaluationPPHelper.zeroIfEmpty(province);
        mProvince.setSelection(mProvincePosSelected);

        mMunicipalityPosSelected = EvaluationPPHelper.zeroIfEmpty(municipality);
        mMunicipality.setSelection(mMunicipalityPosSelected);
    }

    private void populatePatient() {
        if (isNewPatient()) {
            throw new RuntimeException("populatePatient() was called but patient is new.");
        }
        getLoaderManager().initLoader(LOADER_SEARCH_SUBJECT_BY_ID, null, this);
    }

    private void showDialogDateFragment(Date date) {
        assert getActivity() != null;
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();

        TimePickerFragment dateDialog = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(TimePickerFragment.ARG_DATE, date);
        dateDialog.setArguments(args);

        dateDialog.setOnDialogDateCallbacks(this);

        dateDialog.show(fm, "fragment_date_dialog");
    }

    private void showSubjectExistDialog() {
        assert getActivity() != null;
        FragmentManager fm = getActivity().getSupportFragmentManager();

        HelpFragment dialogFragment = HelpFragment
                .newInstance(getString(R.string.dialog_subject_exist),
                        getString(R.string.dialog_subject_exist_message));
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    /**
     * Saves the new subject.
     */
    public void saveContent() {
        if (validateEntries()) {
            /*if (!SubjectHelper.matchWithBirth(mDate, mID.getText().toString())) {
                FragmentManager fm = getChildFragmentManager();
                AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                        getString(R.string.dialog_ci_birth_mismatch_title),
                        getString(R.string.dialog_ci_birth_mismatch_message), CI_BIRTH_MISMATCH_DIALOG_ID);
                dialogFragment.setAskBeforeLeavingCallbacks(this);
                dialogFragment.show(fm, "fragment_dialog");
            } else {
                saveToDb();
            }*/
            saveToDb();
        } else {
            assert getActivity() != null;
            Toast.makeText(getActivity(), getString(R.string.toast_empty_field), Toast.LENGTH_LONG).show();
        }

    }

    private void saveToDb() {
        if (isNewPatient()) {
            // make sure it doesn't exist already in the db
            getLoaderManager().initLoader(LOADER_SEARCH_SUBJECT_BY_CI, null, this);
        } else {
            assert getActivity() != null;
            DatabaseUpdater.updatePatient(getActivity(),
                    mSubjectId,
                    StringHelper.formatName(mName.getText().toString()),
                    StringHelper.formatName(mLastName.getText().toString()),
                    mID.getText().toString(),
                    getBirthdayDate(),
                    getGenderId(),
                    String.valueOf(mOccupationPosSelected),
                    String.valueOf(mStudyYearsPosSelected),
                    String.valueOf(mCivilPosSelected),
                    String.valueOf(mHospitalPosSelected),
                    String.valueOf(mOriginPosSelected),
                    String.valueOf(mClassificationPosSelected),
                    String.valueOf(mCountryPosSelected),
                    String.valueOf(mProvincePosSelected),
                    String.valueOf(mMunicipalityPosSelected),
                    mAddress.getText().toString(),
                    String.valueOf(mSkinColorPosSelected),
                    StringHelper.getHintIfEmpty(mProfession),
                    String.valueOf(mCoexistencePosSelected),
                    mPhone.getText().toString(),
                    mVersion,
                    this);
        }

        saveHospital(mHospitalPosSelected);
        saveLocation(mCountryPosSelected, mProvincePosSelected, mMunicipalityPosSelected);
    }

    private void saveHospital(int hospitalPos) {
        mPreferenceManager.setPreferenceHospital(hospitalPos);
    }

    public void saveLocation(int countryPos, int provincePos, int municipalityPos) {
        // save country selection.
        mPreferenceManager.setPreferenceCountry(countryPos);
        // save province selection.
        mPreferenceManager.setPreferenceProvince(provincePos);
        // save municipality selection.
        mPreferenceManager.setPreferenceMunicipality(municipalityPos);
    }

    public boolean isNewPatient() {
        return mSubjectId == Constant.NO_ID;
    }

    private void sendToActivity(long id) {
        assert getActivity() != null;
        Intent intent = new Intent(getActivity(), SubjectGeneralEvaluationActivity.class);
        String message = getString(R.string.toast_save_subject);
        if (getActivity().getIntent().hasExtra(SubjectAddEditFragment.ARGUMENT_EDIT_PATIENT_ID)) {
            message = getString(R.string.toast_update_subject);

            intent = new Intent();
            fillIntent(intent,
                    id,
                    false,
                    StringHelper.formatName(mName.getText().toString()),
                    StringHelper.formatName(mLastName.getText().toString()),
                    mPhone.getText().toString(),
                    getAge(),
                    getBirthdayDate(),
                    mRdbMale.isChecked() ? Constant.SUBJECT_FEMALE_ID : Constant.SUBJECT_MALE_ID);
            getActivity().setResult(RESULT_OK, intent);
        } else {
            fillIntent(intent, id,
                    false,
                    StringHelper.formatName(mName.getText().toString()),
                    StringHelper.formatName(mLastName.getText().toString()),
                    mPhone.getText().toString(),
                    getAge(),
                    getBirthdayDate(),
                    mRdbMale.isChecked() ? Constant.SUBJECT_FEMALE_ID : Constant.SUBJECT_MALE_ID);
            startActivity(intent);
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    private void fillIntent(Intent intent,
                            long id,
                            boolean newSubject,
                            String name,
                            String lastName,
                            String phone,
                            String age,
                            String birth,
                            int gender) {
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_ID, id);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_NEW_SUBJECT, newSubject);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_ID, id);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_NAME, name);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_LAST_NAME, lastName);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_PHONE, phone);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_AGE, age);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_BIRTH, birth);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_GENDER, gender);
    }

    private boolean validateEntries() {
        if (TextUtils.isEmpty(mName.getText())) {
            mName.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mLastName.getText())) {
            mLastName.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mAddress.getText())) {
            mAddress.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mID.getText())) {
            mID.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (mID.getText().length() != 11) {
            mID.setError(getString(R.string.toast_id_invalid_value));
            return false;
        } else {
            try {
                getAge();
            } catch (IllegalArgumentException e) {
                mID.setError(getString(R.string.toast_invalid_ci_value));
                return false;
            }
        }

        return true;
    }

    private int getGenderId() {
        return mRdbMale.isChecked() ? Constant.SUBJECT_MALE_ID
                : Constant.SUBJECT_FEMALE_ID;
    }

    private String getBirthdayDate() {
        return CalendarHelper.getBirthDateFromCIToSaveSQLite(mID.getText().toString());
    }

    private String getAge() {
        assert getActivity() != null;
        return SubjectHelper.getAge(getActivity(), getBirthdayDate());
    }
}























