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

public class CardIconAndEvalItemFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_CARD_TAG = "tag";
    public static final String ARG_TITLE = "title";
    public static final String ARG_ICON = "icon";
    private static final String IS_DONE = "done";

    protected String mTag;
    protected String mTitle;

    protected @DrawableRes
    int mIcon;

    protected boolean mIsDone;

    protected OnCardIconItemFragmentListener mListener;

    public CardIconAndEvalItemFragment() {
    }

    @Override
    public void onClick(View v) {
        mListener.onCardIconItemClick(mTag, mIsDone);
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
            if (getArguments().containsKey(ARG_ICON)) {
                mIcon = getArguments().getInt(ARG_ICON);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_icon_detail, container, false);

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
        outState.putInt(ARG_ICON,
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
                    .getInt(ARG_ICON);
            mIsDone = savedInstanceState
                    .getBoolean(IS_DONE);
        }

        updateContent(mTag, mTitle, mIcon);
    }

    public void updateContent(String tag, String title, @DrawableRes int descriptiveIcon) {
        mTag = tag;
        mTitle = title;
        mIcon = descriptiveIcon;

        if (getView() != null) {
            TextView titleText = getView().findViewById(R.id.card_item_title);
            titleText.setText(mTitle);

            ImageView iconImageView = getView().findViewById(R.id.card_descriptive_icon);
            iconImageView.setImageResource(mIcon);
        }
    }

    public void updateContent(boolean isDone, @DrawableRes int evaluationIcon) {
        mIsDone = isDone;

        if (getView() != null) {
            ImageView doneImage = getView().findViewById(R.id.card_evaluation_icon);
            doneImage.setImageResource(evaluationIcon);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCardIconItemFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
                    + " must implement OnCardIconItemFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCardIconItemFragmentListener {
        void onCardIconItemClick(String tag, boolean isDone);
    }
}
