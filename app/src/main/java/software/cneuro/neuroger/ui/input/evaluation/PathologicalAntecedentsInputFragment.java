package software.cneuro.neuroger.ui.input.evaluation;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationPAntecedentsHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertPathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdateClassificationByAntecedents_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePatientCompensation_Async;

/**
 * A placeholder fragment containing a simple view.
 */
public class PathologicalAntecedentsInputFragment extends Fragment implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        InsertPathologicalAnt_AsyncTask.OnPathologicalAntInserted,
        UpdatePatientCompensation_Async.OnPatientUpdated,
        UpdateClassificationByAntecedents_AsyncTask.OnClassificationUpdated {
    public static final String ARG_SUBJECT_ID = "subject_id";
    private static final int WARNING_NO_REPORT_DIALOG_ID = 34;
    private static final int WARNING_COMPENSATED_DIALOG_ID = 44;

    private int subjectiveEvaluation;
    private int mmseEvaluation;
    private int katzEvaluation;
    private int frailEvaluation;
    private int lawtonEvaluation;

    private long mSubjectId;
    private SparseBooleanArray mSelected;

    private ViewGroup mCardView1;
    private LayoutInflater mInflater;

    private String[] mPathologicalAnt;
    private SparseBooleanArray mTemp;

    public PathologicalAntecedentsInputFragment() {
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

        mSelected = new SparseBooleanArray();

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pathological_antecedents_input, container, false);

        mCardView1 = rootView.findViewById(R.id.detail_card_container_1);

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
        } else
            return super.onOptionsItemSelected(item);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ((int) buttonView.getTag() == mPathologicalAnt.length - 1 && isChecked) {
            mTemp = mSelected.clone();
            deselectCheckboxes();
            resetSelectionAddNoReport(Integer.parseInt(buttonView.getTag().toString()));
            return;
        }

        if (isChecked) {
            mSelected.put(Integer.parseInt(buttonView.getTag().toString()), true);

            ((CheckBox) mCardView1.getChildAt(mPathologicalAnt.length - 1)).setChecked(false);
        } else {
            mSelected.delete(Integer.parseInt(buttonView.getTag().toString()));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_action) {
            saveContent();
        }
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == WARNING_NO_REPORT_DIALOG_ID) {
            addDefaultValueAndSave();
        } else if (dialogId == WARNING_COMPENSATED_DIALOG_ID) {
            setCompensated(true);
        }
    }

    @Override
    public void onNegativeAnswer(int dialogId) {
        if (dialogId == WARNING_COMPENSATED_DIALOG_ID) {
            setCompensated(false);
        }
    }

    @Override
    public void onPathologicalAntInserted() {
        assert getActivity() != null;

        Toast.makeText(getActivity(), getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void OnPatientUpdated(long _id) {

    }

    @Override
    public void onClassificationUpdated() {

    }

    private void updateContent() {
        assert getContext() != null;
        mPathologicalAnt = SubjectHelper.getPathologicalAntecedents(getContext());
        for (int i = 0; i < mPathologicalAnt.length; i++) {
            addNewInfo(mCardView1, mPathologicalAnt[i], i);
        }
    }

    private void addNewInfo(ViewGroup container, String text, int position) {
        View infoView = mInflater.inflate(R.layout.input_checkbox,
                container, false);

        CheckBox checkBox = infoView.findViewById(R.id.card_input_cbx);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setText(text);
        checkBox.setTag(position);

        container.addView(infoView);
    }

    private void resetSelectionAddNoReport(int value) {
        mSelected = new SparseBooleanArray();
        mSelected.put(value, true);
    }

    private void deselectCheckboxes() {
        for (int i = 0; i < mTemp.size(); i++) {
            View view = mCardView1.getChildAt(mTemp.keyAt(i));
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(false);
            }
        }
    }

    private void showHelp() {
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.antecedents_title));
        dialogFragment.show(fm, "fragment_dialog");
    }

    private void saveContent() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            showSaveWithNoReportMessage();
        } else {
//            checkIfNoCompensated();
            save(result, Constant.SUBJECT_COMPENSATED_ID);
        }
    }

    private void addDefaultValueAndSave() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            result.add(mPathologicalAnt.length - 1);
        }

        save(result, Constant.SUBJECT_COMPENSATED_ID);
    }

    private void checkIfNoCompensated() {
        ArrayList<Integer> result = getItemsSelected();
        if (result.isEmpty() || result.contains(mPathologicalAnt.length - 1)) {
            save(result, Constant.SUBJECT_COMPENSATED_ID);
        }

        String message = getString(R.string.dialog_antecedents_no_compensated_option);
        showCompensatedMessage(getString(R.string.dialog_antecedents_compensated_title), message);
    }

    private void save(ArrayList<Integer> result, int compensated) {
        assert getView() != null;
        int medicationsQuantity = ((CheckBox) getView().findViewById(R.id.cbx_medication_quantity)).isChecked() ?
                Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID : Constant.SUBJECT_NO_TAKES_MORE_THAN_5_MEDICAMENT_ID;

        assert getContext() != null;
        DatabaseInserter.insertPathologicalAnt(getActivity(),
                mSubjectId,
                StringHelper.getJsonString(result),
                medicationsQuantity,
                EvaluationPAntecedentsHelper.evaluate(getContext(), result),
                this);
        // DatabaseUpdater.updatePatientCompensated(getActivity(), mSubjectId, compensated, this);
    }

    private boolean validateTest(ArrayList<Integer> result) {
        if (result.isEmpty()) {
            result.add(mPathologicalAnt.length - 1);
            return true;
        }

        if (result.size() > 1 && result.contains(mPathologicalAnt.length - 1)) {
            showSaveWithNoReportMessage();
            return false;
        }
        return true;
    }

    private ArrayList<Integer> getItemsSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < mSelected.size(); i++) {
            result.add(mSelected.keyAt(i));
        }

        return result;
    }

    public void setCompensated(boolean compensated) {
        save(getItemsSelected(), compensated ? Constant.SUBJECT_COMPENSATED_ID : Constant.SUBJECT_NO_COMPENSATED_ID);
    }

    private void showCompensatedMessage(String title, String message) {
        FragmentManager fm = getChildFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                title,
                message, WARNING_COMPENSATED_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    private void showSaveWithNoReportMessage() {
        FragmentManager fm = getChildFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.dialog_antecedents_error_message), WARNING_NO_REPORT_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }
}
