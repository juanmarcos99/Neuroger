package software.cneuro.neuroger.ui.adapter;

import android.content.Context;

import software.cneuro.neuroger.content.CalendarHelper;

/**
 * Created by klaudia on 12/14/2015.
 */
public class GroupAgeSpinnerAdapter extends InputSpinnerAdapter {
    public GroupAgeSpinnerAdapter(Context context, int itemSelectedLayoutResource, int itemDropdownLayoutResource, int textArrayResource) {
        super(context, itemSelectedLayoutResource, itemDropdownLayoutResource, textArrayResource);
    }

    /**
     * Get the range of ages.
     *
     * @param position Position in the list.
     * @return The range of ages, int[] { from, to }.
     */
    public int[] getAgesRange(int position) {
        // TODO: use regular expressions.
        switch (position) {
            case 0:
                return new int[] { 59, 50 };
            case 1:
                return new int[] { 69, 60 };
            case 2:
                return new int[] { 79, 70 };
            case 3:
                return new int[] { 89, 80 };
            case 4:
                return new int[] { 99, 90 };
            case 5:
                return new int[] {CalendarHelper.NO_VALUE, 100 };
        }
        return new int[] {};
    }
}
