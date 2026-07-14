package software.cneuro.neuroger.ui.detail.evaluation;

import static software.cneuro.neuroger.constant.Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID;

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

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationPAntecedentsHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.input.evaluation.PathologicalAntecedentsEditActivity;
import software.cneuro.neuroger.ui.input.evaluation.PathologicalAntecedentsEditFragment;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class PathologicalAntecedentsDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";

    // These are the Contacts rows that we will retrieve.
    static final String[] PATHOLOGICAL_ANT_PROJECTION = new String[]{
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID,
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY,
            Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION,
    };

    private long mSubjectId;
    private String mName;

    private ViewGroup mComorbidities;
    private TextView mTextMedicationQuantity;
    private LayoutInflater mInflater;
    private double mScore;

    public PathologicalAntecedentsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
                mName = getArguments().getString(PathologicalAntecedentsEditActivity.ARG_SUBJECT_NAME);
            }
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pathological_antecedents_detail, container, false);

        mComorbidities = rootView.findViewById(R.id.detail_card_container_1);
        mTextMedicationQuantity = rootView.findViewById(R.id.card_answer_medicament_quantity);

        return rootView;
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
            Intent intent = new Intent(getActivity(), PathologicalAntecedentsEditActivity.class);
            intent.putExtra(PathologicalAntecedentsEditActivity.ARG_SUBJECT_NAME, mName);
            intent.putExtra(PathologicalAntecedentsEditFragment.ARG_SUBJECT_ID, mSubjectId);
            startActivity(intent);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        // clean all the data to prepared for the new load
        cleanData();

        getLoaderManager().restartLoader(0, null, this);
    }

    private void cleanData() {
        mComorbidities.removeAllViews();
    }

    @NonNull
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(getActivity(), baseUri,
                PATHOLOGICAL_ANT_PROJECTION, select, null, null);
    }

    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            // comorbidities
            String comorbiditiesJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID));
            ArrayList<Integer> comorbidities = StringHelper.getArrayListFromJson(comorbiditiesJson);
            for (Integer position : comorbidities) {
                addNewInfo(mComorbidities,
                        SubjectHelper.getPathologicalAntecedentsText(getActivity(), position));
            }

            int medicationQuantity = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY
                    ));
            mTextMedicationQuantity.setText(medicationQuantity ==
                    SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID ? R.string.medicaments_result_negative : R.string.medicaments_result_ok);

            mScore = EvaluationPAntecedentsHelper.evaluate(getContext(), comorbidities);
        }
        setEvaluation();
    }

    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void addNewInfo(ViewGroup container, String text) {
        View infoView = mInflater.inflate(R.layout.detail_text,
                container, false);

        TextView textView = infoView.findViewById(R.id.card_detail_text);
        textView.setText(text);

        container.addView(infoView);
    }

    private void setEvaluation() {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                    getString(R.string.card_label_score),
                    String.valueOf(mScore),
                    getString(R.string.score_unit)
            ));

            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationPAntecedentsHelper.getEvaluationText(getActivity(), mScore));
        }
    }
}
