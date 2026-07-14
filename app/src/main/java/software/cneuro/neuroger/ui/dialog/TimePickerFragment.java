package software.cneuro.neuroger.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String ARG_DATE = "arg_date";

    private Date mDate;

    private TimePickerCallbacks mCallbacks;

    public interface TimePickerCallbacks {
        void onDataSet(Date date);
    }

    public void setOnDialogDateCallbacks(TimePickerCallbacks listener) {
        mCallbacks = listener;
    }

    public static TimePickerFragment mInstance;
    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment frag = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        frag.setArguments(args);
        return mInstance = frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_DATE)
                    && getArguments().getSerializable(ARG_DATE) instanceof Date) {
                mDate = (Date) getArguments().getSerializable(ARG_DATE);
            } else {
                mDate = null;
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        if (mDate != null) {
            c.setTime(mDate);
        } else {
            mDate = c.getTime();
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        mCallbacks.onDataSet(calendar.getTime());
    }

}
