package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;

import software.cneuro.neuroger.content.HHIESHelper;

/**
 * Created by klaudia on 24/10/2016.
 */

public class HHIESFragment extends BaseQuestionnaireShortAnswerFragment {

    public HHIESFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment PfeifferFragment.
     */
    public static HHIESFragment newInstance(int pagePosition) {
        HHIESFragment fragment = new HHIESFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_POSITION, pagePosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    protected String[] getAnswerList(Context context) {
        return HHIESHelper.getAnswerList(context);
    }

    @Override
    protected String getQuestionText(Context context, int pagePosition) {
        return HHIESHelper.getQuestionText(context, mPagePosition);
    }

    @Override
    protected String getHelp(Context context, int pagePosition) {
        return HHIESHelper.getHelp(context, pagePosition);
    }
}
