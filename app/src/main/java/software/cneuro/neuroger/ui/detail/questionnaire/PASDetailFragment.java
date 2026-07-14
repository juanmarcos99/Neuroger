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
import software.cneuro.neuroger.content.EvaluationPASHelper;
import software.cneuro.neuroger.content.PASHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

public class PASDetailFragment extends BaseQuestionnaireLongAnswerDetailFragment {

    static final String[] PROJECTION = new String[]{
            Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST,
            Constant.COL_PSYCHOFAMILY_TEST_EVALUATION
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constant.URI_TABLE_PSYCHOFAMILY_TEST;

        String select = "((" + Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID + " = " + mSubjectId + "))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            mScore = cursor.getDouble(
                    cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_EVALUATION));

            String answerPosJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST));
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
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                    getString(R.string.card_label_score),
                    String.valueOf(mScore),
                    getString(R.string.score_unit)
            ));

            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationPASHelper.getEvaluationText(getActivity(),
                            EvaluationPASHelper.evaluate(mScore)));
        }
    }

    @Override
    public String getTestTypesText(Context context, int questionPos) {
        assert getContext() != null;
        return PASHelper.getTestTypesText(getContext(), questionPos);
    }

    @Override
    public String getItemsList(Context context, int questionPos, int answerPos) {
        assert getContext() != null;
        return PASHelper.getItemsList(getContext(), questionPos, answerPos);
    }
}
