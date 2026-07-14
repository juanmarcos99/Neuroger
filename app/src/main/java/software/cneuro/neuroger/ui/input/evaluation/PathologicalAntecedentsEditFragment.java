package software.cneuro.neuroger.ui.input.evaluation;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
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
import software.cneuro.neuroger.content.EvaluationClassificationHelper;
import software.cneuro.neuroger.content.EvaluationPAntecedentsHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neurogerdatabase.constant.Constant;
import software.cneuro.neurogerdatabase.database.DatabaseUpdater;
import software.cneuro.neurogerdatabase.database_async.UpdateClassificationByAntecedents_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.UpdatePatientCompensation_Async;

/**
 * Created by klaudia on 12/2/2015.
 */
public class PathologicalAntecedentsEditFragment extends Fragment implements
        View.OnClickListener,
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks,
        CompoundButton.OnCheckedChangeListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        UpdatePathologicalAnt_AsyncTask.OnPathologicalAntUpdated,
        UpdatePatientCompensation_Async.OnPatientUpdated,
        UpdateClassificationByAntecedents_AsyncTask.OnClassificationUpdated {
    public static final String ARG_SUBJECT_ID = "subject_id";
    private static final int WARNING_NO_REPORT_DIALOG_ID = 34;
    private static final int WARNING_COMPENSATED_DIALOG_ID = 44;

    // These are the Contacts rows that we will retrieve.
    static final String[] PATHOLOGICAL_ANT_PROJECTION = new String[]{
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_ID,
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID,
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY,
    };
    // Classification
    static final String[] CLASSIFICATION_PROJECTION = new String[]{
            Constant.COL_CLASSIFICATION_ID,
            Constant.COL_CLASSIFICATION_PATIENT_ID,
            Constant.COL_CLASSIFICATION_SUBJECTIVE_EVALUATION,
            Constant.COL_CLASSIFICATION_KATZ_SCORE,
            Constant.COL_CLASSIFICATION_FRAIL_SCORE,
            Constant.COL_CLASSIFICATION_LAWTON_SCORE,
            Constant.COL_CLASSIFICATION_PSYCHOFAMILY_SCORE,
            Constant.COL_CLASSIFICATION_PSYCHOAFFECTIVE_SCORE
    };
    private static final int ANTECEDENTS_LOADER_ID = 14;
    // update classification in case already exists for this user
    private static final int CLASSIFICATION_LOADER_ID = 15;

    private long antecedentsId, classificationId;
    private int subjectiveEvaluation,
            katzEvaluation,
            frailEvaluation,
            lawtonEvaluation,
            pasEvaluation,
            pAffectiveEvaluation;

    private long mSubjectId;
    private SparseBooleanArray mSelected;

    private ViewGroup mComorbidities;
    private CheckBox mCbxMedicationQuantity;
    private LayoutInflater mInflater;

    private String[] mPathologicalAnt;
    private SparseBooleanArray mTemp;
    private boolean isClassificationSet;

    public PathologicalAntecedentsEditFragment() {
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

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pathological_antecedents_input, container, false);

        mComorbidities = rootView.findViewById(R.id.detail_card_container_1);
        mCbxMedicationQuantity = rootView.findViewById(R.id.cbx_medication_quantity);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_edit, menu);
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

        getLoaderManager().initLoader(ANTECEDENTS_LOADER_ID, null, this);
        // CLASSIFICATION
        getLoaderManager().initLoader(CLASSIFICATION_LOADER_ID, null, this);
    }

    @NonNull
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        assert getContext() != null;
        if (id == ANTECEDENTS_LOADER_ID) {
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Uri baseUri = Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "((" + Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(getContext(), baseUri,
                    PATHOLOGICAL_ANT_PROJECTION, select, null, null);
        } else {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CLASSIFICATION;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    getContext(),
                    baseUri,
                    CLASSIFICATION_PROJECTION,
                    select,
                    null,
                    null);
        }
    }

    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == ANTECEDENTS_LOADER_ID) {
            if (cursor != null && cursor.moveToFirst()) {
                antecedentsId = cursor.getLong(
                        cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_ID));

                SparseBooleanArray antecedentsPos = new SparseBooleanArray(cursor.getCount());
                // comorbidities
                String comorbiditiesJson = cursor.getString(
                        cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID));
                ArrayList<Integer> comorbidities = StringHelper.getArrayListFromJson(comorbiditiesJson);
                for (Integer position : comorbidities) {
                    antecedentsPos.put(position, true);
                }

                // show all antecedents and check the ones that are saved in the bd
                assert getContext() != null;
                mPathologicalAnt = SubjectHelper.getPathologicalAntecedents(getContext());
                for (int i = 0; i < mPathologicalAnt.length; i++) {
                    addNewInfo(mComorbidities, mPathologicalAnt[i], i, antecedentsPos.get(i));
                }

                int medicationQuantity = cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY));
                mCbxMedicationQuantity.setChecked(medicationQuantity ==
                        software.cneuro.neuroger.constant.Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID);
            }
        } else if (cursor != null && cursor.moveToFirst()) {
            isClassificationSet = true;

            classificationId = cursor.getLong(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_ID));
            subjectiveEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_SUBJECTIVE_EVALUATION));
            katzEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_KATZ_SCORE));
            frailEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_FRAIL_SCORE));
            lawtonEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_LAWTON_SCORE));
            pasEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_PSYCHOFAMILY_SCORE));
            pAffectiveEvaluation = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CLASSIFICATION_PSYCHOAFFECTIVE_SCORE));
        }
    }

    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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

            ((CheckBox) mComorbidities.getChildAt(mPathologicalAnt.length - 1)).setChecked(false);
        } else {
            mSelected.delete(Integer.parseInt(buttonView.getTag().toString()));
        }
    }

    @Override
    public void onClick(View v) {
        saveContent();
    }

    private void addNewInfo(ViewGroup container, String text, int position, boolean checked) {
        View infoView = mInflater.inflate(R.layout.input_checkbox,
                container, false);

        if (checked) {
            mSelected.put(position, true);
        }

        CheckBox checkBox = infoView.findViewById(R.id.card_input_cbx);
        checkBox.setChecked(checked);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setText(text);
        checkBox.setTag(position);

        container.addView(infoView);
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == WARNING_NO_REPORT_DIALOG_ID) {
            addDefaultValueAndSave();
        } /*else if (dialogId == WARNING_COMPENSATED_DIALOG_ID) {
            setCompensated(true);
        }*/
    }

    @Override
    public void onNegativeAnswer(int dialogId) {
        /*if (dialogId == WARNING_COMPENSATED_DIALOG_ID) {
            setCompensated(false);
        }*/
    }

    @Override
    public void onPathologicalAntUpdated() {
        assert getActivity() != null;
        Toast.makeText(getActivity(), getString(R.string.toast_saved_pathological_antecedents), Toast.LENGTH_LONG).show();

        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onClassificationUpdated() {

    }

    @Override
    public void OnPatientUpdated(long _id) {

    }

    private void resetSelectionAddNoReport(int value) {
        mSelected = new SparseBooleanArray();
        mSelected.put(value, true);
    }

    private void deselectCheckboxes() {
        for (int i = 0; i < mTemp.size(); i++) {
            View view = mComorbidities.getChildAt(mTemp.keyAt(i));
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(false);
            }
        }
    }

    private void saveContent() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            showSaveWithNoReportMessage();
        } else {
//            checkIfNoCompensated();
            save(result, software.cneuro.neuroger.constant.Constant.SUBJECT_COMPENSATED_ID);
        }
    }

    private void checkIfNoCompensated() {
        ArrayList<Integer> result = getItemsSelected();
        if (result.isEmpty() || result.contains(mPathologicalAnt.length - 1)) {
            save(result, software.cneuro.neuroger.constant.Constant.SUBJECT_COMPENSATED_ID);
        }

        String message = getString(R.string.dialog_antecedents_no_compensated_option);
        showCompensatedMessage(
                getString(R.string.dialog_antecedents_compensated_title),
                message);
    }

    private void addDefaultValueAndSave() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            result.add(mPathologicalAnt.length - 1);
        }

        save(result, software.cneuro.neuroger.constant.Constant.SUBJECT_COMPENSATED_ID);
    }

    private ArrayList<Integer> getItemsSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < mSelected.size(); i++) {
            result.add(mSelected.keyAt(i));
        }
        return result;
    }

    private void setCompensated(boolean compensated) {
        save(getItemsSelected(), compensated ?
                software.cneuro.neuroger.constant.Constant.SUBJECT_COMPENSATED_ID :
                software.cneuro.neuroger.constant.Constant.SUBJECT_NO_COMPENSATED_ID);
    }

    private void save(ArrayList<Integer> result, int compensated) {
//       DatabaseUpdater.updatePatientCompensated(getActivity(), mSubjectId, compensated, this);

        assert getContext() != null;
        int medicationQuantity = mCbxMedicationQuantity.isChecked() ?
                software.cneuro.neuroger.constant.Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID :
                software.cneuro.neuroger.constant.Constant.SUBJECT_NO_TAKES_MORE_THAN_5_MEDICAMENT_ID;
        double evaluation = EvaluationPAntecedentsHelper.evaluate(getContext(), result);

        DatabaseUpdater.updatePathologicalAnt(getContext(),
                antecedentsId,
                mSubjectId,
                StringHelper.getJsonString(result),
                medicationQuantity,
                evaluation,
                this);
        // if it does not exist it is because there are still tests to be done
        // this update is needed only if after the classification the antecedents are changed
        if (isClassificationSet)
            DatabaseUpdater.updateClassificationByAntecedents(getContext(),
                    classificationId,
                    mSubjectId,
                    evaluation,
                    medicationQuantity,
                    EvaluationClassificationHelper.evaluate(
                            evaluation,
                            medicationQuantity,
                            subjectiveEvaluation,
                            katzEvaluation,
                            frailEvaluation,
                            lawtonEvaluation,
                            pasEvaluation,
                            pAffectiveEvaluation
                    ),
                    PathologicalAntecedentsEditFragment.this);
    }

    public void showHelp() {
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.antecedents_title));
        dialogFragment.show(fm, "fragment_dialog");
    }

    public void showCompensatedMessage(String title, String message) {
        assert getActivity() != null;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                title,
                message, WARNING_COMPENSATED_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    public void showSaveWithNoReportMessage() {
        assert getActivity() != null;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.dialog_antecedents_error_message), WARNING_NO_REPORT_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }
}
