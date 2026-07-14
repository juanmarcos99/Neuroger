package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;

import software.cneuro.neuroger.content.GDSHelper;

public class GDSFragment extends BaseQuestionnaireShortAnswerFragment {

    public GDSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment GDSFragment.
     */
    public static GDSFragment newInstance(int pagePosition) {
        GDSFragment fragment = new GDSFragment();
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
        return GDSHelper.getAnswerList(context);
    }

    @Override
    protected String getQuestionText(Context context, int pagePosition) {
        return GDSHelper.getQuestionText(context, mPagePosition);
    }

    @Override
    protected String getHelp(Context context, int pagePosition) {
        return GDSHelper.getHelp(context, pagePosition);
    }
}
