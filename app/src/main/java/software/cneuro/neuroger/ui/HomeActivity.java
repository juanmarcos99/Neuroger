package software.cneuro.neuroger.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.IntentHelper;
import software.cneuro.neuroger.service.ExcelExportIntentService;
import software.cneuro.neuroger.service.JsonExportResponseReceiver;
import software.cneuro.neuroger.ui.about.AboutActivity;
import software.cneuro.neuroger.ui.input.subject.SubjectAddEditActivity;
import software.cneuro.neuroger.ui.search.SearchListFragment;
import software.cneuro.neuroger.ui.search.SearchSpecificationFragment;

public class HomeActivity extends AppCompatActivity implements
        SearchSpecificationFragment.OnSearchFragmentListener,
        SearchListFragment.OnSubjectListFragmentListener,
        View.OnClickListener {
    private SearchListFragment mListFragment;
    private SearchSpecificationFragment mSearchSpecificationFragment;

    private FloatingActionButton mFabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(
                JsonExportResponseReceiver.BROADCAST_ACTION);
        // Instantiates a new DownloadStateReceiver
        JsonExportResponseReceiver mExportStateReceiver =
                new JsonExportResponseReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mExportStateReceiver,
                mStatusIntentFilter);

        mFabAdd = findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(this);
        enableAddNewPatientBtn(false);

        if (savedInstanceState == null) {
            mSearchSpecificationFragment = new SearchSpecificationFragment();
            Bundle arguments = new Bundle();

            mSearchSpecificationFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_container, mSearchSpecificationFragment).commit();

            mListFragment = new SearchListFragment();
            arguments = new Bundle();

            mListFragment.setArguments(arguments);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    if (isAllowToUseApp())
                        checkForWritePermission();
                    else
                        showPermissionDenied(R.string.toast_dont_have_permission_to_use_app);
                } else {
                    IntentHelper.requestPermissionReadPhoneState(this, IntentHelper.REQUEST_READ_PHONE_STATE);
                }
            } else if (isAllowToUseApp()) {
                setupListFragment();
            } else {
                showPermissionDenied(R.string.toast_dont_have_permission_to_use_app);
            }
        }

        if (mSearchSpecificationFragment == null) {
            mSearchSpecificationFragment = (SearchSpecificationFragment) getSupportFragmentManager().findFragmentById(R.id.search_container);
        }

        if (mListFragment == null) {
            mListFragment = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.list_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_export) {
//            JsonExportIntentService.startActionExportDB(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showPermissionDenied(R.string.toast_permission_storage);
            } else
                ExcelExportIntentService.startActionExportDB(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubjectListItemClick(long id, String name, String lastName, String phone, int age, String birth, int levelOfSchooling, int gender, int compensated) {
        Intent intent = new Intent(this, SubjectGeneralEvaluationActivity.class);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_ID, id);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_NAME, name);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_LAST_NAME, lastName);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_PHONE, phone);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_AGE, age);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_BIRTH, birth);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_GENDER, gender);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_SUBJECT_LEVEL_OF_SCHOOLING, levelOfSchooling);
        intent.putExtra(SubjectGeneralEvaluationActivity.ARG_COMPENSATED, compensated);
        startActivity(intent);
    }

    @Override
    public void onSearchByName(String name) {
        mListFragment.updateContent(name);
    }

    @Override
    public void onSearchByGender(int isFemale, boolean isChecked) {
        mListFragment.updateContent(isFemale, isChecked);
    }

    @Override
    public void onSearchByGroupAge(int from, int to, boolean isChecked) {
        mListFragment.updateContent(from, to, isChecked);
    }

    /**
     * Add new subject.
     *
     * @param v the FAB.
     */
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SubjectAddEditActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (!mListFragment.actionModeActivated()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Buena práctica incluir el super

        // Verificamos si el usuario concedió el permiso (grantResults[0] == PERMISSION_GRANTED)
        boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        switch (requestCode) {
            case IntentHelper.REQUEST_READ_PHONE_STATE:
                if (isGranted) {
                    // El permiso de teléfono es el primero en la cadena
                    if (isAllowToUseApp()) {
                        // Si el dispositivo está permitido, pasamos a verificar el de almacenamiento
                        checkForWritePermission();
                    } else {
                        showPermissionDenied(R.string.toast_dont_have_permission_to_use_app);
                    }
                } else {
                    // Si deniega el teléfono, no podemos validar el IMEI/ID, mostramos error
                    showPermissionDenied(R.string.toast_permission_read_phone_state);
                }
                break;

            case IntentHelper.REQUEST_WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    // Si finalmente concede almacenamiento, cargamos la lista
                    setupListFragment();
                } else {
                    // Si deniega almacenamiento, mostramos error específico
                    showPermissionDenied(R.string.toast_permission_storage);
                }
                break;
        }
    }

    private void checkForWritePermission() {
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            setupListFragment();
        } else {
            IntentHelper.requestPermissionWriteExternalStorage(this, IntentHelper.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void setupListFragment() {
        findViewById(R.id.error_device_not_allowed).setVisibility(View.GONE);
        findViewById(R.id.search_container).setVisibility(View.VISIBLE);
        findViewById(R.id.list_container).setVisibility(View.VISIBLE);
        findViewById(R.id.fab_add).setVisibility(View.VISIBLE);

        enableAddNewPatientBtn(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_container, mListFragment).commit();
    }

    /**
     * Get the IMEI or use Secure.ANDROID_ID as an alternative, when the device doesn't have phone capabilities
     *
     * @return IMEI
     */
    private boolean isAllowToUseApp() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return false;
//        }

        String deviceUniqueIdentifier = null;
        if (null != telephonyManager) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
//        Toast.makeText(this, "Device id:" + deviceUniqueIdentifier, Toast.LENGTH_LONG).show();

        boolean allowed = Arrays.asList(Constant.devicesIds).contains(deviceUniqueIdentifier);
        if (!allowed) {
            findViewById(R.id.error_device_not_allowed).setVisibility(View.VISIBLE);
//            ((TextView)findViewById(R.id.error_device_not_allowed)).setText(deviceUniqueIdentifier);
        }
//        return allowed;
        return true;
    }

    private void showPermissionDenied(@StringRes int messageResourceId) {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(messageResourceId))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void enableAddNewPatientBtn(boolean enable) {
        assert mFabAdd != null;
        mFabAdd.setEnabled(enable);
    }
}
