package software.cneuro.neuroger.ui.search;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.collection.LongSparseArray;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.ui.adapter.SearchSubjectRecyclerCursorAdapter;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neurogerdatabase.constant.Constant;
import software.cneuro.neurogerdatabase.database_async.DeletePatient_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.SearchCohabitant_AsyncTask;
import software.cneuro.neurogertheme.recyclerview.DividerItemDecoration;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchListFragment extends Fragment implements
        LoaderCallbacks<Cursor>,
        SearchSubjectRecyclerCursorAdapter.SearchItemRecyclerCallbacks,
        View.OnClickListener,
        DeletePatient_AsyncTask.OnPatientDeleted,
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks,
        SearchCohabitant_AsyncTask.OnCohabitantSearched {
    private static final String ARG_NAME_TO_SEARCH = "name_to_search";

    // These are the Contacts rows that we will retrieve.
    static final String[] PATIENT_SUMMARY_PROJECTION = new String[]{
            Constant.COL_PATIENT_ID,
            Constant.COL_PATIENT_FULLNAME,
            Constant.COL_PATIENT_NAME,
            Constant.COL_PATIENT_LASTNAME,
            Constant.COL_PATIENT_PHONE_NUMBER,
            Constant.COL_PATIENT_ID_NUMBER,
            Constant.COL_PATIENT_BIRTHDATE,
            Constant.COL_PATIENT_YEARS_STUDIES,
            Constant.COL_PATIENT_SEX,
            Constant.COL_PATIENT_COMPENSATED};

    private LongSparseArray<Boolean> mSelected;

    private TextView mAdapterCount;

    private String mName;
    private int mIsFemale;
    private boolean mIsCheckedGender, mIsCheckedGroupAge;
    private int mAgeFrom, mAgeTo;

    private OnSubjectListFragmentListener mListener;
    private SearchSubjectRecyclerCursorAdapter mAdapter;

    protected ActionMode mActionMode;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchListFragment() {
    }
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            SearchSubjectRecyclerCursorAdapter adapter = mAdapter;
            adapter.removeSelection();
            mActionMode = null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            // Assumes that you have "contexual.xml" menu resources
            inflater.inflate(R.menu.contextual_menu_search_list, menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            SearchSubjectRecyclerCursorAdapter adapter = mAdapter;
            mSelected = adapter.getSelectedIds();

            int itemId = item.getItemId();
            if (itemId == R.id.action_delete_list) {
                FragmentManager fm = getChildFragmentManager();
                AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                        getString(R.string.dialog_ask_before_deleting_title),
                        getString(R.string.dialog_ask_before_deleting_message), 0);
                dialogFragment.setAskBeforeLeavingCallbacks(SearchListFragment.this);
                dialogFragment.show(fm, "fragment_dialog_test");

                mActionMode = mode;
                return true;
            } else {
                mode.finish();
                return false;
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_NAME_TO_SEARCH, mName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mName = savedInstanceState.getString(ARG_NAME_TO_SEARCH);
        }

        updateContent(mName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_list, container, false);

        mAdapterCount = rootView.findViewById(R.id.search_count_result);

        /*
      The fragment's RecycleView.
     */
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        assert getActivity() != null;
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity().getResources().getDrawable(software.cneuro.neurogertheme.R.drawable.list_divider),
                        true, true));

        mAdapter = new SearchSubjectRecyclerCursorAdapter(mRecyclerView, getActivity(),
                null);
        mAdapter.setOnItemRecyclerAdapterCallbacksListener(this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constant.URI_TABLE_PATIENT;

        StringBuilder selectionSB = new StringBuilder();

        String selection = "((" + Constant.COL_PATIENT_FULLNAME + " NOTNULL))";

        if (!TextUtils.isEmpty(mName)) {
            selectionSB.append("(");
            selectionSB.append(Constant.COL_PATIENT_FULLNAME + " LIKE '%").append(mName).append("%'");
            selectionSB.append(") AND ");
        }

        if (mIsCheckedGroupAge) {
            Date from = CalendarHelper.getInitRangeDate(mAgeFrom);
            String fromFormat = CalendarHelper.dateFormatYearFirst.format(from);
            if (mAgeTo != CalendarHelper.NO_VALUE) {
                Date to = CalendarHelper.getEndRangeDate(mAgeTo);
                String toFormat = CalendarHelper.dateFormatYearFirst.format(to);

                selectionSB.append(Constant.COL_PATIENT_BIRTHDATE + " BETWEEN '")
                        .append(fromFormat)
                        .append("' AND '")
                        .append(toFormat).append("' AND ");
            } else {
                selectionSB.append(Constant.COL_PATIENT_BIRTHDATE + " >= '").append(fromFormat).append("' AND ");
                selectionSB.append(Constant.COL_PATIENT_BIRTHDATE + " >= ").append(fromFormat).append(" AND ");
            }
        }

        if (mIsCheckedGender) {
            selectionSB.append(Constant.COL_PATIENT_SEX + "=").append(mIsFemale).append(" AND ");
        }

        //String selection = null;
        if (selectionSB.length() > 0) {
            selection = selectionSB.substring(0, selectionSB.length() - 5);
        }

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                PATIENT_SUMMARY_PROJECTION,
                selection,
                null,
                Constant.COL_PATIENT_FULLNAME);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        int count = mAdapter.getItemCount();
        mAdapterCount.setText(getResources().getQuantityString(R.plurals.results, count, count));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSubjectListFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement OnSubjectListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnItemRecyclerClickListener(long id, String name, String lastName, String phone, int age, String birth, int levelOfSchooling, int gender, int compensated) {
        if (mActionMode == null) { // no items selected, so perform item click
            mListener.onSubjectListItemClick(id, name, lastName, phone, age, birth, levelOfSchooling, gender, compensated);
        } else { // add or remove selection for current list item
            onListItemCheck(id);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPatientDeleted() {
        Toast.makeText(getActivity(), getString(R.string.toast_delete_subject), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        Constant.SearchCohabitant(getActivity(), mSelected, SearchListFragment.this);

        mActionMode.finish(); // Action picked, so close the CAB
    }

    @Override
    public void OnCohabitantSearched(long[] mCohabitantIds) {
        Constant.deletePatient(getActivity(), mSelected, mCohabitantIds, SearchListFragment.this);
    }

    @Override
    public void onNegativeAnswer(int dialogId) {
        mActionMode.finish(); // Action picked, so close the CAB
    }

    @Override
    public void onLongClick(View v, long id) {
        onListItemCheck(id);
    }

    private void onListItemCheck(long id) {
        mAdapter.toggleSelection(id);
        boolean hasCheckedItems = mAdapter.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null) {
            assert getActivity() != null;
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
        } else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null) {
            mActionMode.setTitle(mAdapter.getSelectedCount()
                    + " " + getString(R.string.action_mode_selection));
        }
    }

    public void updateContent(String name) {
        mName = name;

        getLoaderManager().restartLoader(0, null, this);
    }

    public void updateContent(int isFemale, boolean isChecked) {
        mIsFemale = isFemale;
        mIsCheckedGender = isChecked;

        getLoaderManager().restartLoader(0, null,
                this);
    }

    public void updateContent(int from, int to, boolean isChecked) {
        mAgeFrom = from;
        mAgeTo = to;
        mIsCheckedGroupAge = isChecked;

        getLoaderManager().restartLoader(0, null,
                this);
    }

    /**
     * Destroy the action mode in case is activated.
     *
     * @return true in case the action mode is activated, false otherwise.
     */
    public boolean actionModeActivated() {
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSubjectListFragmentListener {
        void onSubjectListItemClick(long id, String name, String lastName, String phone, int age, String birth, int levelOfSchooling, int gender, int compensated);
    }

}
