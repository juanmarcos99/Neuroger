package software.cneuro.neuroger.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.util.List;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.FileHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

public class ExcelExportIntentService extends IntentService {
    // Action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_EXPORT_DB = "software.cneuro.neuroger.action.EXPORT_DB";
    public static final String ACTION_COMPLETED = "software.cneuro.neuroger.extra.ACTION_COMPLETED";
    public static final String ACTION_NO_COMPLETED = "software.cneuro.neuroger.extra.ACTION_NO_COMPLETED";
    private static final String TAG = "ExcelExport";
    private static ContentResolver mContentResolver;
    private String mFilePath;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ExcelExportIntentService() {
        super("ExcelExportIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionExportDB(Context context) {
        mContentResolver = context.getContentResolver();

        Intent intent = new Intent(context, ExcelExportIntentService.class);
        intent.setAction(ACTION_EXPORT_DB);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EXPORT_DB.equals(action)) {
                String result = handleActionExportDB();

                /*
                 * Creates a new Intent containing a Uri object
                 * BROADCAST_ACTION is a custom Intent action
                 */
                Intent localIntent =
                        new Intent(JsonExportResponseReceiver.BROADCAST_ACTION)
                                // Puts the status into the Intent
                                .putExtra(JsonExportResponseReceiver.EXTENDED_DATA_STATUS,
                                        result);
                // Broadcasts the Intent to receivers in this app.
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

                if (result.equals(ACTION_COMPLETED)) {
                    MediaScannerConnection.scanFile(getApplicationContext(),
                            new String[]{mFilePath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

                }
            }
        }
    }

    private String handleActionExportDB() {
        CursorHelper cursorHelper = new CursorHelper(mContentResolver);

        Cursor cursorSubjects = cursorHelper.getCursorPatients();
        Cursor cursorAntecedents = null,
                cursorPfeiffer = null,
                cursorCdr = null,
                cursorGds = null,
                cursorHhies = null,
                cursorKatz = null,
                cursorLawton = null,
                cursorSubjective = null,
                cursorFrail = null,
                cursorMinimental = null,
                cursorPhysical = null,
                cursorPas = null,
                cursorPaffective = null,
                cursorClassification = null,
                cursorCognitive = null;
        try {
            if (cursorSubjects != null) {
                HSSFWorkbook workbook = new HSSFWorkbook();
                String safeName = WorkbookUtil.createSafeSheetName(getApplicationContext().getString(R.string.sheet_name_patients));
                HSSFSheet dataPatientSheet = workbook.createSheet(safeName);

                HSSFRow rowHead = dataPatientSheet.createRow(0);
                int columnCount = 0;
                List<String> patientsValues = JsonNames.getPatientsValues(getApplicationContext());
                for (String item : patientsValues) {
                    rowHead.createCell(columnCount++).setCellValue(item);
                }

                int rowCount = 1;
                while (cursorSubjects.moveToNext()) {
                    int id = cursorSubjects.getInt(cursorSubjects
                            .getColumnIndex(Constant.COL_PATIENT_ID));
                    String name = cursorSubjects.getString(
                            cursorSubjects.getColumnIndex(Constant.COL_PATIENT_NAME
                            ));
                    String lastName = cursorSubjects.getString(
                            cursorSubjects.getColumnIndex(Constant.COL_PATIENT_LASTNAME
                            ));
                    int genderId = cursorSubjects.getInt(
                            cursorSubjects.getColumnIndex(Constant.COL_PATIENT_SEX
                            ));
                    boolean isFemale = genderId == software.cneuro.neuroger.constant.Constant.SUBJECT_FEMALE_ID;
                    String birth = cursorSubjects.getString(
                            cursorSubjects.getColumnIndex(Constant.COL_PATIENT_BIRTHDATE
                            ));
                    String yearsStudy = cursorSubjects.getString(
                            cursorSubjects.getColumnIndex(Constant.COL_PATIENT_YEARS_STUDIES
                            ));

                    SheetHelper.createPatientRow(getApplicationContext(), cursorSubjects, name, lastName, dataPatientSheet, rowCount++);

                    // Classification module
                    cursorAntecedents = cursorHelper.getCursorAntecedents(id);
                    if (cursorAntecedents != null && cursorAntecedents.moveToFirst()) {
                        SheetHelper.createComorbidityMedicationData(getApplicationContext(), cursorAntecedents, name, lastName, workbook);
                    }

                    cursorSubjective = cursorHelper.getCursorSubjective(id);
                    if (cursorSubjective != null && cursorSubjective.moveToFirst()) {
                        SheetHelper.createSubjectiveData(getApplicationContext(), cursorSubjective, name, lastName, workbook);
                    }

                    cursorFrail = cursorHelper.getCursorFrail(id);
                    if (cursorFrail != null && cursorFrail.moveToFirst()) {
                        SheetHelper.createFrailData(getApplicationContext(), cursorFrail, name, lastName, workbook);
                    }

                    cursorPas = cursorHelper.getCursorPsychofamily(id);
                    if (cursorPas != null && cursorPas.moveToFirst()) {
                        SheetHelper.createPyschofamilyData(getApplicationContext(), cursorPas, name, lastName, workbook);
                    }

                    cursorPaffective = cursorHelper.getCursorPsychoaffective(id);
                    if (cursorPaffective != null && cursorPaffective.moveToFirst()) {
                        SheetHelper.createPyschoaffectiveData(getApplicationContext(), cursorPaffective, name, lastName, workbook);
                    }

                    cursorKatz = cursorHelper.getCursorKatz(id);
                    if (cursorKatz != null && cursorKatz.moveToFirst()) {
                        SheetHelper.createKatzData(getApplicationContext(), cursorKatz, name, lastName, workbook);
                    }

                    cursorLawton = cursorHelper.getCursorLawton(id);
                    if (cursorLawton != null && cursorLawton.moveToFirst()) {
                        SheetHelper.createLawtonData(getApplicationContext(), cursorLawton, name, lastName, isFemale, workbook);
                    }

                    // Classification
                    cursorClassification = cursorHelper.getCursorClassification(id);
                    if (cursorClassification != null && cursorClassification.moveToFirst()) {
                        SheetHelper.createClassificationData(getApplicationContext(), cursorClassification, name, lastName, workbook);
                    }

                    // Physical module
                    cursorPhysical = cursorHelper.getCursorPhysical(id);
                    if (cursorPhysical != null && cursorPhysical.moveToFirst()) {
                        SheetHelper.createPhysicalData(getApplicationContext(), cursorPhysical, name, lastName, isFemale, workbook);
                    }

                    // Cognitive module
                    cursorMinimental = cursorHelper.getCursorMinimental(id);
                    if (cursorMinimental != null && cursorMinimental.moveToFirst()) {
                        SheetHelper.createMinimentalData(
                                getApplicationContext(),
                                cursorMinimental,
                                name,
                                lastName,
                                CalendarHelper.getAge(birth),
                                Integer.parseInt(yearsStudy),
                                workbook);
                    }

                    cursorGds = cursorHelper.getCursorGds(id);
                    if (cursorGds != null && cursorGds.moveToFirst()) {
                        SheetHelper.createGDSData(getApplicationContext(), cursorGds, name, lastName, workbook);
                    }

                    cursorPfeiffer = cursorHelper.getCursorPfeiffer(id);
                    if (cursorPfeiffer != null && cursorPfeiffer.moveToFirst()) {
                        SheetHelper.createPfeifferData(getApplicationContext(), cursorPfeiffer, name, lastName, workbook);
                    }

                    cursorCdr = cursorHelper.getCursorCdr(id);
                    if (cursorCdr != null && cursorCdr.moveToFirst()) {
                        SheetHelper.createCDRData(getApplicationContext(), cursorCdr, name, lastName, workbook);
                    }

                    cursorHhies = cursorHelper.getCursorHhies(id);
                    if (cursorHhies != null && cursorHhies.moveToFirst()) {
                        SheetHelper.createHHIESData(getApplicationContext(), cursorHhies, name, lastName, workbook);
                    }

                    // Cognitive
                    cursorCognitive = cursorHelper.getCursorCognitive(id);
                    if (cursorCognitive != null && cursorCognitive.moveToFirst()) {
                        SheetHelper.createCognitiveData(getApplicationContext(), cursorCognitive, name, lastName, workbook);
                    }
                }

                mFilePath = FileHelper.createExcelFile(workbook);

                return ACTION_COMPLETED;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cursorHelper.closeCursor(cursorSubjects,
                    cursorAntecedents,
                    cursorPfeiffer,
                    cursorCdr,
                    cursorGds,
                    cursorHhies,
                    cursorKatz,
                    cursorLawton,
                    cursorSubjective,
                    cursorFrail,
                    cursorMinimental,
                    cursorPhysical,
                    cursorPas,
                    cursorPaffective,
                    cursorClassification,
                    cursorCognitive);
        }
        return ACTION_NO_COMPLETED;
    }
}
