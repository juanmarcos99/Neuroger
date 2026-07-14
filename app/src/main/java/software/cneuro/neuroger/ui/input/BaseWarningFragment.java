package software.cneuro.neuroger.ui.input;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;

public abstract class BaseWarningFragment extends Fragment implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    protected static final int CHECK_TEST_BEFORE_LEAVING_DIALOG_ID = 150;

    protected void showCheckDateTestBeforeLeavingMessage() {
        FragmentManager fm = getChildFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_check_test_before_leaving_title),
                getString(R.string.dialog_check_test_before_leaving_message), CHECK_TEST_BEFORE_LEAVING_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }
}
