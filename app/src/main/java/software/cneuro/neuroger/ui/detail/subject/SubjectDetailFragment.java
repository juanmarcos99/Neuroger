package software.cneuro.neuroger.ui.detail.subject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.BaseActivity;
import software.cneuro.neuroger.ui.input.subject.SubjectAddEditActivity;
import software.cneuro.neuroger.ui.input.subject.SubjectAddEditFragment;
import software.cneuro.neurogerdatabase.constant.Constant;

public class SubjectDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final int EDIT_ACTIVITY_REQUEST_CODE = 144;

    // These are the Contacts rows that we will retrieve.
    static final String[] SUBJECTS_SUMMARY_PROJECTION = new String[]{
            Constant.COL_PATIENT_NAME,
            Constant.COL_PATIENT_FULLNAME,
            Constant.COL_PATIENT_LASTNAME,
            Constant.COL_PATIENT_ID_NUMBER,
            Constant.COL_PATIENT_BIRTHDATE,
            Constant.COL_PATIENT_SEX,
            Constant.COL_PATIENT_OCCUPATION,
            Constant.COL_PATIENT_YEARS_STUDIES,
            Constant.COL_PATIENT_CIVIL_STATUS,
            Constant.COL_PATIENT_HOSPITAL,
            Constant.COL_PATIENT_ORIGIN,
            Constant.COL_PATIENT_CLINIC_CLASSIFICATION,
            Constant.COL_PATIENT_COUNTRY,
            Constant.COL_PATIENT_PROVINCE_STATE,
            Constant.COL_PATIENT_MUNICIPALITY,
            Constant.COL_PATIENT_ADDRESS,
            Constant.COL_PATIENT_SKIN_COLOR,
            Constant.COL_PATIENT_PROFESSION,
            Constant.COL_PATIENT_COEXISTENCE,
            Constant.COL_PATIENT_PHONE_NUMBER,
            Constant.COL_PATIENT_COMPENSATED
    };

    private long mSubjectId;
    private String mName;

    private ViewGroup mCardView1;
    private ViewGroup mCardView2;
    private ViewGroup mCardView3;
    private ViewGroup mCardView4;
    private ViewGroup mCardView5;
    private LayoutInflater mInflater;

    public SubjectDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null && getArguments().containsKey(ARG_SUBJECT_ID)) {
            mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            assert getActivity() != null;
            Intent intent = new Intent(getActivity(), SubjectAddEditActivity.class);
            intent.putExtra(SubjectAddEditFragment.ARGUMENT_EDIT_PATIENT_ID, mSubjectId);
            startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subject_detail, container, false);

        mCardView1 = rootView.findViewById(R.id.detail_card_container_1);
        mCardView2 = rootView.findViewById(R.id.detail_card_container_2);
        mCardView3 = rootView.findViewById(R.id.detail_card_container_3);
        mCardView4 = rootView.findViewById(R.id.detail_card_container_4);
        mCardView5 = rootView.findViewById(R.id.detail_card_container_5);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // clean all the data to prepared for the new load
        cleanData();

        getLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constant.URI_TABLE_PATIENT;

        String select = "((" + Constant.COL_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                SUBJECTS_SUMMARY_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            String id = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_ID_NUMBER
                    ));
            mName = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_FULLNAME
                    ));
            assert getActivity() != null;
            ((BaseActivity) getActivity()).updateSubjectFullName(mName);

            String birth = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_BIRTHDATE
                    ));
            String skinColor = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_SKIN_COLOR
                    ));
            int genderId = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_PATIENT_SEX
                    ));
            int compensatedId = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_PATIENT_COMPENSATED
                    ));
            addNewInfo(mCardView1, getString(R.string.label_ci), id);
            addNewInfo(mCardView1, getString(R.string.label_birth), CalendarHelper.getDateToShow(birth));
            addNewInfo(mCardView1, getString(R.string.label_age), String.valueOf(CalendarHelper.getAge(birth)));
            addNewInfo(mCardView1, getString(R.string.label_skin_color), SubjectHelper.getSkinColorText(getContext(),
                    EvaluationPPHelper.zeroIfEmpty(skinColor)));
            addNewInfo(mCardView1, getString(R.string.label_gender), SubjectHelper.getGenderText(getContext(), genderId));
//            addNewInfo(mCardView1, getString(R.string.label_compensated), SubjectHelper.getCompensatedText(getContext(), compensatedId));

            String studyYears = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_YEARS_STUDIES
                    ));
            addNewInfo(mCardView2, getString(R.string.label_study_years),
                    SubjectHelper.getLevelOfSchoolingText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(studyYears)));
            String profession = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_PROFESSION
                    ));
            addNewInfo(mCardView2, getString(R.string.label_profession), profession);
            String occupation = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_OCCUPATION
                    ));
            addNewInfo(mCardView2, getString(R.string.label_occupation),
                    SubjectHelper.getOccupationText(getContext(), EvaluationPPHelper.zeroIfEmpty(occupation)));

            String civilStatus = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_CIVIL_STATUS
                    ));
            addNewInfo(mCardView3, getString(R.string.label_civil_status),
                    SubjectHelper.getCivilStatusText(getContext(), EvaluationPPHelper.zeroIfEmpty(civilStatus)));
            String coexistence = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_COEXISTENCE
                    ));
            addNewInfo(mCardView3, getString(R.string.label_coexistence),
                    SubjectHelper.getCoexistenceText(getContext(), EvaluationPPHelper.zeroIfEmpty(coexistence)));

            String hospital = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_HOSPITAL
                    ));
            String origin = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_ORIGIN
                    ));
            String classification = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_CLINIC_CLASSIFICATION
                    ));

            String country = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_COUNTRY
                    ));
            String province = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_PROVINCE_STATE
                    ));
            String municipality = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_MUNICIPALITY
                    ));
            String address = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_ADDRESS
                    ));
            String phone = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATIENT_PHONE_NUMBER
                    ));

            addNewInfo(mCardView4, getString(R.string.label_hospital),
                    SubjectHelper.getHospitalText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(hospital)));
            addNewInfo(mCardView4, getString(R.string.label_origin),
                    SubjectHelper.getOriginText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(origin)));
//            addNewInfo(mCardView4, getString(R.string.label_clinic_classification),
//                    SubjectHelper.getClassificationText(getContext(),
//                            EvaluationPPHelper.zeroIfEmpty(classification)));

            addNewInfo(mCardView5, getString(R.string.label_country),
                    SubjectHelper.getCountryText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(country)));
            addNewInfo(mCardView5, getString(R.string.label_province),
                    SubjectHelper.getProvinceText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(country),
                            EvaluationPPHelper.zeroIfEmpty(province)));
            addNewInfo(mCardView5, getString(R.string.label_municipality),
                    SubjectHelper.getMunicipalityText(getContext(),
                            EvaluationPPHelper.zeroIfEmpty(country),
                            EvaluationPPHelper.zeroIfEmpty(province),
                            EvaluationPPHelper.zeroIfEmpty(municipality)));
            addNewInfo(mCardView5, getString(R.string.label_address), StringHelper.isEmpty(address));
            addNewInfo(mCardView5, getString(R.string.label_phone), StringHelper.isEmpty(phone));
        }
    }

    private void addNewInfo(ViewGroup container, String title, String text) {
        View infoView = mInflater.inflate(R.layout.detail_text_title,
                container, false);

        TextView titleView = infoView.findViewById(R.id.detail_card_title);
        titleView.setText(title);
        TextView textView = infoView.findViewById(R.id.detail_card_text);
        textView.setText(text);

        container.addView(infoView);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                assert getActivity() != null;
//                getActivity().setResult(RESULT_OK, data);
//                getActivity().finish();
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        }
    }

    private void cleanData() {
        mCardView1.removeAllViews();
        mCardView2.removeAllViews();
        mCardView3.removeAllViews();
        mCardView4.removeAllViews();
        mCardView5.removeAllViews();
    }
}
