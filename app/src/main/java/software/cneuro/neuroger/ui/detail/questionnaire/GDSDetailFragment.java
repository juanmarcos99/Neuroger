package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationGDSHelper;
import software.cneuro.neuroger.content.GDSHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class GDSDetailFragment extends BaseQuestionnaireShortAnswerDetailFragment {

    public GDSDetailFragment() {
    }

    // GDS
    static final String[] GDS_PROJECTION = new String[]{
            Constant.COL_DEPRESSION_TEST_ANSWERS_LIST,
            Constant.COL_DEPRESSION_TEST_EVALUATION
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_DEPRESSION_TEST;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                GDS_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            mScore = cursor.getDouble(
                    cursor.getColumnIndex(Constant.COL_DEPRESSION_TEST_EVALUATION));

            String answerPosJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_DEPRESSION_TEST_ANSWERS_LIST));
            ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
            int questionPos = 0;
            for (Integer position : answerPosList) {
                addNewInfo(questionPos++, position);
            }

            setEvaluation();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void setEvaluation() {
        assert getView() != null;
        ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                getString(R.string.card_label_score),
                String.valueOf(mScore),
                getString(R.string.score_unit)
        ));

        ((TextView) getView().findViewById(R.id.card_result)).setText(
                EvaluationGDSHelper.getEvaluationText(getActivity(),
                        EvaluationGDSHelper.evaluate(mScore)));
    }

    @Override
    public String getQuestionText(Context context, int questionPos) {
        return GDSHelper.getQuestionText(context, questionPos);
    }

    @Override
    public String getAnswerText(Context context, int answerPos) {
        return GDSHelper.getAnswerText(context, answerPos);
    }
}
