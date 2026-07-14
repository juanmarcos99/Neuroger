package software.cneuro.neuroger.content;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 11/9/2015.
 */
public class FileHelper {
    private static final String TAG = "FileHelper";
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    private static final String DIRECTORY_BASE_PATH = SDCARD_PATH + File.separator + "NeuroGerPesquisa";
    private static final String DIRECTORY_HELP_PATH = DIRECTORY_BASE_PATH + File.separator + "help";
    private static final String DIRECTORY_EXPORT_PATH = DIRECTORY_BASE_PATH + File.separator + "data";
    private static final String DIRECTORY_EXPORT_EXCEL_PATH = DIRECTORY_EXPORT_PATH + File.separator + "excel";

    private static final String FILE_EXTENSION = ".json";

    private static final String FILE_HELP_MARCH_NAME = "Help_March.pdf";
    private static final String FILE_HELP_STATIC_BALANCE_NAME = "Help_Static_Balance.pdf";
    private static final String FILE_HELP_SQUAD_NAME = "Help_Squad.pdf";
    private static final String FILE_HELP_GRIP_STRENGTH_NAME = "Help_Grip_Strength.pdf";

    private static final String FILE_HELP_MARCH_PATH = DIRECTORY_HELP_PATH + File.separator + FILE_HELP_MARCH_NAME;
    private static final String FILE_HELP_STATIC_BALANCE_PATH = DIRECTORY_HELP_PATH + File.separator + FILE_HELP_STATIC_BALANCE_NAME;
    private static final String FILE_HELP_SQUAD_PATH = DIRECTORY_HELP_PATH + File.separator + FILE_HELP_SQUAD_NAME;
    private static final String FILE_HELP_GRIP_STRENGTH_PATH = DIRECTORY_HELP_PATH + File.separator + FILE_HELP_GRIP_STRENGTH_NAME;

    public static String createExcelFile(Workbook ourWorkbook) {
        if (isExternalStorageReadable() && createDirectory(DIRECTORY_EXPORT_EXCEL_PATH)) {
            File excelFile = new File(DIRECTORY_EXPORT_EXCEL_PATH, "NeuroGerData.xls");
            try {
                FileOutputStream fileOut = new FileOutputStream(excelFile);
                ourWorkbook.write(fileOut);
                fileOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return excelFile.getAbsolutePath();
        }
        return null;
    }

    public static String saveJSONContent(String jsonString) {
        if (isExternalStorageReadable() && createDirectory(DIRECTORY_EXPORT_PATH)) {
            /*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String outputFileName = timeStamp + ".txt";*/

            String uuid = java.util.UUID.randomUUID().toString();
            String outputFileName = uuid + FILE_EXTENSION;

            File file = new File(DIRECTORY_EXPORT_PATH, outputFileName);
            if (file.exists()) file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /*FileWriter out = null;
            try {
                out = new FileWriter(file);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                out.write(jsonString);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                out.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }*/

            return file.getAbsolutePath();
        }

        return "";
    }

    /**
     * Copies the help file from the assets folder in to the sdcard if it not exists.
     *
     * @param context The application's environment.
     * @param helpID  The id of the help that corresponds to the file which we want to view.
     * @return The file corresponding to the helpID.
     */
    public static File getHelpFile(Context context, int helpID) {
        if (isExternalStorageReadable() && createDirectory(DIRECTORY_HELP_PATH)) {
            File helpFile = new File(getHelpFilePath(helpID));
            if (!helpFile.exists()) {
                copyAssets(context, helpID);
            }
            return new File(getHelpFilePath(helpID));
        }
        return null;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean createDirectory(String path) {
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
                return false;
            }
        }
        return true;
    }

    //method to write the PDFs file to sd card
    private static void copyAssets(Context context, int helpID) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        for (int i = 0; i < files.length; i++) {
            String fStr = files[i];
            if (fStr.equals(getHelpFileName(helpID))) {
                InputStream in;
                OutputStream out;
                try {
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream(getHelpFilePath(helpID));
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    break;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static String getHelpFileName(int helpID) {
        if (helpID == Constant.HELP_MARCH) {
            return FILE_HELP_MARCH_NAME;
        } else if (helpID == Constant.HELP_STATIC_BALANCE) {
            return FILE_HELP_STATIC_BALANCE_NAME;
        } else if (helpID == Constant.HELP_SQUAD) {
            return FILE_HELP_SQUAD_NAME;
        } else if (helpID == Constant.HELP_GRIP_STRENGTH) {
            return FILE_HELP_GRIP_STRENGTH_NAME;
        }
        return "";
    }

    public static String getHelpFilePath(int helpID) {
        if (helpID == Constant.HELP_MARCH) {
            return FILE_HELP_MARCH_PATH;
        } else if (helpID == Constant.HELP_STATIC_BALANCE) {
            return FILE_HELP_STATIC_BALANCE_PATH;
        } else if (helpID == Constant.HELP_SQUAD) {
            return FILE_HELP_SQUAD_PATH;
        } else if (helpID == Constant.HELP_GRIP_STRENGTH) {
            return FILE_HELP_GRIP_STRENGTH_PATH;
        }
        return "";
    }
}
