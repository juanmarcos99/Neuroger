package software.cneuro.neuroger.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import software.cneuro.neuroger.R;


public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    private static String TAG;
    /**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;

    private boolean mLoadHomeActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getName();

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splash);

        // The thread to wait for splash screen events
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(SPLASH_DISPLAY_LENGTH);
                    }
                } catch (InterruptedException ex) {
                }
                finish();

                if (mLoadHomeActivity) {
                    Intent intent = new Intent(SplashActivity.this,
                            HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                }
            }
        };
        mSplashThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed");

        mLoadHomeActivity = false;

    }
}
