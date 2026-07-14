package software.cneuro.neuroger.service;

import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_CDR;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_GDS;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_HHIES;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_KATZ_INDEX;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_LAWTON_MODIFIED_SCALE;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_PFEIFFER;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_PSYCHOAFFECTIVE_SCALE;
import static software.cneuro.neuroger.constant.Constant.CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE;
import static software.cneuro.neuroger.constant.Constant.MATH_COUNTING_TEST;
import static software.cneuro.neuroger.constant.Constant.SUBJECT_NO_TAKES_MORE_THAN_5_MEDICAMENT_ID;
import static software.cneuro.neuroger.constant.Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_EVALUATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_DESIGN;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_LANGUAGE;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_ORDER_3;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_REMEMBERING;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CDRHelper;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.FileHelper;
import software.cneuro.neuroger.content.FrailHelper;
import software.cneuro.neuroger.content.GDSHelper;
import software.cneuro.neuroger.content.HHIESHelper;
import software.cneuro.neuroger.content.KatzHelper;
import software.cneuro.neuroger.content.LawtonHelper;
import software.cneuro.neuroger.content.MMSEHelper;
import software.cneuro.neuroger.content.PASHelper;
import software.cneuro.neuroger.content.PfeifferHelper;
import software.cneuro.neuroger.content.PsychoaffectiveHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.content.SubjectHelper;
import software.cneuro.neuroger.content.SubjectiveHelper;
import software.cneuro.neuroger.content.WarningSignHelper;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class JsonExportIntentService extends IntentService {
    private static final String TAG = "JsonExportIntentService";

    // Action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_EXPORT_DB = "software.cneuro.neuroger.action.EXPORT_DB";

    public static final String ACTION_COMPLETED = "software.cneuro.neuroger.extra.ACTION_COMPLETED";
    public static final String ACTION_NO_COMPLETED = "software.cneuro.neuroger.extra.ACTION_NO_COMPLETED";

    private static ContentResolver mContentResolver;
    private String mFilePath;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionExportDB(Context context) {
        mContentResolver = context.getContentResolver();

        Intent intent = new Intent(context, JsonExportIntentService.class);
        intent.setAction(ACTION_EXPORT_DB);
        context.startService(intent);
    }

    public JsonExportIntentService() {
        super("JsonExportIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

    /**
     * Handle action EXPORT_DB in the provided background thread with the provided
     * parameters.
     */
    private String handleActionExportDB() {
        Uri uri = Constant.URI_TABLE_PATIENT;
        Cursor cursorSubjects = mContentResolver.query(uri, null, null, null,
                null);

        List<JSONObject> subjectsList = new ArrayList<>();

        try {
            if (cursorSubjects != null) {
                while (cursorSubjects.moveToNext()) {
                    int id = cursorSubjects.getInt(cursorSubjects
                            .getColumnIndex(Constant.COL_PATIENT_ID));

//                    Cursor cursorCarer = getCursorCarer(id);
//                    JSONObject carer = new JSONObject();
//                    if (cursorCarer != null && cursorCarer.moveToFirst()) {
//                        int idCarer = cursorCarer.getInt(cursorCarer
//                                .getColumnIndex(Constant.COL_COHABITANT_ID));
//
//                        Cursor cursorWarning = getCursorWarning(idCarer);
//                        ArrayList<String> warnings = getJsonWarningSigns(cursorWarning);
//                        closeCursor(cursorWarning);
//
//                        Cursor cursorPfeiffer = getCursorPfeiffer(idCarer);
//                        ArrayList<JSONObject> pfeiffer = new ArrayList<>();
//                        if (cursorPfeiffer != null && cursorPfeiffer.moveToFirst()) {
//                            pfeiffer = getJsonPfeiffer(cursorPfeiffer);
//                        }
//                        closeCursor(cursorPfeiffer);
//
//                        carer = getJsonCarer(cursorCarer, warnings, pfeiffer);
//                    }
//                    closeCursor(cursorCarer);

                    /*Cursor cursorAntecedents = getCursorAntecedents(id);
                    ArrayList<String> antecedents = getJsonAntecedents(cursorAntecedents);
                    closeCursor(cursorAntecedents);*/

                    Cursor cursorAntecedents = getCursorAntecedents(id);
                    JSONObject antecedents = new JSONObject();
                    if (cursorAntecedents != null && cursorAntecedents.moveToFirst()) {
                        antecedents = getJsonAntecedents(cursorAntecedents);
                    }
                    closeCursor(cursorAntecedents);

                    Cursor cursorPfeiffer = getCursorPfeiffer(id);
                    ArrayList<JSONObject> pfeiffer = new ArrayList<>();
                    if (cursorPfeiffer != null && cursorPfeiffer.moveToFirst()) {
                        pfeiffer = getJsonPfeiffer(cursorPfeiffer);
                    }
                    closeCursor(cursorPfeiffer);

                    Cursor cursorCdr = getCursorCdr(id);
                    ArrayList<JSONObject> cdr = new ArrayList<>();
                    if (cursorCdr != null && cursorCdr.moveToFirst()) {
                        cdr = getJsonCdr(cursorCdr);
                    }
                    closeCursor(cursorCdr);

                    Cursor cursorGds = getCursorGds(id);
                    ArrayList<JSONObject> gds = new ArrayList<>();
                    if (cursorGds != null && cursorGds.moveToFirst()) {
                        gds = getJsonGds(cursorGds);
                    }
                    closeCursor(cursorGds);

                    Cursor cursorHhies = getCursorHhies(id);
                    ArrayList<JSONObject> hhies = new ArrayList<>();
                    if (cursorHhies != null && cursorHhies.moveToFirst()) {
                        hhies = getJsonHhies(cursorHhies);
                    }
                    closeCursor(cursorHhies);

                    Cursor cursorKatz = getCursorKatz(id);
                    ArrayList<JSONObject> katz = new ArrayList<>();
                    if (cursorKatz != null && cursorKatz.moveToFirst()) {
                        katz = getJsonKatz(cursorKatz);
                    }
                    closeCursor(cursorKatz);

                    Cursor cursorLawton = getCursorLawton(id);
                    ArrayList<JSONObject> lawton = new ArrayList<>();
                    if (cursorLawton != null && cursorLawton.moveToFirst()) {
                        lawton = getJsonLawton(cursorLawton);
                    }
                    closeCursor(cursorLawton);

                    Cursor cursorSubjective = getCursorSubjective(id);
                    JSONObject subjective = new JSONObject();
                    if (cursorSubjective != null && cursorSubjective.moveToFirst()) {
                        subjective = getJsonSubjective(cursorSubjective);
                    }
                    closeCursor(cursorSubjective);

                    Cursor cursorFrail = getCursorFrail(id);
                    JSONObject frail = new JSONObject();
                    if (cursorFrail != null && cursorFrail.moveToFirst()) {
                        frail = getJsonFrail(cursorFrail);
                    }
                    closeCursor(cursorFrail);

                    Cursor cursorMinimental = getCursorMinimental(id);
                    JSONObject minimental = new JSONObject();
                    if (cursorMinimental != null && cursorMinimental.moveToFirst()) {
                        minimental = getJsonMMSE(cursorMinimental);
                    }
                    closeCursor(cursorMinimental);

                    Cursor cursorPhysical = getCursorPhysical(id);
                    JSONObject physical = getJsonPhysicalPerformance(cursorPhysical);
                    closeCursor(cursorPhysical);

                    Cursor cursorPas = getCursorPsychofamily(id);
                    ArrayList<JSONObject> pas = new ArrayList<>();
                    if (cursorPas != null && cursorPas.moveToFirst()) {
                        pas = getJsonPsychofamily(cursorPas);
                    }
                    closeCursor(cursorPas);

                    Cursor cursorPaffective = getCursorPsychoaffective(id);
                    ArrayList<JSONObject> affective = new ArrayList<>();
                    if (cursorPaffective != null && cursorPaffective.moveToFirst()) {
                        affective = getJsonPyschoaffective(cursorPaffective);
                    }
                    closeCursor(cursorPaffective);

                    JSONObject evaluation = JsonHelper.toJsonEvaluation(
                            antecedents,
                            physical,
                            subjective,
                            frail,
                            minimental,
                            pfeiffer,
                            cdr,
                            gds,
                            hhies,
                            katz,
                            lawton,
                            pas,
                            affective);
                    ArrayList<JSONObject> evaluations = new ArrayList<>();
                    evaluations.add(evaluation);

                    JSONObject subject = getJsonSubject(cursorSubjects, evaluations);
                    subjectsList.add(subject);
                }
            }

            JSONArray subjects = new JSONArray(subjectsList);

            mFilePath = FileHelper.saveJSONContent(JsonHelper
                    .getJsonPatientsToExport(subjects).toString());

            return ACTION_COMPLETED;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            closeCursor(cursorSubjects);
        }

        return ACTION_NO_COMPLETED;
    }

    private ArrayList<JSONObject> getJsonPyschoaffective(Cursor cursor) {
        ArrayList<JSONObject> affective = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_PSYCHOAFFECTIVE_SCALE, affective, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION));
        addScore(affective, score);

        return affective;
    }

    private ArrayList<JSONObject> getJsonPsychofamily(Cursor cursor) {
        ArrayList<JSONObject> family = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE, family, questionPos++, position);
        }

        int score = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_EVALUATION
                ));
        addScore(family, score);

        return family;
    }

    private JSONObject getJsonMMSE(Cursor cursor) {
        String temporalOrientationJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION));
        ArrayList<Integer> temporalOrientation = StringHelper.getArrayListFromJson(temporalOrientationJson);
        ArrayList<String> temporalOrientationText = new ArrayList<>(temporalOrientation.size());
        for (Integer position : temporalOrientation) {
            temporalOrientationText.add(MMSEHelper.getOrientationTemporalText(getApplicationContext(), position));
        }

        String spatialOrientationJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION));
        ArrayList<Integer> spatialOrientation = StringHelper.getArrayListFromJson(spatialOrientationJson);
        ArrayList<String> spatialOrientationText = new ArrayList<>(spatialOrientation.size());
        for (Integer position : spatialOrientation) {
            spatialOrientationText.add(MMSEHelper.getOrientationSpatialText(getApplicationContext(), position));
        }

        String retentionRegisterInformationJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION));
        ArrayList<Integer> retentionRegisterInformation = StringHelper.getArrayListFromJson(retentionRegisterInformationJson);
        ArrayList<String> retentionRegisterInformationText = new ArrayList<>(retentionRegisterInformation.size());
        for (Integer position : retentionRegisterInformation) {
            retentionRegisterInformationText.add(MMSEHelper.getRetentionInfoText(getApplicationContext(), position));
        }

        int mathAttentionFlag = cursor.getInt(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG));
        String mathAttentionJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION));
        ArrayList<String> mathText = new ArrayList<>(spatialOrientation.size());
        if (mathAttentionFlag == software.cneuro.neuroger.constant.Constant.MATH_COUNTING_TEST) {
            ArrayList<Integer> mathAttention = StringHelper.getArrayListFromJson(mathAttentionJson);
            for (Integer value : mathAttention) {
                mathText.add(value.toString());
            }
        }

        String rememberingJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_REMEMBERING));
        ArrayList<Integer> remembering = StringHelper.getArrayListFromJson(rememberingJson);
        ArrayList<String> rememberingText = new ArrayList<>(spatialOrientation.size());
        for (Integer position : remembering) {
            rememberingText.add(MMSEHelper.getReminderText(getApplicationContext(), position));
        }

        String languageJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_LANGUAGE));
        ArrayList<Integer> language = StringHelper.getArrayListFromJson(languageJson);
        ArrayList<String> languageText = new ArrayList<>(spatialOrientation.size());
        for (Integer position : language) {
            languageText.add(MMSEHelper.getLanguageText(getApplicationContext(), position));
        }

        String order3Json = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_ORDER_3));
        ArrayList<Integer> order3 = StringHelper.getArrayListFromJson(order3Json);
        ArrayList<String> order3Text = new ArrayList<>(spatialOrientation.size());
        for (Integer position : order3) {
            order3Text.add(MMSEHelper.getLanguageOrderIn3Text(getApplicationContext(), position));
        }

        String designJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_DESIGN));
        ArrayList<Integer> design = StringHelper.getArrayListFromJson(designJson);
        ArrayList<String> designText = new ArrayList<>(spatialOrientation.size());
        for (Integer position : design) {
            designText.add(MMSEHelper.getDesignText(getApplicationContext(), position));
        }

        int score = cursor.getInt(
                cursor.getColumnIndex(COL_MINIMENTAL_EVALUATION));

        return JsonHelper.toJsonMMSE(
                temporalOrientationText,
                spatialOrientationText,
                mathText,
                mathAttentionFlag == MATH_COUNTING_TEST ? "" : mathAttentionJson,
                retentionRegisterInformationText,
                rememberingText,
                languageText,
                order3Text,
                designText,
                score
        );
    }

    private JSONObject getJsonFrail(Cursor cursor) {
        int fatigue = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_FATIGUE
                ));
        int resistance = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_RESISTANCE
                ));
        int wandering = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WANDERING
                ));
        int weightCurrent = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_CURRENT
                ));
        int weightYearAgo = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO
                ));

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_EVALUATION));

        ArrayList<Integer> diseases = StringHelper.getArrayListFromJson(cursor.getString(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_DISEASES)));
        ArrayList<String> diseasesText = new ArrayList<>();
        for (Integer position : diseases) {
            diseasesText.add(FrailHelper.getDiseasesText(getApplicationContext(), position));
        }

        return JsonHelper.toJsonFrail(
                fatigue,
                resistance,
                wandering,
                diseasesText,
                weightCurrent,
                weightYearAgo,
                score);
    }

    private JSONObject getJsonSubject(Cursor cursor,
                                      ArrayList<JSONObject> evaluations) {
        String name = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_NAME
                ));
        String lastName = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_LASTNAME
                ));
        String identification = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_ID_NUMBER
                ));
        String guid = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_GUID
                ));
        String birth = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_BIRTHDATE
                ));
        String occupation = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_OCCUPATION
                ));
        String skinColor = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_SKIN_COLOR
                ));
        int genderId = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATIENT_SEX
                ));
        String yearsStudy = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_YEARS_STUDIES
                ));
        String profession = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_PROFESSION
                ));
        String civilStatus = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_CIVIL_STATUS
                ));
        String coexistence = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_COEXISTENCE
                ));
        String hospital = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_HOSPITAL
                ));
        String origin = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_ORIGIN
                ));
        String clinicClassification = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_CLINIC_CLASSIFICATION
                ));
        String country = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_COUNTRY
                ));
        String province = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_PROVINCE_STATE
                ));
        String municipality = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_MUNICIPALITY
                ));
        String address = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_ADDRESS
                ));
        String phone = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_PHONE_NUMBER
                ));
        int compensatedId = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATIENT_COMPENSATED
                ));
        String creationDate = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_INSERT_DATE
                ));
        String patientVersion = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_VERSION
                ));

        return JsonHelper.toJsonSubject(
                name,
                lastName,
                identification,
                guid,
                CalendarHelper.getDateToShow(birth),
                SubjectHelper.getOccupationText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(occupation)),
                SubjectHelper.getSkinColorText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(skinColor)),
                SubjectHelper.getGenderText(getApplicationContext(),
                        genderId),
                yearsStudy,
                profession,
                SubjectHelper.getCivilStatusText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(civilStatus)),
                SubjectHelper.getCoexistenceText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(coexistence)),
                SubjectHelper.getHospitalText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(hospital)),
                SubjectHelper.getOriginText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(origin)),
                SubjectHelper.getClassificationText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(clinicClassification)),
                SubjectHelper.getCountryText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(country)),
                SubjectHelper.getProvinceText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(country),
                        EvaluationPPHelper.zeroIfEmpty(province)),
                SubjectHelper.getMunicipalityText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(country),
                        EvaluationPPHelper.zeroIfEmpty(province),
                        EvaluationPPHelper.zeroIfEmpty(municipality)),
                address,
                phone,
                SubjectHelper.getCompensatedText(getApplicationContext(), compensatedId),
                patientVersion,
                creationDate,
                evaluations
        );
    }

    private JSONObject getJsonCarer(Cursor cursor,
                                    ArrayList<String> warningSigns,
                                    ArrayList<JSONObject> pfeiffer) {
        String name = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_FULLNAME
                ));
        String lastName = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_LASTNAME
                ));
        String age = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_AGE
                ));
        String genderId = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_SEX
                ));
        String familiarity = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_FAMILIARITY
                ));
        String phone = cursor.getString(
                cursor.getColumnIndex(Constant.COL_COHABITANT_PHONE_NUMBER
                ));

        return JsonHelper.toJsonCarer(name, lastName, age,
                SubjectHelper.getGenderText(getApplicationContext(), EvaluationPPHelper.zeroIfEmpty(genderId)),
                SubjectHelper.getFamiliarityText(getApplicationContext(), EvaluationPPHelper.zeroIfEmpty(familiarity)),
                phone, warningSigns, pfeiffer);

    }

    private JSONObject getJsonAntecedents(Cursor cursor) {
        ArrayList<String> antecedentsList = new ArrayList<>();
        int medicationQuantity = SUBJECT_NO_TAKES_MORE_THAN_5_MEDICAMENT_ID;

        if (cursor != null) {
            // comorbidities
            String comorbiditiesJson = cursor.getString(
                    cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID));
            ArrayList<Integer> comorbidities = StringHelper.getArrayListFromJson(comorbiditiesJson);
            for (Integer position : comorbidities) {
                String name = SubjectHelper.getPathologicalAntecedentsText(getApplicationContext(), position);

                antecedentsList.add(name);
            }

            medicationQuantity = cursor.getInt(
                    cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY
                    ));
        }

        return JsonHelper.toJsonPathologicalAntecedents(antecedentsList, medicationQuantity ==
                SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID ? getString(R.string.medicaments_result_negative) : getString(R.string.medicaments_result_ok));
    }

    private ArrayList<String> getJsonWarningSigns(Cursor cursor) {
        ArrayList<String> warningSings = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String position = cursor.getString(
                        cursor.getColumnIndex(Constant.COL_ALERT_SIGNS_ANSWERS_LIST));
                String name = WarningSignHelper.getWarningSignText(getApplicationContext(),
                        EvaluationPPHelper.zeroIfEmpty(position));

                warningSings.add(name);
            }
        }

        return warningSings;
    }

    private JSONObject getJsonPhysicalPerformance(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            // IMC
            double size = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_TALLA));
            double weight = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_PESO));
            // March
            double time = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS));
            int steps = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS));
            // Static Balance
            int parallel = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS));
            int semiTandem = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM));
            int tandem = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_TANDEM));
            // Squads
            int squadRepetitions = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS));
            int squadCount = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS));
            double squadTime = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS));
            // Grip Strength
            double left1 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1));
            double right1 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1));
            double left2 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2));
            double right2 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2));

            double score = cursor.getDouble(
                    cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL
                    ));

            return JsonHelper.toJsonPhysicalPerformance(
                    weight, size,
                    time, steps,
                    parallel, semiTandem, tandem,
                    squadTime, squadRepetitions, squadCount,
                    left1, left2, right1, right2,
                    score
            );
        }

        return new JSONObject();
    }

    private JSONObject getJsonSubjective(Cursor cursor) {
        String healthState = SubjectiveHelper.getAnswer0Text(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_HEALTH_STATE
                        )));
        String memoryCurrent = SubjectiveHelper.getAnswer1AText(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_CURRENT
                        )));
        String memoryPast = SubjectiveHelper.getAnswer1BText(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_PAST
                        )));

        String difficultiesJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_SE_TEST_DIFFICULTIES));
        ArrayList<Integer> difficulties = StringHelper.getArrayListFromJson(difficultiesJson);
        ArrayList<String> difficultiesList = new ArrayList<>();
        for (Integer position : difficulties) {
            difficultiesList.add(SubjectiveHelper.getAnswer2Text(getApplicationContext(), position));
        }

        String iniCognitiveImpairment = SubjectiveHelper.getAnswer3Text(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT
                        )));
        String courseOfDeterioration = SubjectiveHelper.getAnswer4Text(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_COURSE_OF_DETERIORATION
                        )));
        String evolutionTime = SubjectiveHelper.getAnswer5Text(getApplicationContext(),
                cursor.getInt(
                        cursor.getColumnIndex(Constant.COL_SE_TEST_EVOLUTION_TIME
                        )));

        double score = cursor.getDouble(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_EVALUATION
                ));

        return JsonHelper.toJsonSubjective(
                healthState,
                memoryCurrent,
                memoryPast,
                difficultiesList,
                iniCognitiveImpairment,
                courseOfDeterioration,
                evolutionTime,
                score);
    }

    private ArrayList<JSONObject> getJsonCdr(Cursor cursor) {
        ArrayList<JSONObject> cdr = new ArrayList<>();

        int answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_MEMORY
                ));
        add(CARD_DETAIL_CDR, cdr, 0, answerPos);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_ORIENTATION
                ));
        add(CARD_DETAIL_CDR, cdr, 1, answerPos);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_JUDGEMENT
                ));
        add(CARD_DETAIL_CDR, cdr, 2, answerPos);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_COMMUNITY
                ));
        add(CARD_DETAIL_CDR, cdr, 3, answerPos);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_HOBBIES
                ));
        add(CARD_DETAIL_CDR, cdr, 4, answerPos);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_PERSONAL_CARE
                ));
        add(CARD_DETAIL_CDR, cdr, 5, answerPos);

        double score = cursor.getDouble(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_EVALUATION
                ));
        addScore(cdr, score);

        return cdr;
    }

    private ArrayList<JSONObject> getJsonGds(Cursor cursor) {
        ArrayList<JSONObject> gds = new ArrayList<>();
        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_DEPRESSION_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_GDS, gds, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_EVALUATION
                ));
        addScore(gds, score);

        return gds;
    }

    private ArrayList<JSONObject> getJsonHhies(Cursor cursor) {
        ArrayList<JSONObject> hhies = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_HHIES_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_HHIES, hhies, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_HHIES_TEST_EVALUATION
                ));
        addScore(hhies, score);

        return hhies;
    }

    private ArrayList<JSONObject> getJsonPfeiffer(Cursor cursor) {
        ArrayList<JSONObject> pfeiffer = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PFEIFFER_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_PFEIFFER, pfeiffer, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PFEIFFER_TEST_EVALUATION
                ));
        addScore(pfeiffer, score);

        return pfeiffer;
    }

    private ArrayList<JSONObject> getJsonKatz(Cursor cursor) {
        ArrayList<JSONObject> katz = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_KATZ_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_KATZ_INDEX, katz, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_KATZ_TEST_EVALUATION
                ));
        addScore(katz, score);

        return katz;
    }

    private ArrayList<JSONObject> getJsonLawton(Cursor cursor) {
        ArrayList<JSONObject> lawton = new ArrayList<>();

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_LAWTON_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        int questionPos = 0;
        for (Integer position : answerPosList) {
            add(CARD_DETAIL_LAWTON_MODIFIED_SCALE, lawton, questionPos++, position);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_LAWTON_TEST_EVALUATION
                ));
        addScore(lawton, score);

        return lawton;
    }

    private void add(String tag, ArrayList<JSONObject> jsonObjects, int questionPos, int answerPos) {
        String questionText = GDSHelper.getQuestionText(getApplicationContext(), questionPos);
        String answerText = GDSHelper.getAnswerText(getApplicationContext(), answerPos);

        switch (tag) {
            case CARD_DETAIL_CDR:
                questionText = CDRHelper.getTestTypesText(getApplicationContext(), questionPos);
                answerText = CDRHelper.getItemsList(getApplicationContext(), questionPos, answerPos);
                break;
            case CARD_DETAIL_HHIES:
                questionText = HHIESHelper.getQuestionText(getApplicationContext(), questionPos);
                answerText = HHIESHelper.getAnswerText(getApplicationContext(), answerPos);
                break;
            case CARD_DETAIL_PFEIFFER:
                questionText = PfeifferHelper.getQuestionText(getApplicationContext(), questionPos);
                answerText = PfeifferHelper.getAnswerText(getApplicationContext(), answerPos);
                break;
            case CARD_DETAIL_KATZ_INDEX:
                questionText = KatzHelper.getQuestionText(getApplicationContext(), questionPos);
                answerText = KatzHelper.getAnswerText(getApplicationContext(), answerPos);
                break;
            case CARD_DETAIL_LAWTON_MODIFIED_SCALE:
                questionText = LawtonHelper.getTestTypesText(getApplicationContext(), questionPos);
                answerText = LawtonHelper.getItemsList(getApplicationContext(), questionPos, answerPos);
                break;
            case CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE:
                questionText = PASHelper.getTestTypesText(getApplicationContext(), questionPos);
                answerText = PASHelper.getItemsList(getApplicationContext(), questionPos, answerPos);
                break;
            case CARD_DETAIL_PSYCHOAFFECTIVE_SCALE:
                questionText = PsychoaffectiveHelper.getQuestionText(getApplicationContext(), questionPos);
                answerText = PsychoaffectiveHelper.getAnswerText(getApplicationContext(), answerPos);
                break;
        }

        jsonObjects.add(JsonHelper.toJsonQuestionnaire(questionText, questionPos,
                answerText, answerPos));
    }

    private void addScore(ArrayList<JSONObject> jsonObjects, double score) {
        jsonObjects.add(JsonHelper.toJsonScore(score));
    }

    private Cursor getCursorPhysical(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PRUEBA_RF_MC_TALLA,
                Constant.COL_PRUEBA_RF_MC_PESO,
                Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS,
                Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS,
                Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS,
                Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM,
                Constant.COL_PRUEBA_RF_EE_TANDEM,
                Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS,
                Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS,
                Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2,
                Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2,
                Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL};

        Uri baseUri = Constant.URI_TABLE_PRUEBA_RF;

        String select = "((" + Constant.COL_PRUEBA_RF_PACIENTE_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorFrail(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_FRAIL_TEST_FATIGUE,
                Constant.COL_FRAIL_TEST_RESISTANCE,
                Constant.COL_FRAIL_TEST_WANDERING,
                Constant.COL_FRAIL_TEST_DISEASES,
                Constant.COL_FRAIL_TEST_WEIGHT_CURRENT,
                Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO,
                Constant.COL_FRAIL_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_FRAIL_TEST;

        String select = "((" + Constant.COL_FRAIL_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    private Cursor getCursorSubjective(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_SE_TEST_HEALTH_STATE,
                Constant.COL_SE_TEST_MEMORY_CURRENT,
                Constant.COL_SE_TEST_MEMORY_PAST,
                Constant.COL_SE_TEST_DIFFICULTIES,
                Constant.COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT,
                Constant.COL_SE_TEST_COURSE_OF_DETERIORATION,
                Constant.COL_SE_TEST_EVOLUTION_TIME,
                Constant.COL_SE_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST;

        String select = "((" + Constant.COL_SE_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    private Cursor getCursorLawton(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_LAWTON_TEST_ANSWERS_LIST,
                Constant.COL_LAWTON_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_LAWTON_TEST;

        String select = "((" + Constant.COL_LAWTON_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorKatz(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_KATZ_TEST_ANSWERS_LIST,
                Constant.COL_KATZ_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_KATZ_TEST;

        String select = "((" + Constant.COL_KATZ_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorHhies(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_HHIES_TEST_ANSWERS_LIST,
                Constant.COL_HHIES_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_HHIES_TEST;

        String select = "((" + Constant.COL_HHIES_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorGds(int id) {
        return mContentResolver.query(
                Constant.URI_TABLE_DEPRESSION_TEST,
                new String[]{
                        Constant.COL_DEPRESSION_TEST_ANSWERS_LIST,
                        Constant.COL_DEPRESSION_TEST_EVALUATION
                },
                "((" + Constant.COL_DEPRESSION_TEST_PATIENT_ID + " = " + id + " ))",
                null,
                null);
    }

    private Cursor getCursorCdr(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_CDR_TEST_PATIENT_ID,
                Constant.COL_CDR_TEST_MEMORY,
                Constant.COL_CDR_TEST_ORIENTATION,
                Constant.COL_CDR_TEST_JUDGEMENT,
                Constant.COL_CDR_TEST_COMMUNITY,
                Constant.COL_CDR_TEST_HOBBIES,
                Constant.COL_CDR_TEST_PERSONAL_CARE,
                Constant.COL_CDR_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_CDR_TEST;

        String select = "((" + Constant.COL_CDR_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorAntecedents(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID,
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY,
                Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(baseUri,
                PROJECTION, select, null, null);
    }

    private void closeCursor(Cursor... cursor) {
        for (Cursor c : cursor) {
            if (c != null)
                c.close();
        }
    }

    private Cursor getCursorPfeiffer(int idCarer) {
        String[] PROJECTION = new String[]{
                Constant.COL_PFEIFFER_TEST_ANSWERS_LIST,
                Constant.COL_PFEIFFER_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PFEIFFER_TEST;

        String select = "((" + Constant.COL_PFEIFFER_TEST_PATIENT_ID + " = " + idCarer + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorWarning(int idCarer) {
        String[] PROJECTION = new String[]{
                Constant.COL_ALERT_SIGNS_ANSWERS_LIST
        };

        Uri baseUri = Constant.URI_TABLE_ALERT_SIGNS;

        String select = "((" + Constant.COL_ALERT_SIGNS_COHABITANT_ID + " = " + idCarer + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorCarer(int id) {
        // These are the Contacts rows that we will retrieve.
        String[] PROJECTION = new String[]{
                Constant.COL_COHABITANT_ID,
                Constant.COL_COHABITANT_NAME, Constant.COL_COHABITANT_LASTNAME,
                Constant.COL_COHABITANT_AGE, Constant.COL_COHABITANT_SEX, Constant.COL_COHABITANT_FAMILIARITY,
                Constant.COL_COHABITANT_PHONE_NUMBER, Constant.COL_COHABITANT_FULLNAME};

        Uri baseUri = Constant.URI_TABLE_COHABITANT;

        String select = "((" + Constant.COL_COHABITANT_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorMinimental(int id) {
        String[] PROJECTION = new String[]{
                COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION,
                COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION,
                COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION,
                COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG,
                COL_MINIMENTAL_TEST_MATH_ATTENTION,
                COL_MINIMENTAL_TEST_REMEMBERING,
                COL_MINIMENTAL_TEST_LANGUAGE,
                COL_MINIMENTAL_TEST_ORDER_3,
                COL_MINIMENTAL_TEST_DESIGN,
                COL_MINIMENTAL_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_MINIMENTAL_TEST;

        String select = "((" + Constant.COL_MINIMENTAL_TEST_PATIENT_ID + " = " + id + " ))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorPsychofamily(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST,
                Constant.COL_PSYCHOFAMILY_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PSYCHOFAMILY_TEST;

        String select = "((" + Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }

    private Cursor getCursorPsychoaffective(int id) {
        String[] PROJECTION = new String[]{
                Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST,
                Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION
        };

        Uri baseUri = Constant.URI_TABLE_PSYCHOAFFECTIVE_TEST;

        String select = "((" + Constant.COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID + " = " + id + "))";

        return mContentResolver.query(
                baseUri,
                PROJECTION,
                select,
                null,
                null);
    }
}

