package software.cneuro.neuroger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import software.cneuro.neuroger.R;

public class SubjectDeleteResponseReceiver extends BroadcastReceiver {
    // Defines a custom Intent action
    public static final String BROADCAST_ACTION =
            "software.cneuro.neuroger.DELETE_BROADCAST";
    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS =
            "software.cneuro.neuroger.DELETE_STATUS";

    public SubjectDeleteResponseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getExtras().getString(EXTENDED_DATA_STATUS);

        Toast.makeText(context, !TextUtils.isEmpty(result) && result == JsonExportIntentService.ACTION_COMPLETED ?
                        context.getString(R.string.toast_subject_deleted) : context.getString(R.string.toast_subject_no_deleted),
                Toast.LENGTH_SHORT).show();
    }
}
