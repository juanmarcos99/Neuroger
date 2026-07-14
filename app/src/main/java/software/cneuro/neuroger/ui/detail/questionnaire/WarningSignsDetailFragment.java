package software.cneuro.neuroger.ui.detail.questionnaire;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.WarningSignHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class WarningSignsDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";

    // ALERT SIGNS
    static final String[] PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_ALERT_SIGNS_ANSWERS_LIST
    };

    private long mSubjectId;
    private ViewGroup mCardView1;
    private LayoutInflater mInflater;

    public WarningSignsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pathological_antecedents_detail, container, false);

        mCardView1 = rootView.findViewById(R.id.detail_card_container_1);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_ALERT_SIGNS;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_ALERT_SIGNS_COHABITANT_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && getActivity() != null) {
            mCardView1.removeAllViews();

            String answerPosJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_ALERT_SIGNS_ANSWERS_LIST));
            ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
            for (Integer position : answerPosList) {
                addNewInfo(mCardView1,
                        WarningSignHelper.getWarningSignText(getActivity(), position));
            }
        }
    }

    private void addNewInfo(ViewGroup container, String text) {
        View infoView = mInflater.inflate(R.layout.detail_text,
                container, false);

        TextView textView = infoView.findViewById(R.id.card_detail_text);
        textView.setText(text);

        container.addView(infoView);
    }
}
