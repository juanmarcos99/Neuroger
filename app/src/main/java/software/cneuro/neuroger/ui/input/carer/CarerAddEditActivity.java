package software.cneuro.neuroger.ui.input.carer;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;

public class CarerAddEditActivity extends AppCompatActivity implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    public static final String ARG_CARER_NAME = "edit_carer_name";

    private CarerAddEditFragment mCarerInputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_header);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle intent = getIntent().getExtras();
        if (intent != null && getIntent().hasExtra(CarerAddEditFragment.ARGUMENT_EDIT_CARER_ID)) {
            getSupportActionBar().setTitle(R.string.title_activity_subject_edit);
            ((TextView) findViewById(R.id.subject_detail_name)).setText(intent.getString(ARG_CARER_NAME));
        } else {
            getSupportActionBar().setTitle(R.string.title_activity_carer_input);
        }

        if (savedInstanceState == null) {
            mCarerInputFragment = new CarerAddEditFragment();
            mCarerInputFragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mCarerInputFragment).commit();
        }

        if (mCarerInputFragment == null) {
            mCarerInputFragment = (CarerAddEditFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_ask_before_leaving_title),
                getString(R.string.dialog_ask_before_leaving_message), 0);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        finish();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }
}
