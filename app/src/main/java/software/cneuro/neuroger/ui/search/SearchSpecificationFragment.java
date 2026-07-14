package software.cneuro.neuroger.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.adapter.GroupAgeSpinnerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSearchFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SearchSpecificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchSpecificationFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    private static final String ARG_NAME_TO_SEARCH = "name_to_search";
    private static final String ARG_GROUP_AGE_POS_SELECTED = "selected_group_age";

    private EditText mEditTextName;
    private Spinner mGroupAge;
    private RadioButton mRdbFemale, mRdbMale;
    private CheckBox mCbxGender;

    private String mName;
    private int mGrouAgePosSelected;

    private OnSearchFragmentListener mListener;
    private boolean mGroupAgeSpinnerEnable;

    public SearchSpecificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchSpecificationFragment.
     */
    public static SearchSpecificationFragment newInstance(String param1, String param2) {
        return new SearchSpecificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_specification, container, false);

        mEditTextName = rootView.findViewById(R.id.new_list_name);
        mEditTextName.addTextChangedListener(this);

        CheckBox cbxAgeGroup = rootView.findViewById(R.id.cbx_age_group);
        mCbxGender = rootView.findViewById(R.id.cbx_gender);
        cbxAgeGroup.setOnCheckedChangeListener(this);
        mCbxGender.setOnCheckedChangeListener(this);

        mGroupAge = rootView.findViewById(R.id.spinner_age_group);
        mGroupAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mGrouAgePosSelected != position) {
                    mGrouAgePosSelected = position;

                    if (mListener != null) {
                        int[] range = ((GroupAgeSpinnerAdapter) mGroupAge.getAdapter()).getAgesRange(position);
                        mListener.onSearchByGroupAge(range[0], range[1], mGroupAgeSpinnerEnable);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mGroupAge.setEnabled(false);

        mRdbMale = rootView.findViewById(R.id.rdb_male);
        mRdbMale.setOnCheckedChangeListener(this);
        mRdbFemale = rootView.findViewById(R.id.rdb_female);
        mRdbFemale.setChecked(true);
        mRdbFemale.setOnCheckedChangeListener(this);
        mRdbFemale.setEnabled(false);
        mRdbMale.setEnabled(false);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_NAME_TO_SEARCH, mName);
        outState.putInt(ARG_GROUP_AGE_POS_SELECTED,
                mGrouAgePosSelected);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mName = savedInstanceState.getString(ARG_NAME_TO_SEARCH);
            mGrouAgePosSelected = savedInstanceState
                    .getInt(ARG_GROUP_AGE_POS_SELECTED);
        }

        updateContent();
    }

    private void updateContent() {
        if (!TextUtils.isEmpty(mName)) {
            mEditTextName.setText(StringHelper.isEmpty(mName));
        }

        mGroupAge.setAdapter(new GroupAgeSpinnerAdapter(getActivity(),
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.age_group));
        mGroupAge.setSelection(mGrouAgePosSelected);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mName = s.toString();

        if (mListener != null) {
            mListener.onSearchByName(mName);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.cbx_age_group:
                mGroupAgeSpinnerEnable = isChecked;
                mGroupAge.setEnabled(isChecked);
                if (mListener != null) {
                    int[] range = ((GroupAgeSpinnerAdapter) mGroupAge.getAdapter()).getAgesRange(mGrouAgePosSelected);
                    mListener.onSearchByGroupAge(range[0], range[1], isChecked);
                }
                break;
            case R.id.cbx_gender:
                mRdbFemale.setEnabled(isChecked);
                mRdbMale.setEnabled(isChecked);
                if (mListener != null) {
                    mListener.onSearchByGender(Constant.SUBJECT_FEMALE_ID, isChecked);
                }
                break;
            case R.id.rdb_female:
            case R.id.rdb_male:
                if (mListener != null) {
                    mListener.onSearchByGender(Constant.SUBJECT_MALE_ID, mCbxGender.isChecked());
                }
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSearchFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
                    + " must implement OnSearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSearchFragmentListener {
        void onSearchByName(String name);

        void onSearchByGender(int isFemale, boolean isChecked);

        void onSearchByGroupAge(int from, int to, boolean isChecked);
    }

}
