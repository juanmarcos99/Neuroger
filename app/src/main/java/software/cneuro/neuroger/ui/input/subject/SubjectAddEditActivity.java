package software.cneuro.neuroger.ui.input.subject;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;

public class SubjectAddEditActivity extends AppCompatActivity implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    private SubjectAddEditFragment mSubjectInputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_header);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle intent = getIntent().getExtras();
        ((TextView) findViewById(R.id.subject_detail_name)).setText(R.string.patient);
        if (intent != null && getIntent().hasExtra(SubjectAddEditFragment.ARGUMENT_EDIT_PATIENT_ID)) {
            getSupportActionBar().setTitle(R.string.title_activity_subject_edit);
        } else {
            getSupportActionBar().setTitle(R.string.title_activity_subject_input);
        }

        if (savedInstanceState == null) {
            mSubjectInputFragment = new SubjectAddEditFragment();
            mSubjectInputFragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mSubjectInputFragment).commit();
        }

        if (mSubjectInputFragment == null) {
            mSubjectInputFragment = (SubjectAddEditFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
        }
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
