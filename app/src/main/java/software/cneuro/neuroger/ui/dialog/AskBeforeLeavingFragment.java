package software.cneuro.neuroger.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 9/8/2015.
 */
public class AskBeforeLeavingFragment extends DialogFragment {
    private static final String ARG_DIALOG_ID = "dialog_id";
    public static String ARG_TITLE = "arg_title";
    public static String ARG_MESSAGE = "arg_message";

    public static AskBeforeLeavingFragment mInstance;
    private static final AskBeforeLeavingCallbacks sCallbacks = new AskBeforeLeavingCallbacks() {

        @Override
        public void onPositiveAnswer(int dialogId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNegativeAnswer(int dialogId) {
            // TODO Auto-generated method stub
        }
    };

    private String mTitle, mMessage;
    // Use this instance of the interface to deliver action events
    private AskBeforeLeavingCallbacks mCallbacks;
    private int mDialogId;

    public static AskBeforeLeavingFragment newInstance(String title, String message, int dialogId) {
        AskBeforeLeavingFragment frag = new AskBeforeLeavingFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        arguments.putInt(ARG_DIALOG_ID, dialogId);
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
            if (getArguments().containsKey(ARG_DIALOG_ID)) {
                mDialogId = getArguments().getInt(ARG_DIALOG_ID);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        assert getActivity() != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_text, null);
        ((TextView) view.findViewById(R.id.dialog_item_text)).setText(mMessage);

        builder.setTitle(mTitle)
                .setView(view)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mCallbacks.onPositiveAnswer(mDialogId);
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mCallbacks.onNegativeAnswer(mDialogId);
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setAskBeforeLeavingCallbacks(AskBeforeLeavingCallbacks listener) {
        mCallbacks = listener;
    }

    public interface AskBeforeLeavingCallbacks {
        void onPositiveAnswer(int dialogId);

        void onNegativeAnswer(int dialogId);
    }
}
