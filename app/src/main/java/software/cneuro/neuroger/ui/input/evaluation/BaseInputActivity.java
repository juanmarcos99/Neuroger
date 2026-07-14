package software.cneuro.neuroger.ui.input.evaluation;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.ui.BaseActivity;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.input.carer.CarerAddEditFragment;
import software.cneuro.neuroger.ui.input.subject.SubjectAddEditFragment;

public class BaseInputActivity extends BaseActivity implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    private static final int BACK_PRESS_DIALOG_ID = 12;
    public static final String ARG_TEST_TYPE = "type";

    protected String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mType = getIntent().getStringExtra(ARG_TEST_TYPE);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_ask_before_leaving_title),
                getString(R.string.dialog_ask_before_leaving_message), BACK_PRESS_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == BACK_PRESS_DIALOG_ID)
            finish();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    @Override
    protected Fragment getFragment() {
        switch (mType) {
            case Constant.CARD_DETAIL_SUBJECT:
                return new SubjectAddEditFragment();
            case Constant.CARD_DETAIL_CARER:
                return new CarerAddEditFragment();
            case Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS:
                return new PathologicalAntecedentsInputFragment();
            case Constant.CARD_DETAIL_PHYSICAL_EVALUATION:
                return new PhysicalPerformanceInputFragment();
            case Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION:
                return new SubjectiveEvaluationInputFragment();
            case Constant.CARD_DETAIL_MINI_MENTAL_STATE:
                return new MiniMentalStateInputFragment();
            case Constant.CARD_DETAIL_FRAIL_SCALE:
                return new FrailScaleInputFragment();
            default:
                return new WarningSignsFragment();
        }
    }
}
