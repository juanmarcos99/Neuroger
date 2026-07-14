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
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.WarningSignHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertAlertSigns_AsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class WarningSignsFragment extends Fragment implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, InsertAlertSigns_AsyncTask.OnAlertSignsInserted {
    public static final String ARG_SUBJECT_ID = "subject_id";
    private static final int WARNING_SAVE_DIALOG_ID = 34;

    private long mSubjectId; // carer id
    private SparseBooleanArray mSelected;

    private ViewGroup mCardView1;
    private LayoutInflater mInflater;

    private String[] mSigns;
    private SparseBooleanArray mTemp;

    public WarningSignsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_warning_signs_input, container, false);

        mCardView1 = (ViewGroup) rootView.findViewById(R.id.detail_card_container_1);

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

    private void updateContent() {
        //((TextView) getView().findViewById(R.id.card_title)).setText(getString(R.string.warning_sign_title));
        //getView().findViewById(R.id.card_title).setVisibility(View.GONE);

        assert getContext() != null;
        mSigns = WarningSignHelper.getWarningSigns(getContext());

        for (int i = 0; i < mSigns.length; i++) {
            addNewInfo(mCardView1, mSigns[i], i);
        }
    }

    private void addNewInfo(ViewGroup container, String text, int position) {
        View infoView = mInflater.inflate(R.layout.input_checkbox_white_icon,
                container, false);

        CheckBox checkBox = (CheckBox) infoView.findViewById(R.id.card_input_cbx);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setText(text);
        checkBox.setTag(position);

        ImageButton helBtn = (ImageButton) infoView.findViewById(R.id.card_help_icon);
        helBtn.setTag(position);
        helBtn.setOnClickListener(this);

        container.addView(infoView);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ((int) buttonView.getTag() == mSigns.length - 1 && isChecked) {
            mTemp = mSelected.clone();
            deselectCheckboxes();
            resetSelectionAddNoReport(Integer.parseInt(buttonView.getTag().toString()));
            return;
        }

        if (isChecked) {
            mSelected.put(Integer.parseInt(buttonView.getTag().toString()), true);

            /*ViewGroup container = (ViewGroup) mCardView1.getChildAt(mSigns.length - 1);
            ((CheckBox)container.getChildAt(1)).setChecked(false);*/

            setChecked(mSigns.length - 1, false);
        } else {
            mSelected.delete(Integer.parseInt(buttonView.getTag().toString()));
        }
    }

    private void setChecked(int position, boolean checked) {
        ViewGroup container = (ViewGroup) mCardView1.getChildAt(position);
        ((CheckBox) container.getChildAt(1)).setChecked(checked);
    }

    private void resetSelectionAddNoReport(int value) {
        mSelected = new SparseBooleanArray();
        mSelected.put(value, true);
    }

    private void deselectCheckboxes() {
        for (int i = 0; i < mTemp.size(); i++) {
            /*View view = mCardView1.getChildAt(mTemp.keyAt(i));
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(false);
            }*/
            setChecked(mTemp.keyAt(i), false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_action) {
            saveContent();
        } else {
            showHelp(Integer.parseInt(v.getTag().toString()));
        }
    }

    @Override
    public void onAlertSignsInserted() {
        assert getActivity() != null;

        Toast.makeText(getActivity(), getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    public void addDefaultValueAndSave() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            result.add(mSigns.length - 1);
        }

        save(result);
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == WARNING_SAVE_DIALOG_ID) {
            addDefaultValueAndSave();
        }
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    public void saveContent() {
        ArrayList<Integer> result = getItemsSelected();

        if (result.isEmpty()) {
            showErrorMessage();
            return;
        }

        save(result);
    }

    private void save(ArrayList<Integer> result) {
        // TODO: 26/09/2017 erase the result_ok when is fixed in the database library
        DatabaseInserter.insertAlertSigns(getActivity(),
                mSubjectId, StringHelper.getJsonString(result), Constant.RESULT_OK, WarningSignsFragment.this);
    }

    private boolean validateTest(ArrayList<Integer> result) {
        if (result.isEmpty()) {
            result.add(mSigns.length - 1);
            return true;
        }

        if (result.size() > 1 && result.contains(mSigns.length - 1)) {
            showErrorMessage();
            return false;
        }
        return true;
    }

    public ArrayList<Integer> getItemsSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < mSelected.size(); i++) {
            result.add(mSelected.keyAt(i));
        }

        return result;
    }

    public void showHelp(int help) {
        assert getActivity() != null;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                WarningSignHelper.getWarningHelpText(getActivity(), help));
        dialogFragment.show(fm, "fragment_dialog");
    }

    private void showErrorMessage() {
        /*FragmentManager fm = getSupportFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.dialog_antecedents_error_message));
        dialogFragment.show(fm, "fragment_dialog");*/

        assert getActivity() != null;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.dialog_antecedents_error_message), WARNING_SAVE_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    public void showHelp() {
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.warning_sign_help));
        dialogFragment.show(fm, "fragment_dialog");
    }
}
