package software.cneuro.neuroger.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by klaudia on 7/9/2015.
 */
public class SearchSubjectRecyclerCursorAdapter extends
        CursorRecyclerViewAdapter<SearchSubjectRecyclerCursorAdapter.ViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {
    private final LayoutInflater mInflater;
    private final RecyclerView mRecycleView;
    private SearchItemRecyclerCallbacks mCallbacks;
    private final int mSelectedItemColor;
    private final int mColorTransparent;

    protected LongSparseArray<Boolean> mSelectedItemsIds;

    public SearchSubjectRecyclerCursorAdapter(RecyclerView recycle,
                                              final Context context, Cursor cursor) {
        super(context, cursor);

        mRecycleView = recycle;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mSelectedItemsIds = new LongSparseArray();
        mSelectedItemColor = ContextCompat.getColor(context,
                R.color.list_item_selected_action_mode);
        int mColorHighLight = ContextCompat.getColor(context,
                R.color.colorControlHighlight);
        mColorTransparent = ContextCompat.getColor(context,
                android.R.color.transparent);
    }

    public void setOnItemRecyclerAdapterCallbacksListener(
            SearchItemRecyclerCallbacks listener) {
        mCallbacks = listener;
    }

    public void toggleSelection(long id) {
        selectView(id, !mSelectedItemsIds.get(id, false));
    }

    public void removeSelection() {
        mSelectedItemsIds = new LongSparseArray();
        // TODO  Rely on notifyDataSetChanged as a last resort.
        notifyDataSetChanged();
    }

    public void selectView(long id, boolean value) {
        if (value) {
            mSelectedItemsIds.put(id, value);
        } else {
            mSelectedItemsIds.delete(id);
        }
        // TODO  Rely on notifyDataSetChanged as a last resort.
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public LongSparseArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String id = cursor.getString(cursor
                .getColumnIndex(Constant.COL_PATIENT_ID));
        String name = cursor.getString(cursor
                .getColumnIndex(Constant.COL_PATIENT_FULLNAME));

        viewHolder.populate(name, R.drawable.ic_list_avatar);

        viewHolder.setBackgroundColor(mSelectedItemsIds.get(Long.parseLong(id), false) ? mSelectedItemColor
                : mColorTransparent);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View workingView = mInflater.inflate(R.layout.one_line_list_item,
                parent, false);
        workingView.setOnClickListener(this);
        workingView.setOnLongClickListener(this);

        return new ViewHolder(workingView);
    }

    /**
     * list item click event
     */
    @Override
    public void onClick(View v) {
        int position = mRecycleView.getChildAdapterPosition(v);

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        String name = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_NAME
                ));
        String lastName = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_LASTNAME
                ));
        String phone = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_PHONE_NUMBER
                ));
        String birth = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_BIRTHDATE
                ));
        int gender = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATIENT_SEX
                ));
        int compensated = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATIENT_COMPENSATED
                ));
        int levelOfSchooling = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATIENT_YEARS_STUDIES
                ));

        mCallbacks.OnItemRecyclerClickListener(getItemId(position),
                name,
                lastName,
                phone,
                CalendarHelper.getAge(birth),
                birth,
                levelOfSchooling,
                gender,
                compensated);
    }

    @Override
    public boolean onLongClick(View v) {
        int position = mRecycleView.getChildAdapterPosition(v);

        mCallbacks.onLongClick(v, getItemId(position));
        return false;
    }

    public interface SearchItemRecyclerCallbacks {

        void OnItemRecyclerClickListener(long id,
                                         String name,
                                         String lastName,
                                         String phone,
                                         int age,
                                         String birth,
                                         int levelOfSchooling,
                                         int gender,
                                         int compensated);

        void onLongClick(View v, long id);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View workingView) {
            super(workingView);

            mView = workingView.findViewById(R.id.list_item_container);

            textView = workingView.findViewById(R.id.list_item_text);
            imageView = workingView
                    .findViewById(R.id.list_item_icon);
        }

        public void populate(String text, int imageResourceId) {
            textView.setText(text);
            imageView.setImageResource(imageResourceId);
        }

        public void setBackgroundColor(int color) {
            mView.setBackgroundColor(color);
        }
    }
}
