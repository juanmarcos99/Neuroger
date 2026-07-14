package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationPsychoaffectiveHelper;
import software.cneuro.neuroger.content.PsychoaffectiveHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

public class PsychoaffectiveDetailFragment extends BaseQuestionnaireShortAnswerDetailFragment {

    static final String[] PROJECTION = new String[]{
            Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST,
            Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION
    };

    public PsychoaffectiveDetailFragment() {
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Constant.URI_TABLE_PSYCHOAFFECTIVE_TEST;

        String select = "((" + Constant.COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

        assert getContext() != null;
        return new CursorLoader(
                getContext(),
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            mScore = cursor.getDouble(
                    cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION));

            String answerPosJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST));
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
                    EvaluationPsychoaffectiveHelper.getEvaluationText(getActivity(),
                            EvaluationPsychoaffectiveHelper.evaluate(mScore)));
        }
    }

    @Override
    public String getQuestionText(Context context, int questionPos) {
        assert getContext() != null;
        return PsychoaffectiveHelper.getQuestionText(getContext(), questionPos);
    }

    @Override
    public String getAnswerText(Context context, int answerPos) {
        assert getContext() != null;
        return PsychoaffectiveHelper.getAnswerText(getContext(), answerPos);
    }
}
