package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import software.cneuro.neuroger.content.PfeifferHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionnaireListener} interface
 * to handle interaction events.
 * Use the {@link PfeifferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PfeifferFragment extends BaseQuestionnaireShortAnswerFragment {

    public static final String ARG_SUBJECT_NAME = "patient_name";
    private String mName;

    public PfeifferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment PfeifferFragment.
     */
    public static PfeifferFragment newInstance(int pagePosition, String name) {
        PfeifferFragment fragment = new PfeifferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT_NAME, name);
        args.putInt(ARG_PAGE_POSITION, pagePosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null && getArguments().containsKey(ARG_SUBJECT_NAME)) {
            mName = getArguments().getString(ARG_SUBJECT_NAME);
        }
    }

    @Override
    protected String[] getAnswerList(Context context) {
        return PfeifferHelper.getAnswerList(context);
    }

    @Override
    protected String getHelp(Context context, int pagePosition) {
        return PfeifferHelper.getHelp(context, 0);
    }

    @Override
    protected String getQuestionText(Context context, int pagePosition) {
        return PfeifferHelper.getQuestionText(context, mPagePosition, mName);
    }
}
