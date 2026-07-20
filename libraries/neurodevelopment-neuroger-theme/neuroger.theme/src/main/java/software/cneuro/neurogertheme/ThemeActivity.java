package software.cneuro.neurogertheme;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;


public class ThemeActivity extends AppCompatActivity implements DummyListFragment.OnFragmentInteractionListener {
    private DummyListFragment mListFragment;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.fragment_subject_input);

        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(mToolbar);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle("Listado Pacientes");
        mCollapsingToolbar.setContentScrimColor(getResources().getColor(R.color.primary));

        if (savedInstanceState == null) {
            mListFragment = new DummyListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_container, mListFragment).commit();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner_subject_province);
        spinner.setAdapter(new DummySpinnerAdapter(this,
                R.layout.spinner_item_selected, R.layout.spinner_item_dropdown,
                R.array.array_for_test));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(View icon, int position, String text, int imageResourceID) {

    }
}

