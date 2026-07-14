package software.cneuro.neuroger.ui.input.evaluation;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import software.cneuro.neuroger.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnImageFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_IMAGE_RESOURCE_ID = "param1";

    private int mImageResId;

    private OnImageFragmentListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageResId Parameter 1.
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance(int imageResId) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageResId = getArguments().getInt(ARG_IMAGE_RESOURCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stimulus_image, container, false);

        rootView.findViewById(R.id.image_display).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_IMAGE_RESOURCE_ID, mImageResId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mImageResId = savedInstanceState.getInt(ARG_IMAGE_RESOURCE_ID);
        }

        updateContent(mImageResId);
    }

    public void updateContent(int imageResId) {
        mImageResId = imageResId;

        Picasso.with(getActivity())
                .load(mImageResId)
                .resize(500, 600)
                .onlyScaleDown()
                .centerInside()
                .into((ImageView) getView().findViewById(R.id.image_display));
    }

    @Override
    public void onClick(View v) {
        /*if (mListener != null) {
            mListener.onImageTouch();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnImageFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement OnImageFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnImageFragmentListener {
        void onImageTouch();
    }
}
