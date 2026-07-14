package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 24/10/2016.
 */

public abstract class BaseQuestionnaireShortAnswerFragment extends BaseQuestionnaireFragment {

    protected String[] mAnswers;

    public BaseQuestionnaireShortAnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnswers = getAnswerList(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.questionnaire_question_full_mode, container, false);
        mContainer = rootView.findViewById(R.id.radiogroup_questionary_answer);

        return rootView;
    }

    @Override
    protected void updateContent() {
        assert getView() != null;
        ((TextView) getView().findViewById(R.id.questionary_text)).setText(
                getQuestionText(getActivity(), mPagePosition));

        for (int i = 0; i < mAnswers.length; i++) {
            addNewInfo(mAnswers[i], i);
        }
    }

    protected abstract String getQuestionText(Context context, int pagePosition);

    protected abstract String[] getAnswerList(Context context);
}
