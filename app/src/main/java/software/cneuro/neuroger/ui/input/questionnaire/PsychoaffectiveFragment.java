package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import software.cneuro.neuroger.content.PsychoaffectiveHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionnaireListener} interface
 * to handle interaction events.
 * Use the {@link KatzIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PsychoaffectiveFragment extends BaseQuestionnaireShortAnswerFragment {

    public PsychoaffectiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment KatzIndexFragment.
     */
    public static PsychoaffectiveFragment newInstance(int pagePosition) {
        PsychoaffectiveFragment fragment = new PsychoaffectiveFragment();
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
        return PsychoaffectiveHelper.getAnswerList(context);
    }

    @Override
    protected String getQuestionText(Context context, int pagePosition) {
        return PsychoaffectiveHelper.getQuestionText(context, mPagePosition);
    }

    @Override
    protected String getHelp(Context context, int pagePosition) {
        return PsychoaffectiveHelper.getHelp(context);
    }
}
