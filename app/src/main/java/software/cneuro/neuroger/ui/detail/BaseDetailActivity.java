package software.cneuro.neuroger.ui.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.ui.BaseActivity;
import software.cneuro.neuroger.ui.detail.carer.CarerDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.FrailScaleDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.MiniMentalStateDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.PathologicalAntecedentsDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.PhysicalPerformanceDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.SubjectiveEvaluationDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.CDRDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.GDSDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.HHIESDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.KatzIndexDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.LawtonDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PASDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PfeifferDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PsychoaffectiveDetailFragment;
import software.cneuro.neuroger.ui.detail.subject.SubjectDetailFragment;

public class BaseDetailActivity extends BaseActivity {
    public static final String ARG_TEST_TYPE = "type";

    protected String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mType = getIntent().getStringExtra(ARG_TEST_TYPE);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        switch (mType) {
            case Constant.CARD_DETAIL_SUBJECT:
                return new SubjectDetailFragment();
            case Constant.CARD_DETAIL_CARER:
                return new CarerDetailFragment();
            case Constant.CARD_DETAIL_FRAIL_SCALE:
                return new FrailScaleDetailFragment();
            case Constant.CARD_DETAIL_MINI_MENTAL_STATE:
                return new MiniMentalStateDetailFragment();
            case Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS:
                return new PathologicalAntecedentsDetailFragment();
            case Constant.CARD_DETAIL_PHYSICAL_EVALUATION:
                return new PhysicalPerformanceDetailFragment();
            case Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION:
                return new SubjectiveEvaluationDetailFragment();
            case Constant.CARD_DETAIL_CDR:
                return new CDRDetailFragment();
            case Constant.CARD_DETAIL_PFEIFFER:
                return new PfeifferDetailFragment();
            case Constant.CARD_DETAIL_GDS:
                return new GDSDetailFragment();
            case Constant.CARD_DETAIL_HHIES:
                return new HHIESDetailFragment();
            case Constant.CARD_DETAIL_KATZ_INDEX:
                return new KatzIndexDetailFragment();
            case Constant.CARD_DETAIL_LAWTON_MODIFIED_SCALE:
                return new LawtonDetailFragment();
            case Constant.CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE:
                return new PASDetailFragment();
            default:
                return new PsychoaffectiveDetailFragment();
        }
    }
}
