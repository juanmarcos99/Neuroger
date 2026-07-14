package software.cneuro.neuroger.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.ui.input.questionnaire.CDRFragment;
import software.cneuro.neuroger.ui.input.questionnaire.GDSFragment;
import software.cneuro.neuroger.ui.input.questionnaire.HHIESFragment;
import software.cneuro.neuroger.ui.input.questionnaire.KatzIndexFragment;
import software.cneuro.neuroger.ui.input.questionnaire.LawtonFragment;
import software.cneuro.neuroger.ui.input.questionnaire.PASFragment;
import software.cneuro.neuroger.ui.input.questionnaire.PfeifferFragment;
import software.cneuro.neuroger.ui.input.questionnaire.PsychoaffectiveFragment;

/**
 * Created by klaudia on 02/08/2016.
 */
public class QuestionnairePagerAdapter extends FragmentStatePagerAdapter {
    private final FragmentManager mFragmentManager;
    private final SparseArray<String> mFragmentTags;
    private final int mTotalViews;
    private final int mType;
    private final String mName;

    public QuestionnairePagerAdapter(Fragment fragment, int type, int totalViews, String subjectName) {
        super(fragment.getChildFragmentManager());

        mFragmentManager = fragment.getChildFragmentManager();
        mFragmentTags = new SparseArray<>();
        mTotalViews = totalViews;
        mType = type;
        mName = subjectName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = getFragment(position);
        if (f != null) {
            return f;
        }

        switch (mType) {
            case Constant.QUESTIONNAIRE_CDR:
                return CDRFragment.newInstance(position);
            case Constant.QUESTIONNAIRE_PFEIFFER:
                return PfeifferFragment.newInstance(position, mName);
            case Constant.QUESTIONNAIRE_GDS:
                return GDSFragment.newInstance(position);
            case Constant.QUESTIONNAIRE_HHIES:
                return HHIESFragment.newInstance(position);
            case Constant.QUESTIONNAIRE_KATZ:
                return KatzIndexFragment.newInstance(position);
            case Constant.QUESTIONNAIRE_LAWTON:
                return LawtonFragment.newInstance(position);
            case Constant.QUESTIONNAIRE_PSYCHOFAMILY:
                return PASFragment.newInstance(position);
            default:
                return PsychoaffectiveFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mTotalViews;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }

        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            return mFragmentManager.findFragmentByTag(tag);
        }

        return null;
    }
}
