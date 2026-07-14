package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.HelpFragment;

public abstract class BaseQuestionnaireFragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener {
    protected static final String ARG_PAGE_POSITION = "page_position";

    protected int mPagePosition;

    protected LayoutInflater mInflater;

    protected QuestionnaireListener mListener;
    protected RadioGroup mContainer;

    public BaseQuestionnaireFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPagePosition = getArguments().getInt(ARG_PAGE_POSITION);
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_help, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_help) {
            assert getActivity() != null;
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();

            HelpFragment dialogFragment = HelpFragment
                    .newInstance(getString(R.string.dialog_help_title),
                            getHelp(getActivity(), mPagePosition));
            dialogFragment.show(fm, "fragment_dialog_test");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateContent();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mListener != null) {
            mListener.onItemSelected(buttonView.getId(), isChecked);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof QuestionnaireListener) {
            mListener = (QuestionnaireListener) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement QuestionnaireListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void addNewInfo(String text, int position) {
        RadioButton radioButton = (RadioButton) mInflater.inflate(
                R.layout.input_radiobtn_answer_questionnaire,
                mContainer, false);

        radioButton.setId(position);
        radioButton.setText(text);
        radioButton.setOnCheckedChangeListener(this);

        mContainer.addView(radioButton);
    }

    protected abstract void updateContent();

    protected abstract String getHelp(Context context, int pagePosition);
}
