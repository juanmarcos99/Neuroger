package software.cneuro.neuroger.ui.input.evaluation;

import static android.app.Activity.RESULT_OK;
import static software.cneuro.neuroger.constant.Constant.FRAIL_DISEASES_ANSWER_WAS_NO;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationFrailHelper;
import software.cneuro.neuroger.content.FrailHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neuroger.ui.input.BaseWarningFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertFrailScale_AsyncTask;
import software.cneuro.neurogertheme.RadioButtonFont;

public class FrailScaleInputFragment extends BaseWarningFragment implements
        View.OnClickListener,
        InsertFrailScale_AsyncTask.OnFrailScaleInserted {
    private static final int BACK_PRESS_DIALOG_ID = 12;
    public static final String ARG_SUBJECT_ID = "subject_id";

    private long mSubjectId;

    private RadioGroup mRgFatigue;
    private RadioGroup mRgResistance;
    private RadioGroup mRgWandering;
    private LinearLayout mLlDiseases;
    private EditText mEditWeightCurrent, mEditWeight1yearAgo;

    private LayoutInflater mInflater;

    private EditText mCurrentOnFocusEditText;
    private int mCurrentOnFocusEditTextPosition;

    private SparseBooleanArray mSelected;

    public FrailScaleInputFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frail_input, container, false);

        // Fatigue
        mRgFatigue = rootView.findViewById(R.id.detail_card_1_rg);

        // Resistance
        mRgResistance = rootView.findViewById(R.id.detail_card_2_rg);

        // Wandering
        mRgWandering = rootView.findViewById(R.id.detail_card_3_rg);

        // Diseases
        mLlDiseases = rootView.findViewById(R.id.detail_card_4_layout);

        // Weight loss
        mEditWeightCurrent = rootView.findViewById(R.id.edit_weight_loss_current);
        mEditWeight1yearAgo = rootView.findViewById(R.id.edit_weight_loss_1_year_ago);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_help, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            showHelp();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

        updateContent();
    }

    @Override
    public void onClick(View v) {
        if (validateTest()) showCheckDateTestBeforeLeavingMessage();
    }

    @Override
    public void onFrailScaleInserted() {
        assert getActivity() != null;

        Toast.makeText(getActivity(), getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == CHECK_TEST_BEFORE_LEAVING_DIALOG_ID)
            saveContent();

    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    private void updateContent() {
        if (getContext() == null) return;

        // Fatigue
        String[] array = FrailHelper.getFatigue(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewRadioButtonInfo(mRgFatigue, array[i], i);
        }
        // Resistance
        array = FrailHelper.getResistance(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewRadioButtonInfo(mRgResistance, array[i], i);
        }
        // Wandering
        array = FrailHelper.getWandering(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewRadioButtonInfo(mRgWandering, array[i], i);
        }
        // Diseases
        array = FrailHelper.getDiseases(getContext());
        mSelected = new SparseBooleanArray(array.length);
        for (int i = 0; i < array.length; i++) {
            addNewCheckboxInfo(mLlDiseases, array[i], i);
        }
    }

    private void addNewRadioButtonInfo(ViewGroup container, String text, int position) {
        View infoView = mInflater.inflate(R.layout.input_radiobtn, container, false);

        RadioButtonFont radioButtonFont = infoView.findViewById(R.id.card_input_rdb);
        radioButtonFont.setId(ViewCompat.generateViewId());
        radioButtonFont.setText(text);
        radioButtonFont.setTag(position);

        container.addView(infoView);
    }

    private void addNewCheckboxInfo(ViewGroup container, String text, int position) {
        View infoView = mInflater.inflate(R.layout.input_checkbox,
                container, false);

        CheckBox checkBox = infoView.findViewById(R.id.card_input_cbx);
        checkBox.setText(text);
        checkBox.setTag(position);

        container.addView(infoView);
    }

    private boolean validateTest() {
        if (mRgFatigue.getCheckedRadioButtonId() == -1 &&
                mRgResistance.getCheckedRadioButtonId() == -1 && mRgWandering.getCheckedRadioButtonId() == -1) {
            showErrorMessage(getString(R.string.dialog_missing_answer_message));
            return false;
        }
        if (TextUtils.isEmpty(mEditWeightCurrent.getText().toString()) ||
                TextUtils.isEmpty(mEditWeight1yearAgo.getText().toString())) {
            showErrorMessage(getString(R.string.dialog_missing_answer_message));
            return false;
        }
        return true;
    }

    private void saveContent() {
        if (getContext() != null) {
            int fatigue = getAnswerPosition(mRgFatigue.getCheckedRadioButtonId());
            int resistance = getAnswerPosition(mRgResistance.getCheckedRadioButtonId());
            int wandering = getAnswerPosition(mRgWandering.getCheckedRadioButtonId());
            int weightCurrent = Integer.parseInt(mEditWeightCurrent.getText().toString());
            int weightYearAgo = Integer.parseInt(mEditWeight1yearAgo.getText().toString());

            DatabaseInserter.insertFrailScale(getContext(),
                    mSubjectId,
                    fatigue,
                    resistance,
                    wandering,
                    StringHelper.getJsonString(getItemsSelected()),
                    weightCurrent,
                    weightYearAgo,
                    EvaluationFrailHelper.getEvaluation(fatigue,
                            resistance,
                            wandering,
                            getItemsSelected().size(),
                            weightCurrent,
                            weightYearAgo),
                    this);
        }
    }

    private int getAnswerPosition(@IdRes int checkedRadioButtonId) {
        assert getView() != null;
        return Integer.parseInt(getView().findViewById(checkedRadioButtonId).getTag().toString());
    }

    private ArrayList<Integer> getItemsSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < mLlDiseases.getChildCount(); i++) {
            CheckBox child = (CheckBox) mLlDiseases.getChildAt(i);
            if (child.isChecked())
                result.add(Integer.parseInt(child.getTag().toString()));
        }

        if (result.isEmpty()) {
            result.add(FRAIL_DISEASES_ANSWER_WAS_NO);
        }

        return result;
    }

    private void showErrorMessage(String message) {
        FragmentManager fm = getChildFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_ask_before_leaving_title),
                message, 0);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    private void showHelp() {
        assert getActivity() != null;
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment
                .newInstance(getString(R.string.dialog_help_title), FrailHelper.getHelp(getActivity()));
        dialogFragment.show(fm, "fragment_dialog_test");
    }
}
