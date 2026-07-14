package software.cneuro.neuroger.ui.input.carer;

import static android.app.Activity.RESULT_OK;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.adapter.InputSpinnerAdapter;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database.DatabaseUpdater;
import software.cneuro.neurogerdatabase.database_async.InsertCohabitant_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdateCohabitant_AsyncTask;

/**
 * A placeholder fragment containing the carer input info.
 */
public class CarerAddEditFragment extends Fragment implements
        InsertCohabitant_AsyncTask.OnCohabitantInserted,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, UpdateCohabitant_AsyncTask.OnCohabitantUpdated {
    public static final String ARGUMENT_EDIT_CARER_ID = "edit_carer_id";

    public static final String ARG_SUBJECT_ID = "subject_id";
    private static final String ARG_FAMILIARITY_POS_SELECTED = "selected_familiarity";

    private long mSubjectId;
    private long mCarerId = Constant.NO_ID;

    private EditText mName, mLastName, mAge, mPhone;
    private RadioButton mRdbFemale;
    private Spinner mFamiliarity;

    private int mFamiliarityPosSelected;

    public CarerAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID, Constant.NO_ID);
            }
            if (getArguments().containsKey(ARGUMENT_EDIT_CARER_ID)) {
                mCarerId = getArguments().getLong(ARGUMENT_EDIT_CARER_ID, Constant.NO_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_carer_input, container, false);

        mName = rootView.findViewById(R.id.edit_carer_name);
        mLastName = rootView.findViewById(R.id.edit_carer_last_name);
        mAge = rootView.findViewById(R.id.edit_carer_age);
        mPhone = rootView.findViewById(R.id.edit_carer_phone);

        mRdbFemale = rootView.findViewById(R.id.rdb_female);
        mRdbFemale.setChecked(true);

        mFamiliarity = rootView.findViewById(R.id.spinner_carer_familiarity);
        mFamiliarity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFamiliarityPosSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        assert getActivity() != null;
        mFamiliarity.setAdapter(new InputSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.familiarity));
        mFamiliarity.setSelection(mFamiliarityPosSelected);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_FAMILIARITY_POS_SELECTED,
                mFamiliarityPosSelected);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            if (!isNewCarer()) {
                populateCarer();
            }
        }
    }

    @Override
    public void onClick(View v) {
        saveContent();
    }

    @Override
    public void onCohabitantInserted(long id) {
        // Send callback to the activity
        sendToActivity(id);
    }

    private boolean isNewCarer() {
        return mCarerId == Constant.NO_ID;
    }

    private void populateCarer() {
        if (isNewCarer()) {
            throw new RuntimeException("populateCarer() was called but carer is new.");
        }
        getLoaderManager().initLoader(0, null, this);
    }

    public void saveContent() {
        if (validateEntries()) {
            saveToDb();
        } else {
            Toast.makeText(getActivity(), getString(R.string.toast_empty_field), Toast.LENGTH_LONG).show();
        }
    }

    private void saveToDb() {
        if (isNewCarer()) {
            DatabaseInserter.insertCohabitant(getActivity(),
                    mSubjectId,
                    StringHelper.formatName(mName.getText().toString()),
                    StringHelper.formatName(mLastName.getText().toString()),
                    mAge.getText().toString(),
                    mRdbFemale.isChecked() ? Constant.SUBJECT_FEMALE_ID : Constant.SUBJECT_MALE_ID,
                    String.valueOf(mFamiliarityPosSelected),
                    mPhone.getText().toString(),
                    this);
        } else {
            DatabaseUpdater.updateCohabitant(getActivity(),
                    mCarerId,
                    mSubjectId,
                    StringHelper.formatName(mName.getText().toString()),
                    StringHelper.formatName(mLastName.getText().toString()),
                    mAge.getText().toString(),
                    mRdbFemale.isChecked() ? Constant.SUBJECT_FEMALE_ID : Constant.SUBJECT_MALE_ID,
                    String.valueOf(mFamiliarityPosSelected),
                    mPhone.getText().toString(),
                    this);
        }
    }

    private void sendToActivity(long id) {
        // Send callback to the activity
        /*mListener.onCarerSaved(id, SubjectHelper.getName(mName.getText().toString(), mLastName.getText().toString(), false),
                StringHelper.isEmpty(mPhone.getText().toString()),
                StringHelper.isEmpty(mAge.getText().toString()),
                mRdbFemale.isChecked() ? Constant.SUBJECT_FEMALE_ID : Constant.SUBJECT_MALE_ID,
                StringHelper.isEmpty(mFamiliarity.getItemAtPosition(mFamiliarityPosSelected).toString()));*/

        assert getActivity() != null;
        String message = getString(R.string.toast_save_carer);
        if (getActivity().getIntent().hasExtra(CarerAddEditFragment.ARGUMENT_EDIT_CARER_ID)) {
            message = getString(R.string.toast_update_carer);
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private boolean validateEntries() {
        if (TextUtils.isEmpty(mName.getText())) {
            mName.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mLastName.getText())) {
            mLastName.setError(getString(R.string.toast_invalid_value));
            return false;
        }

        return true;
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CARER_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_NAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_LASTNAME,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_AGE,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_SEX,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_FAMILIARITY,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_PHONE_NUMBER,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_FULLNAME};

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_COHABITANT;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                CARER_SUMMARY_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_NAME
                    ));
            String lastName = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_LASTNAME
                    ));
            String age = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_AGE
                    ));
            String genderId = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_SEX
                    ));
            String familiarity = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_FAMILIARITY
                    ));
            String phone = cursor.getString(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COHABITANT_PHONE_NUMBER
                    ));

            updateContent(name,
                    lastName,
                    age,
                    genderId,
                    familiarity,
                    phone);
        }
    }

    private void updateContent(String name,
                               String lastName,
                               String age,
                               String genderId,
                               String familiarity,
                               String phone) {
        mName.setText(name);
        mLastName.setText(lastName);
        mAge.setText(age);

        mRdbFemale.setChecked(EvaluationPPHelper.zeroIfEmpty(genderId) == Constant.SUBJECT_FEMALE_ID);

        mFamiliarityPosSelected = EvaluationPPHelper.zeroIfEmpty(familiarity);
        mFamiliarity.setSelection(mFamiliarityPosSelected);

        mPhone.setText(phone);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void OnCohabitantUpdated(long _id) {
        sendToActivity(_id);
    }
}
