package software.cneuro.neuroger.ui.input.evaluation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.UIHelper;

public class StimulusImageActivity extends AppCompatActivity implements ImageFragment.OnImageFragmentListener {
    private static final String IS_NAV_AND_NOT_BAR_VISIBLE = "arg_is_visible";

    private ImageFragment mFragment;
    private boolean mIsNavAndNotifBarVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimulus_image);

        mIsNavAndNotifBarVisible = true;

        if (savedInstanceState == null) {
            mFragment = new ImageFragment();
            mFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_container, mFragment).commit();
        }

        if (mFragment == null) {
            mFragment = (ImageFragment) getSupportFragmentManager().findFragmentById(R.id.image_container);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(IS_NAV_AND_NOT_BAR_VISIBLE, mIsNavAndNotifBarVisible);
    }

    @Override
    public void onImageTouch() {
        // hide toolbar and notification bar
        if (mIsNavAndNotifBarVisible) {
            getSupportActionBar().hide();

            UIHelper.hideSystemUI(getWindow().getDecorView());
        } else {
            getSupportActionBar().show();

            UIHelper.showSystemUI(getWindow().getDecorView());
        }

        mIsNavAndNotifBarVisible = !mIsNavAndNotifBarVisible;
    }
}
