package software.cneuro.neuroger.ui.detail.evaluation;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationSubValHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectiveHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubjectiveEvaluationDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";

    static final String[] SUBJECTIVE_EVALUATION_PROJECTION = new String[]{
            Constant.COL_SE_TEST_HEALTH_STATE,
            Constant.COL_SE_TEST_MEMORY_CURRENT,
            Constant.COL_SE_TEST_MEMORY_PAST,
            Constant.COL_SE_TEST_DIFFICULTIES,
            Constant.COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT,
            Constant.COL_SE_TEST_COURSE_OF_DETERIORATION,
            Constant.COL_SE_TEST_EVOLUTION_TIME,
            Constant.COL_SE_TEST_EVALUATION
    };

    private long mSubjectId;
    private int mEvaluation;

    private TextView mCard3Answer1, mCard4Answer1, mCard5Answer1;
    private TextView mCard1Answer0, mCard1Answer1, mCard1Answer2;
    private ViewGroup mCard2AnswerContainer, mCard3AnswerContainer, mCard4AnswerContainer, mCard5AnswerContainer;
    private LayoutInflater mInflater;

    public SubjectiveEvaluationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjective_evaluation_detail, container, false);

        // card 0
        mCard1Answer0 = rootView.findViewById(R.id.card_1_answer_0);

        // card 1
        mCard1Answer1 = rootView.findViewById(R.id.card_1_answer_1);
        mCard1Answer2 = rootView.findViewById(R.id.card_1_answer_2);

        // card 2
        mCard2AnswerContainer = rootView.findViewById(R.id.card_2_answer_container);

        // card 3
        mCard3Answer1 = rootView.findViewById(R.id.card_3_answer_1);
        mCard3AnswerContainer = rootView.findViewById(R.id.card_3_container);

        // card 4
        mCard4Answer1 = rootView.findViewById(R.id.card_4_answer_1);
        mCard4AnswerContainer = rootView.findViewById(R.id.card_4_container);

        // card 5
        mCard5Answer1 = rootView.findViewById(R.id.card_5_answer_1);
        mCard5AnswerContainer = rootView.findViewById(R.id.card_5_container);

        return rootView;
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

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
    }

    @NonNull
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + Constant.COL_SE_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(getActivity(), baseUri,
                SUBJECTIVE_EVALUATION_PROJECTION, select, null, null);
    }

    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            int question_0 = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_SE_TEST_HEALTH_STATE
                    ));
            mCard1Answer0.setText(SubjectiveHelper.getAnswer0Text(getContext(), question_0));
            int question_1a = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_CURRENT
                    ));
            mCard1Answer1.setText(SubjectiveHelper.getAnswer1AText(getContext(), question_1a));
            int question_1b = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_PAST
                    ));
            mCard1Answer2.setText(SubjectiveHelper.getAnswer1BText(getContext(), question_1b));

            mCard2AnswerContainer.removeAllViews();
            String question2Json = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_SE_TEST_DIFFICULTIES));
            ArrayList<Integer> question2 = StringHelper.getArrayListFromJson(question2Json);
            for (Integer position : question2) {
                addNewInfo(mCard2AnswerContainer,
                        SubjectiveHelper.getAnswer2Text(getContext(),
                                position));
            }

            mEvaluation = cursor.getInt(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_EVALUATION));

            setEvaluation();
        }
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

    public void setEvaluation() {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationSubValHelper.getEvaluationText(getActivity(), mEvaluation));
        }
    }
}
