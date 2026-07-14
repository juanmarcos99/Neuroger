package software.cneuro.neuroger.ui.detail.carer;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.ui.input.carer.CarerAddEditActivity;
import software.cneuro.neuroger.ui.input.carer.CarerAddEditFragment;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class CarerDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_CARER_ID = "carer_id";
    public static final int EDIT_ACTIVITY_REQUEST_CODE = 144;

    private long mCarerId;
    private String mName;
    private long mSubjectId;

    private ViewGroup mCardView1;
    private ViewGroup mCardView2;
    private LayoutInflater mInflater;

    public CarerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null && getArguments().containsKey(ARG_CARER_ID)) {
            mCarerId = getArguments().getLong(ARG_CARER_ID);
            mSubjectId = getArguments().getLong(CarerAddEditFragment.ARG_SUBJECT_ID, software.cneuro.neuroger.constant.Constant.NO_ID);
            mName = getArguments().getString(CarerAddEditActivity.ARG_CARER_NAME);
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_carer_detail, container, false);

        mCardView1 = (ViewGroup) rootView.findViewById(R.id.detail_card_container_1);
        mCardView2 = (ViewGroup) rootView.findViewById(R.id.detail_card_container_2);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            assert getActivity() != null;
            Intent intent = new Intent(getActivity(), CarerAddEditActivity.class);
            intent.putExtra(CarerAddEditFragment.ARG_SUBJECT_ID, mSubjectId);
            intent.putExtra(CarerAddEditFragment.ARGUMENT_EDIT_CARER_ID, mCarerId);
            intent.putExtra(CarerAddEditActivity.ARG_CARER_NAME, mName);
            startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_CARER_ID, mCarerId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mCarerId = savedInstanceState.getLong(ARG_CARER_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // clean all the data to prepared for the new load
        cleanData();

        getLoaderManager().restartLoader(0, null, this);
    }

    private void cleanData() {
        mCardView1.removeAllViews();
        mCardView2.removeAllViews();
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CARER_SUMMARY_PROJECTION = new String[]{
            Constant.COL_COHABITANT_NAME, Constant.COL_COHABITANT_LASTNAME,
            Constant.COL_COHABITANT_AGE, Constant.COL_COHABITANT_SEX, Constant.COL_COHABITANT_FAMILIARITY,
            Constant.COL_COHABITANT_PHONE_NUMBER, Constant.COL_COHABITANT_FULLNAME};

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constant.URI_TABLE_COHABITANT;

        String select = "((" + Constant.COL_COHABITANT_ID + " = " + mCarerId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                CARER_SUMMARY_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst() && getContext() != null) {
            String name = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_COHABITANT_FULLNAME
                    ));
            String age = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_COHABITANT_AGE
                    ));
            String genderId = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_COHABITANT_SEX
                    ));
            addNewInfo(mCardView1, getString(R.string.label_name), name);
            addNewInfo(mCardView1, getString(R.string.label_age), age);
            addNewInfo(mCardView1, getString(R.string.label_gender), SubjectHelper.getGenderText(getActivity(),
                    EvaluationPPHelper.zeroIfEmpty(genderId)));

            String familiarity = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_COHABITANT_FAMILIARITY
                    ));
            String phone = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_COHABITANT_PHONE_NUMBER
                    ));
            addNewInfo(mCardView2, getString(R.string.label_familiarity), SubjectHelper.getFamiliarityText(getContext(),
                    EvaluationPPHelper.zeroIfEmpty(familiarity)));
            addNewInfo(mCardView2, getString(R.string.label_phone), phone);
        }
    }

    private void addNewInfo(ViewGroup container, String title, String text) {
        View infoView = mInflater.inflate(R.layout.detail_text_title,
                container, false);

        TextView titleView = (TextView) infoView.findViewById(R.id.detail_card_title);
        titleView.setText(title);
        TextView textView = (TextView) infoView.findViewById(R.id.detail_card_text);
        textView.setText(text);

        container.addView(infoView);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert getActivity() != null;
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        }
    }
}
