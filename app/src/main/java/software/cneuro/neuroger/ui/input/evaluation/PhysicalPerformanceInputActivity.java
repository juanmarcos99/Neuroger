package software.cneuro.neuroger.ui.input.evaluation;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.FileHelper;
import software.cneuro.neuroger.content.IntentHelper;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;

public class PhysicalPerformanceInputActivity extends AppCompatActivity implements
        PhysicalPerformanceInputFragment.OnPhysicalPerformanceInteractionListener,
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    public static final String ARG_SUBJECT_NAME = "name";
    private static final int BACK_PRESS_DIALOG_ID = 12;
    private static final int CHECK_TEST_BEFORE_LEAVING_DIALOG_ID = 15;

    private PhysicalPerformanceInputFragment mPhysicalPerformanceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_header);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle intent = getIntent().getExtras();
        assert intent != null;
        ((TextView) findViewById(R.id.subject_detail_name)).setText(intent.getString(ARG_SUBJECT_NAME));

        if (savedInstanceState == null) {
            mPhysicalPerformanceFragment = new PhysicalPerformanceInputFragment();
            mPhysicalPerformanceFragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPhysicalPerformanceFragment).commit();
        }

        if (mPhysicalPerformanceFragment == null) {
            mPhysicalPerformanceFragment = (PhysicalPerformanceInputFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showHelp(int helpID) {
        File helpFile = FileHelper.getHelpFile(this, helpID);
        IntentHelper.startPDFReaderIntent(this, helpFile);
    }

    @Override
    public void onSaveContent() {
        FragmentManager fm = getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_check_test_before_leaving_title),
                getString(R.string.dialog_check_test_before_leaving_message), CHECK_TEST_BEFORE_LEAVING_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    @Override
    public void onSaved() {
        Toast.makeText(this, getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);

        finish();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        AskBeforeLeavingFragment dialogFragment = AskBeforeLeavingFragment.newInstance(
                getString(R.string.dialog_ask_before_leaving_title),
                getString(R.string.dialog_ask_before_leaving_message), BACK_PRESS_DIALOG_ID);
        dialogFragment.setAskBeforeLeavingCallbacks(this);
        dialogFragment.show(fm, "fragment_dialog");
    }

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == BACK_PRESS_DIALOG_ID)
            finish();
        else
            mPhysicalPerformanceFragment.saveContent();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }
}
