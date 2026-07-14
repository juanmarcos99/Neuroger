package software.cneuro.neuroger.ui.input.evaluation;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationSubValHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectiveHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neuroger.ui.input.BaseWarningFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertSubEvaluationTest_AsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubjectiveEvaluationInputFragment extends BaseWarningFragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        InsertSubEvaluationTest_AsyncTask.OnSubEvaluationTestInserted {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ACTUAL_MEMORY_FLAG = "actual_memory";
    public static final String PAST_MEMORY_FLAG = "past_memory";
    protected static final int MISSING_ANSWERS_DIALOG_ID = 1;
    private static final String ACTUAL_HEALTH_FLAG = "health_flag";
    private static final String ACTUAL_DIFFICULTIES_FLAG = "difficulties_flag";

    private long mSubjectId;
    private SparseBooleanArray mSelected;
    // temporal saves the buttons states for check and unchecked
    private SparseBooleanArray mTemp;

    private RadioGroup mRg1Card0, mRg1Card1, mRg2Card1;
    private boolean mHealthFlag, mBadMemoryFlag, mPastMemoryFlag, mDifficultiesFlag;

    public SubjectiveEvaluationInputFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
        }

        mSelected = new SparseBooleanArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjective_evaluation_input, container, false);

        mRg1Card0 = rootView.findViewById(R.id.subject_radiogroup_0_card_1);
        mRg1Card1 = rootView.findViewById(R.id.subject_radiogroup_1_card_1);
        mRg2Card1 = rootView.findViewById(R.id.subject_radiogroup_2_card_1);

        ((RadioButton) rootView.findViewById(R.id.rb_regular)).setOnCheckedChangeListener(this);
        ((RadioButton) rootView.findViewById(R.id.rb_bad)).setOnCheckedChangeListener(this);
        ((RadioButton) rootView.findViewById(R.id.rb_worst_than_before)).setOnCheckedChangeListener(this);

        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_1)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_2)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_3)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_4)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_5)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_6)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.card_2_cbx_7)).setOnCheckedChangeListener(this);

        rootView.findViewById(R.id.card_2_help_icon_1).setOnClickListener(this);
        rootView.findViewById(R.id.card_2_help_icon_2).setOnClickListener(this);
        rootView.findViewById(R.id.card_2_help_icon_3).setOnClickListener(this);
        rootView.findViewById(R.id.card_2_help_icon_4).setOnClickListener(this);
        rootView.findViewById(R.id.card_2_help_icon_5).setOnClickListener(this);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
        outState.putBoolean(ACTUAL_MEMORY_FLAG, mBadMemoryFlag);
        outState.putBoolean(PAST_MEMORY_FLAG, mPastMemoryFlag);
        outState.putBoolean(ACTUAL_HEALTH_FLAG, mHealthFlag);
        outState.putBoolean(ACTUAL_DIFFICULTIES_FLAG, mDifficultiesFlag);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
            mHealthFlag = savedInstanceState.getBoolean(ACTUAL_HEALTH_FLAG);
            mBadMemoryFlag = savedInstanceState.getBoolean(ACTUAL_MEMORY_FLAG);
            mPastMemoryFlag = savedInstanceState.getBoolean(PAST_MEMORY_FLAG);
            mDifficultiesFlag = savedInstanceState.getBoolean(ACTUAL_DIFFICULTIES_FLAG);
        }
    }

    @Override
    public void onTestInserted() {
        assert getActivity() != null;

        Toast.makeText(getActivity(), getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_2_help_icon_1:
                showHelp(Constant.HELP_SV_1);
                break;
            case R.id.card_2_help_icon_2:
                showHelp(Constant.HELP_SV_2);
                break;
            case R.id.card_2_help_icon_3:
                showHelp(Constant.HELP_SV_3);
                break;
            case R.id.card_2_help_icon_4:
                showHelp(Constant.HELP_SV_4);
                break;
            case R.id.card_2_help_icon_5:
                showHelp(Constant.HELP_SV_5);
                break;
            case R.id.btn_action:
                if (validateTest())
                    showCheckDateTestBeforeLeavingMessage();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_worst_than_before:
            case R.id.rb_regular:
            case R.id.rb_bad:
                mBadMemoryFlag = isChecked;
                break;
            default:
                // if we selected the "No reporta", deselected the previous checkboxes.
                if (buttonView.getId() == R.id.card_2_cbx_7 && isChecked) {
                    mDifficultiesFlag = false;
                    mTemp = mSelected.clone();
                    deselectCheckboxes();
                    resetSelectionAddNoReport(buttonView.getId());
                    return;
                }
                if (isChecked) {
                    mDifficultiesFlag = true;
                    mSelected.put(buttonView.getId(), true);

                    if (getView() != null)
                        ((CheckBox) getView().findViewById(R.id.card_2_cbx_7)).setChecked(false);
                } else {
                    mSelected.delete(buttonView.getId());
                }
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private int getCbxPosition(int id) {
        switch (id) {
            case R.id.card_2_cbx_1:
                return 0;
            case R.id.card_2_cbx_2:
                return 1;
            case R.id.card_2_cbx_3:
                return 2;
            case R.id.card_2_cbx_4:
                return 3;
            case R.id.card_2_cbx_5:
                return 4;
            case R.id.card_2_cbx_6:
                return 5;
            case R.id.card_2_cbx_7:
                return 6;
        }
        return 0;
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == CHECK_TEST_BEFORE_LEAVING_DIALOG_ID)
            saveContent();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    // verify if at least there is one selected radiobutton.
    private boolean validateTest() {
        if (mRg1Card0.getCheckedRadioButtonId() == -1 &&
                mRg1Card1.getCheckedRadioButtonId() == -1 &&
                mRg2Card1.getCheckedRadioButtonId() == -1) {
            showErrorMessage(getString(R.string.dialog_missing_answer_message));
            return false;
        }
        if (getItemsSelected().size() == 0) {
            showErrorMessage(getString(R.string.dialog_dont_report_message));
            return false;
        }
        return true;
    }

    private void resetSelectionAddNoReport(int value) {
        mSelected = new SparseBooleanArray();
        mSelected.put(value, true);
    }

    private void deselectCheckboxes() {
        if (getView() == null)
            return;

        for (int i = 0; i < mTemp.size(); i++) {
            View view = getView().findViewById(mTemp.keyAt(i));
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(false);
            }
        }
    }

    private ArrayList<Integer> getItemsSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < mSelected.size(); i++) {
            result.add(getCbxPosition(mSelected.keyAt(i)));
        }
        return result;
    }

    private void saveContent() {
        if (getView() != null) {
            int card1Answer0 = mRg1Card0.indexOfChild(getView().findViewById(mRg1Card0.getCheckedRadioButtonId()));
            int card1Answer1 = mRg1Card1.indexOfChild(getView().findViewById(mRg1Card1.getCheckedRadioButtonId()));
            int card1Answer2 = mRg2Card1.indexOfChild(getView().findViewById(mRg2Card1.getCheckedRadioButtonId()));

            DatabaseInserter.insertSubEvaluationTest(getActivity(), mSubjectId, card1Answer0, card1Answer1, card1Answer2,
                    StringHelper.getJsonString(getItemsSelected()), 0, 0, 0,
                    EvaluationSubValHelper.evaluate(mBadMemoryFlag, card1Answer0, mDifficultiesFlag),
                    this);
        }
    }

    public void showErrorMessage(String message) {
        FragmentManager fm = getChildFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_ask_before_leaving_title),
                message, MISSING_ANSWERS_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    public void showHelp(int helpID) {
        assert getActivity() != null;
        String text = SubjectiveHelper.getHelpText(getActivity(), helpID);
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                text);
        dialogFragment.show(fm, "fragment_dialog");
    }
}
