package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 27/10/2016.
 */
public abstract class BaseQuestionnaireShortAnswerDetailFragment extends BaseQuestionnaireDetailFragment {

    public BaseQuestionnaireShortAnswerDetailFragment() {
    }

    @Override
    public void addNewInfo(int questionPos, int answerPos) {
        if (getContext() != null) {
            View infoView = mInflater.inflate(R.layout.list_card_two_items,
                    mContainer, false);

            String questionText = getQuestionText(getContext(), questionPos);
            String answerText = getAnswerText(getContext(), answerPos);

            ((TextView) infoView.findViewById(R.id.card_title)).setText(questionText);
            ((TextView) infoView.findViewById(R.id.card_text)).setText(answerText);

            mContainer.addView(infoView);
        }
    }

    protected abstract String getQuestionText(Context context, int questionPos);

    protected abstract String getAnswerText(Context context, int answerPos);
}
