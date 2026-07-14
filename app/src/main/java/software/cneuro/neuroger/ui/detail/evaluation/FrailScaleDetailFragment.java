package software.cneuro.neuroger.ui.detail.evaluation;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import software.cneuro.neuroger.content.EvaluationFrailHelper;
import software.cneuro.neuroger.content.FrailHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

public class FrailScaleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";

    static final String[] PROJECTION = new String[]{
            Constant.COL_FRAIL_TEST_FATIGUE,
            Constant.COL_FRAIL_TEST_RESISTANCE,
            Constant.COL_FRAIL_TEST_WANDERING,
            Constant.COL_FRAIL_TEST_DISEASES,
            Constant.COL_FRAIL_TEST_WEIGHT_CURRENT,
            Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO,
            Constant.COL_FRAIL_TEST_EVALUATION
    };

    protected double mScore;
    private long mSubjectId;
    private LayoutInflater mInflater;
    private TextView mTextFatigue, mTextResistance, mTextWandering;
    private ViewGroup mLlDiseases;
    private TextView mTextWeightCurrent, mTextWeightYearAgo;

    public FrailScaleDetailFragment() {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frail_detail, container, false);

        mTextFatigue = rootView.findViewById(R.id.card_answer_fatigue);
        mTextResistance = rootView.findViewById(R.id.card_answer_resistance);
        mTextWandering = rootView.findViewById(R.id.card_answer_wandering);

        mLlDiseases = rootView.findViewById(R.id.card_answer_diseases_container);

        mTextWeightCurrent = rootView.findViewById(R.id.card_answer_weight_current);
        mTextWeightYearAgo = rootView.findViewById(R.id.card_answer_weight_year_ago);

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
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Constant.URI_TABLE_FRAIL_TEST;

        String select = "((" + Constant.COL_FRAIL_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(getActivity(), baseUri,
                PROJECTION, select, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            int fatigue = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_FRAIL_TEST_FATIGUE
                    ));
            mTextFatigue.setText(FrailHelper.getFatigueText(getContext(), fatigue));

            int resistance = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_FRAIL_TEST_RESISTANCE
                    ));
            mTextResistance.setText(FrailHelper.getResistanceText(getContext(), resistance));

            int wandering = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WANDERING
                    ));
            mTextWandering.setText(FrailHelper.getWanderingText(getContext(), wandering));

            // Diseases
            mLlDiseases.removeAllViews();
            String diseasesJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_FRAIL_TEST_DISEASES));
            ArrayList<Integer> diseases = StringHelper.getArrayListFromJson(diseasesJson);
            for (Integer position : diseases) {
                if (position == software.cneuro.neuroger.constant.Constant.FRAIL_DISEASES_ANSWER_WAS_NO) {
                    addNewInfo(mLlDiseases, getString(R.string.no_diseases));
                } else {
                    addNewInfo(mLlDiseases, FrailHelper.getDiseasesText(getContext(), position));
                }
            }

            int weightCurrent = cursor.getInt(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_CURRENT));
            mTextWeightCurrent.setText(StringHelper.appendWithDots(getString(R.string.edittext_weight_loss_current_hint), String.valueOf(weightCurrent)));
            int weightYearAgo = cursor.getInt(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO));
            mTextWeightYearAgo.setText(
                    StringHelper.appendWithDots(getString(R.string.edittext_weight_loss_1_year_ago_hint),
                            String.valueOf(weightYearAgo)));

            mScore = cursor.getDouble(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_EVALUATION));
        }

        // Diseases
//        if (cursor != null && getContext() != null) {
//            mLlDiseases.removeAllViews();
//
//            int answerPos = cursor.getInt(
//                    cursor.getColumnIndex(Constant.COL_FRAIL_TEST_DISEASES));
//            if (answerPos == software.cneuro.neuroger.constant.Constant.FRAIL_DISEASES_ANSWER_WAS_NO) {
//                addNewInfo(mLlDiseases, getString(R.string.no_diseases));
//            } else {
//                addNewInfo(mLlDiseases, FrailHelper.getDiseasesText(getContext(), answerPos));
//                while (cursor.moveToNext()) {
//                    answerPos = cursor.getInt(
//                            cursor.getColumnIndex(Constant.COL_FRAIL_TEST_DISEASES));
//                    addNewInfo(mLlDiseases,
//                            FrailHelper.getDiseasesText(getContext(),
//                                    answerPos));
//                }
//            }
//        }

        setEvaluation();
    }

    @Override
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
            ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                    getString(R.string.card_label_score),
                    String.valueOf(mScore),
                    getString(R.string.score_unit)
            ));

            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationFrailHelper.getEvaluationText(getActivity(),
                            EvaluationFrailHelper.evaluate(mScore)));
        }
    }
}
