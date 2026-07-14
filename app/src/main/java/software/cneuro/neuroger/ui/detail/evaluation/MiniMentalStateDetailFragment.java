package software.cneuro.neuroger.ui.detail.evaluation;

import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_EVALUATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_DESIGN;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_LANGUAGE;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_ORDER_3;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_REMEMBERING;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION;

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
import software.cneuro.neuroger.content.EvaluationMMHelper;
import software.cneuro.neuroger.content.MMSEHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class MiniMentalStateDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_SUBJECT_AGE = "age";
    public static final String ARG_SUBJECT_LEVEL_OF_SCHOOLING = "level_of_schooling";

    public static final int MINIMENTAL_LOADER_ID = 10;

    // These are the Contacts rows that we will retrieve.
    static final String[] PROJECTION = new String[]{
            COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION,
            COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION,
            COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION,
            COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG,
            COL_MINIMENTAL_TEST_MATH_ATTENTION,
            COL_MINIMENTAL_TEST_REMEMBERING,
            COL_MINIMENTAL_TEST_LANGUAGE,
            COL_MINIMENTAL_TEST_ORDER_3,
            COL_MINIMENTAL_TEST_DESIGN,
            COL_MINIMENTAL_EVALUATION
    };

    private long mSubjectId;
    private int mAge;
    private int mLevelOfSchooling;

    private ViewGroup mCard1View1, mCard1View2;
    private ViewGroup mCard2View;
    private ViewGroup mCard3View;
    private ViewGroup mCard4View;
    private ViewGroup mCard5View1, mCard5View2;
    private ViewGroup mCard6View;
    private LayoutInflater mInflater;

    private double mScore;

    public MiniMentalStateDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
            if (getArguments().containsKey(ARG_SUBJECT_AGE)) {
                mAge = getArguments().getInt(ARG_SUBJECT_AGE);
            }
            if (getArguments().containsKey(ARG_SUBJECT_LEVEL_OF_SCHOOLING)) {
                mLevelOfSchooling = getArguments().getInt(ARG_SUBJECT_LEVEL_OF_SCHOOLING);
            }
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mini_mental_state_detail, container, false);

        // Orientation
        mCard1View1 = rootView.findViewById(R.id.detail_card_1_container_1);
        mCard1View2 = rootView.findViewById(R.id.detail_card_1_container_2);

        // Retention
        mCard2View = rootView.findViewById(R.id.detail_card_2_container);

        // Math
        mCard3View = rootView.findViewById(R.id.detail_card_3_container);

        // Reminder
        mCard4View = rootView.findViewById(R.id.detail_card_4_container);

        // Language
        mCard5View1 = rootView.findViewById(R.id.detail_card_5_container_1);

        mCard5View2 = rootView.findViewById(R.id.detail_card_5_container_2);

        // Design
        mCard6View = rootView.findViewById(R.id.detail_card_6_container);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
        outState.putInt(ARG_SUBJECT_AGE, mAge);
        outState.putInt(ARG_SUBJECT_LEVEL_OF_SCHOOLING, mLevelOfSchooling);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
            mAge = savedInstanceState.getInt(ARG_SUBJECT_AGE);
            mLevelOfSchooling = savedInstanceState.getInt(ARG_SUBJECT_LEVEL_OF_SCHOOLING);
        }

        assert getActivity() != null;
        getActivity().getSupportLoaderManager().initLoader(MINIMENTAL_LOADER_ID, null, this);
    }

    @NonNull
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constant.URI_TABLE_MINIMENTAL_TEST;

        String select = "((" + Constant.COL_MINIMENTAL_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getContext() != null;
        return new CursorLoader(
                getContext(),
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            mScore = cursor.getDouble(
                    cursor.getColumnIndex(Constant.COL_MINIMENTAL_EVALUATION));

            mCard1View1.removeAllViews();
            String temporalOrientationJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION));
            ArrayList<Integer> temporalOrientation = StringHelper.getArrayListFromJson(temporalOrientationJson);
            for (Integer position : temporalOrientation) {
                addNewInfo(mCard1View1, MMSEHelper.getOrientationTemporalText(getContext(), position));
            }

            mCard1View2.removeAllViews();
            String spatialOrientationJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION));
            ArrayList<Integer> spatialOrientation = StringHelper.getArrayListFromJson(spatialOrientationJson);
            for (Integer position : spatialOrientation) {
                addNewInfo(mCard1View2, MMSEHelper.getOrientationSpatialText(getContext(), position));
            }

            mCard2View.removeAllViews();
            String retentionRegisterInformationJson = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION));
            ArrayList<Integer> retentionRegisterInformation = StringHelper.getArrayListFromJson(retentionRegisterInformationJson);
            for (Integer position : retentionRegisterInformation) {
                addNewInfo(mCard2View, MMSEHelper.getRetentionInfoText(getContext(), position));
            }

            mCard3View.removeAllViews();
            int mathAttentionFlag = cursor.getInt(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG));
            String mathAttentionJson = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION));
            if (mathAttentionFlag == software.cneuro.neuroger.constant.Constant.MATH_COUNTING_TEST) {
                ArrayList<Integer> mathAttention = StringHelper.getArrayListFromJson(mathAttentionJson);
                for (Integer values : mathAttention) {
                    addNewInfo(mCard3View, values.toString());
                }
            } else {
                addNewInfo(mCard3View, mathAttentionJson);
            }

            mCard4View.removeAllViews();
            String rememberingJson = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_REMEMBERING));
            ArrayList<Integer> remembering = StringHelper.getArrayListFromJson(rememberingJson);
            for (Integer position : remembering) {
                addNewInfo(mCard4View, MMSEHelper.getReminderText(getContext(), position));
            }

            mCard5View1.removeAllViews();
            String languageJson = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_LANGUAGE));
            ArrayList<Integer> language = StringHelper.getArrayListFromJson(languageJson);
            for (Integer position : language) {
                addNewInfo(mCard5View1, MMSEHelper.getLanguageText(getContext(), position));
            }

            mCard5View2.removeAllViews();
            String order3Json = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_ORDER_3));
            ArrayList<Integer> order3 = StringHelper.getArrayListFromJson(order3Json);
            for (Integer position : order3) {
                addNewInfo(mCard5View2, MMSEHelper.getLanguageOrderIn3Text(getContext(), position));
            }

            mCard6View.removeAllViews();
            String designJson = cursor.getString(
                    cursor.getColumnIndex(COL_MINIMENTAL_TEST_DESIGN));
            ArrayList<Integer> design = StringHelper.getArrayListFromJson(designJson);
            for (Integer position : design) {
                addNewInfo(mCard6View, MMSEHelper.getDesignText(getContext(), position));
            }

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
            ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                    getString(R.string.card_label_score),
                    String.valueOf(mScore),
                    getString(R.string.score_unit)
            ));

            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationMMHelper.getEvaluationText(getActivity(),
                            EvaluationMMHelper.evaluate(mScore, mAge, mLevelOfSchooling)));
        }
    }
}
