package software.cneuro.neuroger.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 9/8/2015.
 */
public class HelpFragment extends DialogFragment {
    public static String ARG_TITLE = "arg_title";
    public static final String ARG_MESSAGE = "arg_message";

    private String mTitle, mMessage;

    public static HelpFragment mInstance;

    public static HelpFragment newInstance(String title, String message) {
        HelpFragment frag = new HelpFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        frag.setArguments(arguments);
        return mInstance = frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_TITLE)) {
                mTitle = getArguments().getString(ARG_TITLE);
            }
            if (getArguments().containsKey(ARG_MESSAGE)) {
                mMessage = getArguments().getString(ARG_MESSAGE);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_text, null);
        ((TextView) view.findViewById(R.id.dialog_item_text)).setText(mMessage);

        builder.setTitle(mTitle)
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
