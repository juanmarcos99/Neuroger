package com.aess.licenciaapp;

import static com.aess.licenciaapp.LicenseConstants.*;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import software.cneuro.neuroger.R;

public class LicenseActivity extends AppCompatActivity {

    private static final int PICK_NEU_FILE = 1001;

    // UI Components ajustados al nuevo diseño
    private ConstraintLayout mainContainer;
    private TextInputEditText etUsername, etPassword;
    private EditText etLicenseCode;
    private TextView tvTitle, tvInfoBox;
    private Button btnValidate, btnSaveNeu, btnOpenApp, btnLoad;
    private MaterialCardView cardStatus;

    private LicenseManager licenseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        licenseManager = new LicenseManager(this);
        licenseManager.collectDeviceInfo();

        initializeUI();
        loadUserCredentialsToUI();

        // Ejecutamos la validación inicial
        internalValidationAndUI();
    }

    private void initializeUI() {
        // Vinculamos con el nuevo contenedor principal del diseño sin scroll
        mainContainer = findViewById(R.id.main_container);
        tvTitle = findViewById(R.id.tv_title);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etLicenseCode = findViewById(R.id.et_license_code);
        tvInfoBox = findViewById(R.id.tv_info_box);

        btnValidate = findViewById(R.id.btn_validate);
        btnSaveNeu = findViewById(R.id.btn_save_neu);
        btnOpenApp = findViewById(R.id.btn_open_app);
        btnLoad = findViewById(R.id.btn_load_neu);
        cardStatus = findViewById(R.id.card_status);

        btnValidate.setOnClickListener(v -> handleUnlock());
        btnOpenApp.setOnClickListener(v -> startMainApp());
        btnSaveNeu.setOnClickListener(v -> shareDeviceInfo());

        btnLoad.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccione archivo .neu"), PICK_NEU_FILE);
        });

        tvInfoBox.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Fingerprint", licenseManager.getDeviceFingerprint()));
            showShortToast("Copiado al portapapeles");
        });
    }

    private void internalValidationAndUI() {
        boolean isUnlocked = licenseManager.getPrefs().getBoolean(K_IS_UNLOCKED, false);
        String statusMessage = "";

        if (isUnlocked) {
            updateDaysUsed();
            String status = licenseManager.checkLicenseValidity();
            if (!"VALID".equals(status)) {
                statusMessage = status;
                licenseManager.getPrefs().edit().putBoolean(K_IS_UNLOCKED, false).apply();
                isUnlocked = false;
            }
        }
        updateUIState(isUnlocked, statusMessage);
    }

    private void updateDaysUsed() {
        long lastCheck = licenseManager.getPrefs().getLong("K_LAST_TIME_CHECK", 0);
        long now = System.currentTimeMillis();

        if (lastCheck == 0) {
            licenseManager.getPrefs().edit().putLong("K_LAST_TIME_CHECK", now).apply();
            return;
        }

        long diff = now - lastCheck;
        int daysPassed = (int) (diff / (24 * 60 * 60 * 1000));

        if (daysPassed > 0) {
            int totalUsed = licenseManager.getPrefs().getInt(K_LICENSE_TIME_USED, 0);
            licenseManager.getPrefs().edit()
                    .putInt(K_LICENSE_TIME_USED, totalUsed + daysPassed)
                    .putLong("K_LAST_TIME_CHECK", now)
                    .apply();
        }
    }

    private void handleUnlock() {
        String username = getUsername();
        String userPwd = getUserPassword();
        String code = getLicenseCodeFromInput();

        if (username.isEmpty() || userPwd.isEmpty() || code.isEmpty()) {
            showShortToast("Complete todos los campos.");
            return;
        }

        String fingerprint = licenseManager.getDeviceFingerprint();
        LicenseManager.LicenseDecodeResult decodeResult = licenseManager.decodeLicense(code, username, userPwd, fingerprint);

        if (!decodeResult.valid) {
            if (!decodeResult.validDev) {
                showError("Esta licencia no es para este dispositivo.");
            } else if (!decodeResult.validUsr) {
                showError("Usuario o contraseña incorrectos para esta licencia.");
            } else {
                showError("El código de licencia es inválido o ha expirado.");
            }
            return;
        }

        String licenciaGuardada = licenseManager.getPrefs().getString("K_CURRENT_ACTIVATED_CODE", "");

        if (!licenciaGuardada.isEmpty() && !licenciaGuardada.equals(code)) {
            licenseManager.getPrefs().edit()
                    .putInt(K_LICENSE_CURRENT_USES, 0)
                    .putInt(K_LICENSE_TIME_USED, 0)
                    .apply();
        }

        licenseManager.saveLicenseInfo(decodeResult, username, userPwd, code);
        showShortToast("¡Licencia activada con éxito!");
        internalValidationAndUI();
    }

    private void updateUIState(boolean isUnlocked, String errorMsg) {
        btnOpenApp.setEnabled(isUnlocked);
        btnValidate.setEnabled(!isUnlocked);
        btnValidate.setText(isUnlocked ? "LICENCIA ACTIVA" : "VALIDAR LICENCIA");

        etLicenseCode.setEnabled(!isUnlocked);
        etUsername.setEnabled(!isUnlocked);
        etPassword.setEnabled(!isUnlocked);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnOpenApp.setBackgroundTintList(ColorStateList.valueOf(isUnlocked ? Color.parseColor("#059669") : Color.LTGRAY));
        }

        if (!isUnlocked) {
            if (!errorMsg.isEmpty()) {
                tvInfoBox.setText(errorMsg);
                tvInfoBox.setTextColor(Color.parseColor("#EF4444"));
            } else {
                tvInfoBox.setText(licenseManager.getDeviceFingerprint());
                tvInfoBox.setTextColor(Color.parseColor("#000000"));
            }
        } else {
            int maxU = licenseManager.getPrefs().getInt(K_LICENSE_MAX_USES, -1);
            int curU = licenseManager.getPrefs().getInt(K_LICENSE_CURRENT_USES, 0);
            int maxT = licenseManager.getPrefs().getInt(K_LICENSE_MAX_TIME, -1);
            int curT = licenseManager.getPrefs().getInt(K_LICENSE_TIME_USED, 0);

            String text = String.format(Locale.getDefault(),
                    "LICENCIA ACTIVA\nUsos: %d / %s\nDías: %d / %s",
                    curU, (maxU == -1 || maxU == 100) ? "∞" : maxU,
                    curT, (maxT == -1 || maxT == 365) ? "∞" : maxT);

            tvInfoBox.setText(text);
            tvInfoBox.setTextColor(Color.parseColor("#1E293B"));
        }
    }

    private void startMainApp() {
        int currentUses = licenseManager.getPrefs().getInt(K_LICENSE_CURRENT_USES, 0);
        licenseManager.getPrefs().edit().putInt(K_LICENSE_CURRENT_USES, currentUses + 1).apply();

        // 🔴 CAMBIAR ESTA LÍNEA en LicenseActivity.java 🔴
        startActivity(new Intent(this, software.cneuro.neuroger.ui.SplashActivity.class));
        finish();
    }

    // --- MÉTODOS DE ARCHIVOS .NEU (LOGICA SCRAMBLE/UNSCRAMBLE SIN CAMBIOS) ---

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_NEU_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String code = readUnlockCodeFromNeu(uri);
            if (code != null) {
                etLicenseCode.setText(code);
                handleUnlock();
            } else {
                showError("Archivo .neu corrupto.");
            }
        }
    }

    private String readUnlockCodeFromNeu(Uri uri) {
        try (InputStream is = getContentResolver().openInputStream(uri);
             DataInputStream dis = new DataInputStream(is)) {

            int len = dis.readInt();
            if (len <= 0) return null;

            dis.readUnsignedShort();
            dis.skipBytes(3);

            int half = len / 2;
            byte[] evenPart = new byte[half];
            dis.readFully(evenPart);

            dis.readUnsignedShort();

            byte[] oddPart = new byte[len - half];
            dis.readFully(oddPart);

            if (dis.available() >= 5) {
                dis.readUnsignedShort();
                dis.skipBytes(3);
            }

            byte[] original = new byte[len];

            int evenIdx = 0;
            for (int i = 1; i < len; i += 2) {
                if (evenIdx < evenPart.length) {
                    original[i] = evenPart[evenIdx++];
                }
            }

            int oddIdx = 0;
            for (int i = 0; i < len; i += 2) {
                if (oddIdx < oddPart.length) {
                    original[i] = oddPart[oddIdx++];
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return new String(original, StandardCharsets.UTF_8);
            } else {
                return new String(original, "UTF-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void shareDeviceInfo() {
        try {
            String fp = licenseManager.getDeviceFingerprint();
            if (fp == null || fp.isEmpty()) return;

            byte[] bytes;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                bytes = fp.getBytes(StandardCharsets.UTF_8);
            } else {
                bytes = fp.getBytes("UTF-8");
            }

            int len = bytes.length;

            byte[] scrambled = new byte[len];
            int sIdx = 0;

            for (int i = 1; i < len; i += 2) {
                scrambled[sIdx++] = bytes[i];
            }
            int splitPoint = sIdx;

            for (int i = 0; i < len; i += 2) {
                scrambled[sIdx++] = bytes[i];
            }

            File file = new File(getCacheDir(), "huella.neu");

            try (FileOutputStream fos = new FileOutputStream(file);
                 DataOutputStream dos = new DataOutputStream(fos)) {

                Random rnd = new Random();
                int[] sigPool = {0xDEAD, 0xCAFE, 0xBEEF, 0xFEED, 0xFADE};

                dos.writeInt(len);

                dos.writeShort(sigPool[rnd.nextInt(sigPool.length)]);
                byte[] p1 = new byte[3];
                rnd.nextBytes(p1);
                dos.write(p1);

                dos.write(scrambled, 0, splitPoint);

                dos.writeShort(sigPool[rnd.nextInt(sigPool.length)]);

                dos.write(scrambled, splitPoint, len - splitPoint);

                dos.writeShort(sigPool[rnd.nextInt(sigPool.length)]);
                byte[] p2 = new byte[3];
                rnd.nextBytes(p2);
                dos.write(p2);

                dos.flush();
            }

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Enviar huella de dispositivo"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al exportar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserCredentialsToUI() {
        etUsername.setText(licenseManager.getPrefs().getString(K_USERNAME, ""));
        etPassword.setText(licenseManager.getPrefs().getString(K_USERPWD, ""));
    }

    private void showError(String msg) {
        tvInfoBox.setText(msg);
        tvInfoBox.setTextColor(Color.parseColor("#EF4444"));
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getLicenseCodeFromInput() { return etLicenseCode.getText().toString().trim(); }
    private String getUsername() { return Objects.requireNonNull(etUsername.getText()).toString().trim(); }
    private String getUserPassword() { return Objects.requireNonNull(etPassword.getText()).toString().trim(); }
}