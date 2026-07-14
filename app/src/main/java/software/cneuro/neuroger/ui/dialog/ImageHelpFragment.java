package software.cneuro.neuroger.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 01/04/2016.
 */
public class ImageHelpFragment extends DialogFragment {
    public static String ARG_TITLE = "arg_title";
    public static final String ARG_IMAGE_RESOURCE_ID = "arg_image";

    private String mTitle;
    private int mImageResourceId;
    private LayoutInflater mInflater;


    public static ImageHelpFragment mInstance;

    public static ImageHelpFragment newInstance(String title, int imageResourceID) {
        ImageHelpFragment frag = new ImageHelpFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putInt(ARG_IMAGE_RESOURCE_ID, imageResourceID);
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
            if (getArguments().containsKey(ARG_IMAGE_RESOURCE_ID)) {
                mImageResourceId = getArguments().getInt(ARG_IMAGE_RESOURCE_ID);
            }
        }

        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = mInflater.inflate(R.layout.dialog_image,
            null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_item_icon);
        Picasso.with(getActivity())
                .load(mImageResourceId)
                .resize(500, 600)
                .centerInside()
                .into(imageView);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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