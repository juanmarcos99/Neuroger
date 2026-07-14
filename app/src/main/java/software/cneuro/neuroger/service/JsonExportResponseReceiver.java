package software.cneuro.neuroger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Objects;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 20/01/2016.
 */
public class JsonExportResponseReceiver extends BroadcastReceiver {
    // Defines a custom Intent action
    public static final String BROADCAST_ACTION =
            "software.cneuro.neuroger.JSON_BROADCAST";
    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS =
            "software.cneuro.neuroger.JSON_STATUS";

    // Prevents instantiation
    public JsonExportResponseReceiver() {
    }

    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    public void onReceive(Context context, Intent intent) {
        String result = intent.getExtras().getString(EXTENDED_DATA_STATUS);

        assert result != null;
        Toast.makeText(context, !TextUtils.isEmpty(result) && Objects.equals(result, JsonExportIntentService.ACTION_COMPLETED) ?
                        context.getString(R.string.toast_data_exported) : context.getString(R.string.toast_data_no_exported),
                Toast.LENGTH_SHORT).show();

    }
}
