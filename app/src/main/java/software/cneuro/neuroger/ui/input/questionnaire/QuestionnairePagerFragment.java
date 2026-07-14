package software.cneuro.neuroger.ui.input.questionnaire;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import com.viewpagerindicator.PageIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationCDRHelper;
import software.cneuro.neuroger.content.EvaluationGDSHelper;
import software.cneuro.neuroger.content.EvaluationHHIESHelper;
import software.cneuro.neuroger.content.EvaluationLawtonHelper;
import software.cneuro.neuroger.content.EvaluationPfeifferHelper;
import software.cneuro.neuroger.content.EvaluationPsychoaffectiveHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.adapter.QuestionnairePagerAdapter;
import software.cneuro.neuroger.ui.input.BaseWarningFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertCDRTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertDepressionTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertHHIESTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertKatzTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertLawton_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPfeifferTest_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPsychoaffective_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertPyschofamilyTest_AsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuestionnairePagerFragment extends BaseWarningFragment implements
        InsertCDRTest_AsyncTask.OnCDRTestInserted,
        InsertPfeifferTest_AsyncTask.OnPfeifferTestInserted,
        InsertDepressionTest_AsyncTask.OnDepressionTestInserted,
        InsertHHIESTest_AsyncTask.OnHHIESTestInserted,
        InsertKatzTest_AsyncTask.OnKatzTestInserted,
        InsertLawton_AsyncTask.OnLawtonTestInserted,
        InsertPyschofamilyTest_AsyncTask.OnPsychofamilyTestInserted,
        InsertPsychoaffective_AsyncTask.OnPsychoaffectiveTestInserted {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_TOTAL_VIEWS = "total_views";
    public static final String ARG_QUESTIONNAIRE_TYPE = "type";
    private static final String ARG_CURRENT_PAGE = "current_page";
    public static final String ARG_SUBJECT_NAME = "patient_name";
    public static final String ARG_SUBJECT_GENDER = "patient_gender";

    private long mSubjectId;
    private int mTotalViews;
    private int mType;
    private String mName;
    private int mGender;

    private ViewPager mPager;
    private PageIndicator mIndicator;
    private ViewStub mEndBtnStub;

    private LinkedHashMap<Integer, SparseBooleanArray> mItems;
    private int mCurrentPage;
    private QuestionnairePagerListener mListener;

    // Dynamically load the C++ library into your application.
    static {
        System.loadLibrary("neuroger");
    }

    /**
     * A native method that is implemented by the native library,
     * which is packaged with this application.
     */
    public native double evaluateCDR(double[] values);

    public QuestionnairePagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
            if (getArguments().containsKey(ARG_TOTAL_VIEWS)) {
                mTotalViews = getArguments().getInt(ARG_TOTAL_VIEWS);
            }
            if (getArguments().containsKey(ARG_QUESTIONNAIRE_TYPE)) {
                mType = getArguments().getInt(ARG_QUESTIONNAIRE_TYPE);
            }
            if (getArguments().containsKey(ARG_SUBJECT_NAME)) {
                mName = getArguments().getString(ARG_SUBJECT_NAME);
            }
            if (getArguments().containsKey(ARG_SUBJECT_GENDER)) {
                mGender = getArguments().getInt(ARG_SUBJECT_GENDER);
            }
        }

        mItems = new LinkedHashMap<>(mTotalViews);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionnaire_pager, container, false);

        assert getContext() != null;
        rootView.findViewById(R.id.line_separator).setBackgroundColor(
                ContextCompat.getColor(getContext(), R.color.primary));

        mPager = rootView.findViewById(R.id.pager);
        mIndicator = rootView.findViewById(R.id.indicator);
        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentPage = position;

                        if (position == mTotalViews - 1) {
                            // if it is the last page of the test, make visible the finish
                            // button
                            if (mEndBtnStub != null) {
                                return;
                            }
                            assert getView() != null;
                            mEndBtnStub = getView().findViewById(R.id.stub_btn_end_test_container);
                            View inflated = mEndBtnStub.inflate();

                            Button endButton = inflated.findViewById(R.id.btn_action);
                            if (endButton != null) {
                                endButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // check if all the questions are answered
                                        if (mItems.size() != mTotalViews) {
                                            if (mListener != null) {
                                                mListener.showMissingAnswerMessage();
                                            }
                                        } else {
                                            showCheckDateTestBeforeLeavingMessage();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
        mPager.setPageTransformer(true, new DepthPageTransformer());

        // slower scroller down
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mPager.getContext(), new AccelerateInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(mPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
        outState.putInt(ARG_QUESTIONNAIRE_TYPE, mType);
        outState.putInt(ARG_TOTAL_VIEWS, mTotalViews);
        outState.putInt(ARG_CURRENT_PAGE, mCurrentPage);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
            mType = savedInstanceState.getInt(ARG_QUESTIONNAIRE_TYPE);
            mTotalViews = savedInstanceState.getInt(ARG_TOTAL_VIEWS);
            mCurrentPage = savedInstanceState.getInt(ARG_CURRENT_PAGE);
        }

        updateContent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (QuestionnairePagerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement QuestionnairePagerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == CHECK_TEST_BEFORE_LEAVING_DIALOG_ID)
            saveContent();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    @Override
    public void onCDRTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onPfeifferTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onDepressionTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onHHIESTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onKatzTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onLawtonTestInserted() {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onPsychofamilyTestInserted(long id) {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onPsychoaffectiveTestInserted(long id) {
        if (mListener != null) {
            mListener.onSaved();
        }
    }

    private void saveContent() {
        double score = getEvaluation(mItems, mType);

        assert getActivity() != null;
        // say something to the user and finish the activity
        switch (mType) {
            case Constant.QUESTIONNAIRE_CDR:
                DatabaseInserter.insertCDRTest(getActivity(), mSubjectId,
                        Objects.requireNonNull(mItems.get(0)).keyAt(0), // memory
                        Objects.requireNonNull(mItems.get(1)).keyAt(0), // orientation
                        Objects.requireNonNull(mItems.get(2)).keyAt(0), // judgment
                        Objects.requireNonNull(mItems.get(3)).keyAt(0), // community
                        Objects.requireNonNull(mItems.get(4)).keyAt(0), // hobbies
                        Objects.requireNonNull(mItems.get(5)).keyAt(0), // personal care
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_PFEIFFER:
                DatabaseInserter.insertPfeifferTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_GDS:
                DatabaseInserter.insertDepressionTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_HHIES:
                DatabaseInserter.insertHHIESTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_KATZ:
                DatabaseInserter.insertKatzTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_LAWTON:
                DatabaseInserter.insertLawtonTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            case Constant.QUESTIONNAIRE_PSYCHOFAMILY:
                DatabaseInserter.insertPsychofamilyTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
            default:
                DatabaseInserter.insertPsychoaffectiveTest(getActivity(), mSubjectId,
                        StringHelper.getJsonString(getAnswerList()),
                        score,
                        QuestionnairePagerFragment.this);
                break;
        }
    }

    private ArrayList<Integer> getAnswerList() {
        ArrayList<Integer> result = new ArrayList<>(mItems.size());
        for (Map.Entry<Integer, SparseBooleanArray> entry : mItems.entrySet()) {
            result.add(entry.getValue().keyAt(0));
        }
        return result;
    }

    private double getEvaluation(LinkedHashMap<Integer, SparseBooleanArray> items, int type) {
        double score = 0;

        assert getActivity() != null;
        switch (type) {
            case Constant.QUESTIONNAIRE_PFEIFFER:
                for (Map.Entry<Integer, SparseBooleanArray> entry : items.entrySet()) {
                    score += EvaluationPfeifferHelper.getScaleValue(getActivity(), entry.getValue().keyAt(0));
                }
                break;
            case Constant.QUESTIONNAIRE_CDR:
                double[] scalesValues = new double[6];

                for (int i = 0; i < 6; i++) {
                    int answerPosition = Objects.requireNonNull(mItems.get(i)).keyAt(0);
                    if (i == 5 && answerPosition >= 1) {
                        scalesValues[i] = EvaluationCDRHelper.getScaleValue(getActivity(), answerPosition + 1);
                    } else {
                        scalesValues[i] = EvaluationCDRHelper.getScaleValue(getActivity(), answerPosition);
                    }
                }

                score = evaluateCDR(scalesValues);
                break;
            case Constant.QUESTIONNAIRE_GDS:
                int i = 0;
                for (Map.Entry<Integer, SparseBooleanArray> entry : items.entrySet()) {
                    score += EvaluationGDSHelper.getScaleValue(getActivity(), i++, entry.getValue().keyAt(0));
                }
                break;
            case Constant.QUESTIONNAIRE_HHIES:
                for (Map.Entry<Integer, SparseBooleanArray> entry : items.entrySet()) {
                    score += EvaluationHHIESHelper.getScaleValue(getActivity(), entry.getValue().keyAt(0));
                }
                break;
            case Constant.QUESTIONNAIRE_KATZ:
                for (Map.Entry<Integer, SparseBooleanArray> entry : items.entrySet()) {
                    score += entry.getValue().keyAt(0);
                }
                break;
            case Constant.QUESTIONNAIRE_LAWTON:
                score = EvaluationLawtonHelper.evaluate(getActivity(),
                        Objects.requireNonNull(mItems.get(0)).keyAt(0), // phone
                        Objects.requireNonNull(mItems.get(1)).keyAt(0), // shopping
                        Objects.requireNonNull(mItems.get(2)).keyAt(0), // food
                        Objects.requireNonNull(mItems.get(3)).keyAt(0), // housekeeping
                        Objects.requireNonNull(mItems.get(4)).keyAt(0), // laundry
                        Objects.requireNonNull(mItems.get(5)).keyAt(0), // transportation
                        Objects.requireNonNull(mItems.get(6)).keyAt(0), // medication
                        Objects.requireNonNull(mItems.get(7)).keyAt(0));// finance
                break;
            case Constant.QUESTIONNAIRE_PSYCHOFAMILY:
                for (Map.Entry<Integer, SparseBooleanArray> entry : items.entrySet()) {
                    score += entry.getValue().keyAt(0) + 1; // position + 1
                }
                break;
            case Constant.QUESTIONNAIRE_PSYCHOAFFECTIVE:
                score = EvaluationPsychoaffectiveHelper.evaluate(getActivity(),
                        Objects.requireNonNull(mItems.get(0)).keyAt(0),  // sadness
                        Objects.requireNonNull(mItems.get(1)).keyAt(0),  // pessimism
                        Objects.requireNonNull(mItems.get(2)).keyAt(0),  // suicide
                        Objects.requireNonNull(mItems.get(3)).keyAt(0),  // sleepDisorder
                        Objects.requireNonNull(mItems.get(4)).keyAt(0),  // nervous
                        Objects.requireNonNull(mItems.get(5)).keyAt(0),  // lossSelfConfidence
                        Objects.requireNonNull(mItems.get(6)).keyAt(0)); // irritability
                break;
        }

        return score;
    }

    private void updateContent() {
        FragmentStatePagerAdapter mAdapter = new QuestionnairePagerAdapter(this, mType, mTotalViews, mName);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(mTotalViews);

        mIndicator.setViewPager(mPager);
    }

    public void onItemSelected(int position, boolean isChecked) {
        SparseBooleanArray selection = mItems.get(mCurrentPage);

        if (selection == null) {
            selection = new SparseBooleanArray();
            mItems.put(mCurrentPage, selection);
        }

        if (isChecked) {
            selection.put(position, true);
            // pass to the next question
            mPager.setCurrentItem(mCurrentPage + 1, true);
        } else {
            selection.delete(position);
        }
    }

    interface QuestionnairePagerListener {
        void showMissingAnswerMessage();

        void onSaved();
    }

    private static class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.50f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
