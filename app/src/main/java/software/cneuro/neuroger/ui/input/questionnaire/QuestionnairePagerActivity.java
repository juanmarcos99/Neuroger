package software.cneuro.neuroger.ui.input.questionnaire;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.ui.dialog.AskBeforeLeavingFragment;
import software.cneuro.neuroger.ui.dialog.HelpFragment;

public class QuestionnairePagerActivity extends AppCompatActivity implements
        QuestionnaireListener,
        QuestionnairePagerFragment.QuestionnairePagerListener,
        AskBeforeLeavingFragment.AskBeforeLeavingCallbacks {
    public static final String ARG_SUBJECT_NAME = "name";
    public static final String ARG_TITLE = "title";
    private static final int BACK_PRESS_DIALOG_ID = 12;

    private QuestionnairePagerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_pager);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        assert getIntent() != null && getIntent().getExtras() != null;
        Bundle intent = getIntent().getExtras();
        if (!TextUtils.isEmpty(intent.getString(ARG_TITLE)))
            getSupportActionBar().setTitle(intent.getString(ARG_TITLE));

        ((TextView) findViewById(R.id.subject_detail_name)).setText(intent.getString(ARG_SUBJECT_NAME));

        if (savedInstanceState == null) {
            mFragment = new QuestionnairePagerFragment();
            mFragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment).commit();
        }

        if (mFragment == null) {
            mFragment = (QuestionnairePagerFragment) getSupportFragmentManager().findFragmentById(R.id.container);
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
    public void onItemSelected(int position, boolean isChecked) {
        mFragment.onItemSelected(position, isChecked);
    }

    @Override
    public void showMissingAnswerMessage() {
        FragmentManager fm = getSupportFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                getString(R.string.dialog_missing_answer_message));
        dialogFragment.show(fm, "fragment_dialog");
    }

    @Override
    public void onSaved() {
        Toast.makeText(this,
                getString(R.string.toast_saved_test),
                Toast.LENGTH_SHORT).show();

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
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }
}
