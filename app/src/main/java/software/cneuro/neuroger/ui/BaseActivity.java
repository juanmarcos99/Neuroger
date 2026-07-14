package software.cneuro.neuroger.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import software.cneuro.neuroger.R;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String ARG_TITLE = "title";
    public static final String ARG_SUBJECT_FULL_NAME = "name";

    protected Fragment mFragment;

    protected abstract Fragment getFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_header);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        assert getIntent() != null && getIntent().getExtras() != null;
        Bundle intent = getIntent().getExtras();
        if (!TextUtils.isEmpty(intent.getString(ARG_TITLE)))
            getSupportActionBar().setTitle(intent.getString(ARG_TITLE));

        updateSubjectFullName(getIntent().getExtras().getString(ARG_SUBJECT_FULL_NAME));

        if (savedInstanceState == null) {
            mFragment = getFragment();
            mFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment).commit();
        }

        if (mFragment == null) {
            mFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        }
    }

    public void updateSubjectFullName(String fullName) {
        ((TextView) findViewById(R.id.subject_detail_name)).setText(fullName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
