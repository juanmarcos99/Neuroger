package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import software.cneuro.neuroger.content.CDRHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionnaireListener} interface
 * to handle interaction events.
 * Use the {@link CDRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CDRFragment extends BaseQuestionnaireLongAnswerFragment {

    public CDRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment CDRFragment.
     */
    public static CDRFragment newInstance(int pagePosition) {
        CDRFragment fragment = new CDRFragment();
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
    protected String getHelp(Context context, int pagePosition) {
        return CDRHelper.getHelp(context, mPagePosition);
    }

    @Override
    protected String getTestTypesText(Context context, int pagePosition) {
        return CDRHelper.getTestTypesText(context, mPagePosition);
    }

    @Override
    protected String[] getItemsList(Context context, int pagePosition) {
        return CDRHelper.getItemsList(context, mPagePosition);
    }
}
