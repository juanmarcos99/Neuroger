package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CDRHelper;
import software.cneuro.neuroger.content.EvaluationCDRHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class CDRDetailFragment extends BaseQuestionnaireLongAnswerDetailFragment {

    static final String[] PROJECTION = new String[]{
            Constant.COL_CDR_TEST_PATIENT_ID,
            Constant.COL_CDR_TEST_MEMORY,
            Constant.COL_CDR_TEST_ORIENTATION,
            Constant.COL_CDR_TEST_JUDGEMENT,
            Constant.COL_CDR_TEST_COMMUNITY,
            Constant.COL_CDR_TEST_HOBBIES,
            Constant.COL_CDR_TEST_PERSONAL_CARE,
            Constant.COL_CDR_TEST_EVALUATION
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CDR_TEST;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_PATIENT_ID + " = " + mSubjectId + "))";

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
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            int answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_MEMORY
                    ));
            addNewInfo(0, answerPos);

            answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_ORIENTATION
                    ));
            addNewInfo(1, answerPos);

            answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_JUDGEMENT
                    ));
            addNewInfo(2, answerPos);

            answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_COMMUNITY
                    ));
            addNewInfo(3, answerPos);

            answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_HOBBIES
                    ));
            addNewInfo(4, answerPos);

            answerPos = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_PERSONAL_CARE
                    ));
            addNewInfo(5, answerPos);

            mScore = cursor.getDouble(
                    cursor.getColumnIndex(Constant.COL_CDR_TEST_EVALUATION
                    ));

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
                    EvaluationCDRHelper.getEvaluationText(getActivity(), mScore));
        }
    }

    @Override
    public String getTestTypesText(Context context, int questionPos) {
        assert getContext() != null;
        return CDRHelper.getTestTypesText(getContext(), questionPos);
    }

    @Override
    public String getItemsList(Context context, int questionPos, int answerPos) {
        assert getContext() != null;
        return CDRHelper.getItemsList(getContext(), questionPos, answerPos);
    }
}
