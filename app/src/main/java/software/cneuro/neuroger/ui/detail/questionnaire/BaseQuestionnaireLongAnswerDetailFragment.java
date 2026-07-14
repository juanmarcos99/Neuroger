package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import software.cneuro.neuroger.R;

public abstract class BaseQuestionnaireLongAnswerDetailFragment extends BaseQuestionnaireDetailFragment {

    public BaseQuestionnaireLongAnswerDetailFragment() {
    }

    @Override
    protected void addNewInfo(int questionPos, int answerPos) {
        if (getContext() != null) {
            View infoView = mInflater.inflate(R.layout.list_card_two_items,
                    mContainer, false);

            String questionText = getTestTypesText(getContext(), questionPos);
            String answerText = getItemsList(getContext(), questionPos, answerPos);

            ((TextView) infoView.findViewById(R.id.card_title)).setText(questionText);
            ((TextView) infoView.findViewById(R.id.card_text)).setText(answerText);

            mContainer.addView(infoView);
        }
    }

    protected abstract String getTestTypesText(Context context, int questionPos);

    protected abstract String getItemsList(Context context, int questionPos, int answerPos);
}
