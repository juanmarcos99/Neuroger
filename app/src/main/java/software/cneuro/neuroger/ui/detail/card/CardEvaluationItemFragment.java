package software.cneuro.neuroger.ui.detail.card;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import software.cneuro.neuroger.R;

public class CardEvaluationItemFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_CARD_TAG = "tag";
    public static final String ARG_TITLE = "title";
    public static final String ARG_EVALUATION_ICON = "evaluation_icon";
    private static final String IS_DONE = "done";

    protected String mTag;
    protected String mTitle;

    protected @DrawableRes
    int mIcon;

    protected boolean mIsDone;

    protected CardEvaluationItemFragment.OnCardEvaluationFragmentListener mListener;

    public CardEvaluationItemFragment() {
    }

    @Override
    public void onClick(View v) {
        mListener.onCardEvaluationItemClick(mTag, mIsDone);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDone = false;

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_CARD_TAG)) {
                mTag = getArguments().getString(ARG_CARD_TAG);
            }
            if (getArguments().containsKey(ARG_TITLE)) {
                mTitle = getArguments().getString(ARG_TITLE);
            }
            if (getArguments().containsKey(ARG_EVALUATION_ICON)) {
                mIcon = getArguments().getInt(ARG_EVALUATION_ICON);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_evaluation_item_detail, container, false);

        rootView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_CARD_TAG,
                mTag);
        outState.putString(ARG_TITLE,
                mTitle);
        outState.putInt(ARG_EVALUATION_ICON,
                mIcon);
        outState.putBoolean(IS_DONE,
                mIsDone);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mTag = savedInstanceState
                    .getString(ARG_CARD_TAG);
            mTitle = savedInstanceState
                    .getString(ARG_TITLE);
            mIcon = savedInstanceState
                    .getInt(ARG_EVALUATION_ICON);
            mIsDone = savedInstanceState
                    .getBoolean(IS_DONE);
        }

        updateContent(mTag, mTitle);
    }

    public void updateContent(String tag, String title) {
        mTag = tag;
        mTitle = title;

        if (getView() != null) {
            TextView titleText = getView().findViewById(R.id.card_item_title);
            titleText.setText(mTitle);
        }
    }

    public void updateContent(boolean isDone, @DrawableRes int evaluationIcon) {
        mIsDone = isDone;

        if (getView() != null) {
            ImageView evalImage = getView().findViewById(R.id.card_image_to_do);
            evalImage.setImageResource(evaluationIcon);
        }
    }

    public boolean getIsDone() {
        return mIsDone;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CardEvaluationItemFragment.OnCardEvaluationFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
                    + " must implement OnCardEvaluationFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCardEvaluationFragmentListener {
        void onCardEvaluationItemClick(String tag, boolean isDone);
    }
}