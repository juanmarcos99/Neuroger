package software.cneuro.neuroger.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 7/23/2015.
 */
public class InputSpinnerAdapter extends BaseAdapter {

    private final int mItemSelectedLayoutResource;
    private final int mItemDropdownLayoutResource;
    private final String[] mItems;

    private final LayoutInflater mInflater;

    public InputSpinnerAdapter(Context context,
                               int itemSelectedLayoutResource, int itemDropdownLayoutResource,
                               int textArrayResource) {
        mItemSelectedLayoutResource = itemSelectedLayoutResource;
        mItemDropdownLayoutResource = itemDropdownLayoutResource;
        mItems = context.getResources().getStringArray(textArrayResource);

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public InputSpinnerAdapter(Context context,
                               int itemSelectedLayoutResource, int itemDropdownLayoutResource,
                               String[] items) {
        mItemSelectedLayoutResource = itemSelectedLayoutResource;
        mItemDropdownLayoutResource = itemDropdownLayoutResource;
        mItems = items;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View workingView = convertView;
        ViewHolder holder;

        if (workingView == null) {
            workingView = mInflater.inflate(mItemSelectedLayoutResource,
                    parent, false);

            holder = new ViewHolder(workingView);
            workingView.setTag(holder);
        } else {
            holder = (ViewHolder) workingView.getTag();
        }

        holder.populate((String) getItem(position));

        return (workingView);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View workingView = convertView;
        ViewHolder holder;

        if (workingView == null) {
            workingView = mInflater.inflate(mItemDropdownLayoutResource,
                    parent, false);

            holder = new ViewHolder(workingView);
            workingView.setTag(holder);
        } else {
            holder = (ViewHolder) workingView.getTag();
        }

        holder.populate((String) getItem(position));

        return (workingView);
    }

    static class ViewHolder {
        private final TextView textView;

        public ViewHolder(View workingView) {
            textView = workingView
                    .findViewById(R.id.spinner_item_text);
        }

        public void populate(String text) {
            textView.setText(text);
        }

    }
}
