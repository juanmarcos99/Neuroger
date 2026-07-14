package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import software.cneuro.neuroger.content.PASHelper;

/**
 * Psychofamily assessment scale
 * <p>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionnaireListener} interface
 * to handle interaction events.
 * * Use the {@link CDRFragment#newInstance} factory method to
 * * create an instance of this fragment.
 */
public class PASFragment extends BaseQuestionnaireLongAnswerFragment {

    public PASFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition The position that determinate the list of questions to be displayed.
     * @return A new instance of fragment GDSFragment.
     */
    public static PASFragment newInstance(int pagePosition) {
        PASFragment fragment = new PASFragment();
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
        return PASHelper.getHelp(context);
    }

    @Override
    protected String getTestTypesText(Context context, int pagePosition) {
        return PASHelper.getTestTypesText(context, mPagePosition);
    }

    @Override
    protected String[] getItemsList(Context context, int pagePosition) {
        return PASHelper.getItemsList(context, mPagePosition);
    }
}
