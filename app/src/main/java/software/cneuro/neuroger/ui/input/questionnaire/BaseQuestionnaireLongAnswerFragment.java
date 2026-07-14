package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.cneuro.neuroger.R;

public abstract class BaseQuestionnaireLongAnswerFragment extends BaseQuestionnaireFragment {

    public BaseQuestionnaireLongAnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.questionnaire_answer_full_mode, container, false);
        mContainer = rootView.findViewById(R.id.radiogroup_questionary_answer);

        return rootView;
    }

    @Override
    protected void updateContent() {
        assert getView() != null;
        ((TextView) getView().findViewById(R.id.questionary_type)).setText(
                getTestTypesText(getActivity(), mPagePosition));

        String[] questions = getItemsList(getActivity(), mPagePosition);

        for (int i = 0; i < questions.length; i++) {
            addNewInfo(questions[i], i);
        }
    }

    protected abstract String getTestTypesText(Context context, int pagePosition);

    protected abstract String[] getItemsList(Context context, int pagePosition);
}
