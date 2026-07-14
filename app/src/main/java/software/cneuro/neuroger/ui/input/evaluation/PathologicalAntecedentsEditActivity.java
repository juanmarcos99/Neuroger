package software.cneuro.neuroger.ui.input.evaluation;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;

/**
 * Created by klaudia on 12/2/2015.
 */
public class PathologicalAntecedentsEditActivity extends AppCompatActivity implements
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    public static final String ARG_SUBJECT_NAME = "name";
    private static final int BACK_PRESS_DIALOG_ID = 12;

    private PathologicalAntecedentsEditFragment mPathologicalAntecedentsEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_header);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert getIntent() != null && getIntent().getExtras() != null;
        Bundle intent = getIntent().getExtras();
        ((TextView) findViewById(R.id.subject_detail_name)).setText(intent.getString(ARG_SUBJECT_NAME));

        if (savedInstanceState == null) {
            mPathologicalAntecedentsEditFragment = new PathologicalAntecedentsEditFragment();
            mPathologicalAntecedentsEditFragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPathologicalAntecedentsEditFragment).commit();
        }

        if (mPathologicalAntecedentsEditFragment == null) {
            mPathologicalAntecedentsEditFragment = (PathologicalAntecedentsEditFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        }
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
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }
}
