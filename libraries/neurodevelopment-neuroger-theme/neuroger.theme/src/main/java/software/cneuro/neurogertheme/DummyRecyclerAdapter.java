package software.cneuro.neurogertheme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by klaudia on 5/20/2015.
 */
public class DummyRecyclerAdapter extends
        RecyclerView.Adapter<DummyRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {
    private final String[] mItems;
    private final String[] mSubItems;
    private final Integer[] mImages;
    private final LayoutInflater mInflater;
    private final RecyclerView mRecycleView;

    private ItemRecyclerCallbacks mCallbacks;

    public interface ItemRecyclerCallbacks {

        void OnItemClickListener(View icon, int position, String text, String subText, int imageReourceID);

    }

    public void setOnItemRecyclerAdapterCallbacksListener(
            ItemRecyclerCallbacks listener) {
        mCallbacks = listener;
    }

    public DummyRecyclerAdapter(RecyclerView recycle,
                                final Context context, String[] items, String[] subItems, Integer[] images) {
        mRecycleView = recycle;
        mItems = items;
        mImages = images;
        mSubItems = subItems;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(View workingView) {
            super(workingView);

            textView = workingView.findViewById(R.id.list_item_text);
            imageView = workingView
                    .findViewById(R.id.list_item_icon);
        }

        public void populate(String text, String subText, int imageResoureID) {
            textView.setText(text);
            imageView.setImageResource(imageResoureID);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(mItems[position], mSubItems[position], mImages[position]);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View workingView = mInflater.inflate(R.layout.one_line_list_item,
                parent, false);
        workingView.setOnClickListener(this);

        return new ViewHolder(workingView);
    }

    /**
     * list item click event
     */
    @Override
    public void onClick(View v) {
        int position = mRecycleView.getChildAdapterPosition(v);

        mCallbacks.OnItemClickListener(v.findViewById(R.id.list_item_icon), position, mItems[position], mSubItems[position], mImages[position]);
    }
}
