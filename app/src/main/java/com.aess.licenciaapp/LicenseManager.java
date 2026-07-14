package com.aess.licenciaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.provider.Settings;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// Importante: Importar tus constantes (Salt, Diccionarios, Keys)
import static com.aess.licenciaapp.LicenseConstants.*;

public class LicenseManager {
    private final SharedPreferences prefs;
    private final Context context;
    private String deviceFingerprint;

    public LicenseManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Genera el fingerprint del dispositivo basado en hardware y fecha de instalación.
     */
    public void collectDeviceInfo() {
        long installDate = prefs.getLong(K_INSTALL_DATE, 0L);
        if (installDate == 0L) {
            installDate = getInstallTime();
            prefs.edit().putLong(K_INSTALL_DATE, installDate).apply();
        }
        deviceFingerprint = generateFingerprint(installDate);
    }

    private long getInstallTime() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.firstInstallTime;
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    @SuppressLint("HardwareIds")
    private String generateFingerprint(long date) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        // Formato de fecha idéntico al que esperaría el generador para el hardware
        String raw = androidId + Build.MODEL + Build.MANUFACTURER + HashUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        return HashUtils.md5(raw).toLowerCase();
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    /**
     * DECODIFICADOR PRINCIPAL
     * Traduce la lógica de MATLAB/Python a Java/Android.
     */
    private void parseLicenseBlocks(String block, LicenseDecodeResult res) {
        try {
            // --- TIEMPO (T) ---
            // Python: posinf_t = int(licstring[-1])
            int posT = Character.getNumericValue(block.charAt(block.length() - 1));
            int idxT = posT - 1;

            // Python: l_t = int(licstring[idx_t])
            int lenT = Character.getNumericValue(block.charAt(idxT));

            // Python: t_segment = licstring[idx_t+1 : idx_t+1+l_t]
            String tSegment = block.substring(idxT + 1, idxT + 1 + lenT);
            int dlim = Integer.parseInt(tSegment);

            // Mapeo con el diccionario (Python: actual_val_t = DICT_LICENSE_D[dlim - 1])
            res.maxTime = DICT_D[dlim - 1];
            res.isResetTime = false; // El script de Python no maneja esto

            // Python: licstring_reduced = licstring[:indrem_t_start] + licstring[indrem_t_end:]
            String reducedBlock = block.substring(0, idxT) + block.substring(idxT + 1 + lenT);

            // --- USOS (U) ---
            // Python: posinf_u = int(licstring_reduced[-2])
            // Nota: En Python es -2 porque el último char ahora es el "posT" que se arrastró al final
            int posU = Character.getNumericValue(reducedBlock.charAt(reducedBlock.length() - 2));
            int idxU = posU - 1;

            int lenU = Character.getNumericValue(reducedBlock.charAt(idxU));
            String uSegment = reducedBlock.substring(idxU + 1, idxU + 1 + lenU);
            int ulim = Integer.parseInt(uSegment);

            res.maxUses = DICT_U[ulim - 1];
            res.isResetUsage = false;

            // Validez del bloque
            res.validBlock = (res.maxTime != 0 && res.maxUses != 0);

        } catch (Exception e) {
            res.validBlock = false;
        }
    }

    /**
     * Reemplaza tu decodeLicense para que use el rango de 31 días de Python
     */
    public LicenseDecodeResult decodeLicense(String code, String user, String pwd, String fingerprint) {
        LicenseDecodeResult res = new LicenseDecodeResult();
        try {
            String[] parts = extractSortedParts(code);
            if (parts == null || parts.length < 3) return res;

            // 1. Validar Device
            String devHash = HashUtils.md5(fingerprint + SECRET_SALT).toUpperCase();
            res.validDev = devHash.contains(parts[0].toUpperCase());

            // 2. Validar Usuario (Brute Force 31 días como en Python)
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

            // Intento 1: Sin fecha (Python: md5(pwd + salt + "" + soft + user))
            String uHashNoDate = HashUtils.md5(pwd + SECRET_SALT + "" + SOFTWARE_NAME + user).toUpperCase();
            if (parts[1].toUpperCase().equals("") || uHashNoDate.contains(parts[1].toUpperCase())) {
                res.validUsr = true;
            } else {
                // Intento 2: Bucle de 31 días
                Calendar cal = Calendar.getInstance();
                for (int i = 0; i < 31; i++) {
                    String dStr = sdf.format(cal.getTime()); // dd-MMM-yyyy
                    String uHash = HashUtils.md5(pwd + SECRET_SALT + dStr + SOFTWARE_NAME + user).toUpperCase();

                    if (uHash.contains(parts[1].toUpperCase())) {
                        res.validUsr = true;
                        break;
                    }
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }
            }

            // 3. Bloques de límites
            parseLicenseBlocks(parts[2], res);

            res.valid = res.validDev && res.validUsr && res.validBlock;

        } catch (Exception e) {
            res.valid = false;
        }
        return res;
    }




    /**
     * Extrae un valor basado en índices de diccionario, soportando lógica de Reset.
     */
    private BlockExtraction extractValueFromDict(String s, int start, int[] dict, int resetCode) {
        int lenOfIndex = Character.getNumericValue(s.charAt(start));
        int indexInDict = Integer.parseInt(s.substring(start + 1, start + 1 + lenOfIndex));

        int val = dict[indexInDict - 1]; // MATLAB es 1-based, Java 0-based
        int currentEnd = start + 1 + lenOfIndex;
        boolean resetFound = false;

        // Lógica de Reset: Si el valor es el código especial, el valor real sigue después
        if (val == resetCode) {
            resetFound = true;
            int nextLen = Character.getNumericValue(s.charAt(currentEnd));
            int nextIdx = Integer.parseInt(s.substring(currentEnd + 1, currentEnd + 1 + nextLen));
            val = dict[nextIdx - 1];
            currentEnd = currentEnd + 1 + nextLen;
        }
        return new BlockExtraction(val, currentEnd, resetFound);
    }




    private String[] extractSortedParts(String code) {
        StringBuilder regexBuilder = new StringBuilder("[");

        for (String s : SYMBOLS) regexBuilder.append(Pattern.quote(s));
        regexBuilder.append("]");

        String[] raw = code.split(regexBuilder.toString());
        List<Part> list = new ArrayList<>();
        for (String s : raw) {
            if (s != null && !s.trim().isEmpty()) list.add(new Part(s.trim()));
        }
        if (list.size() < 3) return null;

        Collections.sort(list);
        return new String[]{list.get(0).data, list.get(1).data, list.get(2).data};
    }




    /**
     * Guarda la licencia y resetea contadores si los flags de Reset están activos.
     */
    public void saveLicenseInfo(LicenseDecodeResult res, String user, String pwd) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(K_IS_UNLOCKED, res.valid);
        editor.putString(K_USERNAME, user);
        editor.putString(K_USERPWD, pwd);
        editor.putInt(K_LICENSE_MAX_USES, res.maxUses);
        editor.putInt(K_LICENSE_MAX_TIME, res.maxTime);

        if (res.isResetUsage) {
            editor.putInt(K_LICENSE_CURRENT_USES, 0);
        }
        if (res.isResetTime) {
            editor.putInt(K_LICENSE_TIME_USED, 0);
            editor.putLong("last_time_check", System.currentTimeMillis());
        }

        editor.apply();
    }

    public String checkLicenseValidity() {
        if (!prefs.getBoolean(K_IS_UNLOCKED, false)) return "SIN LICENCIA";

        int maxUses = prefs.getInt(K_LICENSE_MAX_USES, -1);
        int curUses = prefs.getInt(K_LICENSE_CURRENT_USES, 0);
        int maxTime = prefs.getInt(K_LICENSE_MAX_TIME, -1);
        int curTime = prefs.getInt(K_LICENSE_TIME_USED, 0);

        if (maxUses != -1 && maxUses != 100 && curUses >= maxUses) return "LÍMITE DE USOS ALCANZADO";
        if (maxTime != -1 && maxTime != 365 && curTime >= maxTime) return "LÍMITE DE TIEMPO ALCANZADO";

        return "VALID";
    }

    // --- CLASES DE APOYO ---

    public static class LicenseDecodeResult {
        public boolean valid = false, validDev = false, validUsr = false, validBlock = false;
        public int maxUses = -1, maxTime = -1;
        public boolean isResetUsage = false, isResetTime = false;
    }

    private static class BlockExtraction {
        int value, endIndex;
        boolean isReset;
        BlockExtraction(int v, int e, boolean r) {
            this.value = v; this.endIndex = e; this.isReset = r;
        }
    }

    private static class Part implements Comparable<Part> {
        int id; String data;
        Part(String s) {
            id = Character.getNumericValue(s.charAt(0));
            data = s.substring(1);
        }
        @Override public int compareTo(Part o) {
            return Integer.compare(this.id, o.id);
        }
    }
    /**
     * Devuelve el objeto SharedPreferences para lectura/escritura externa.
     * Requerido por LicenseActivity para obtener nombres de usuario, contraseñas, etc.
     */
    public SharedPreferences getPrefs() {
        return prefs;
    }

    /**
     * Permite cambiar el estado de desbloqueo manualmente (útil cuando expira la licencia).
     * @param status true para desbloquear, false para bloquear.
     */
    public void setUnlocked(boolean status) {
        prefs.edit().putBoolean(K_IS_UNLOCKED, status).apply();
    }

    /**
     * Guarda la información de la licencia tras una validación exitosa.
     * Maneja automáticamente el reseteo de contadores si el código lo indica.
     */
    public void saveLicenseInfo(LicenseDecodeResult res, String user, String pwd, String activatedCode) {
        SharedPreferences.Editor editor = prefs.edit();

        // 1. Datos de identidad
        editor.putBoolean(K_IS_UNLOCKED, res.valid);
        editor.putString(K_USERNAME, user);
        editor.putString(K_USERPWD, pwd);
        editor.putString("K_CURRENT_ACTIVATED_CODE", activatedCode);

        // 2. Límites extraídos
        editor.putInt(K_LICENSE_MAX_USES, res.maxUses);
        editor.putInt(K_LICENSE_MAX_TIME, res.maxTime);

        // 3. Lógica de Resets y Tiempo
        if (res.isResetUsage) {
            editor.putInt(K_LICENSE_CURRENT_USES, 0);
        }

        // Siempre que activamos una licencia (nueva o de reset),
        // actualizamos el puntero de tiempo para que empiece a contar desde "ahora".
        if (res.isResetTime || res.valid) {
            editor.putInt(K_LICENSE_TIME_USED, res.isResetTime ? 0 : prefs.getInt(K_LICENSE_TIME_USED, 0));
            editor.putLong("K_LAST_TIME_CHECK", System.currentTimeMillis());
        }

        editor.apply();
    }
}
