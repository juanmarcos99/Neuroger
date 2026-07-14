package software.cneuro.neuroger.service;

import static software.cneuro.neuroger.constant.Constant.SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_DESIGN;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_LANGUAGE;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_ORDER_3;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_REMEMBERING;
import static software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION;

import android.content.Context;
import android.database.Cursor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.content.CDRHelper;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.EvaluationCDRHelper;
import software.cneuro.neuroger.content.EvaluationFrailHelper;
import software.cneuro.neuroger.content.EvaluationGDSHelper;
import software.cneuro.neuroger.content.EvaluationGeneral;
import software.cneuro.neuroger.content.EvaluationHHIESHelper;
import software.cneuro.neuroger.content.EvaluationKatzHelper;
import software.cneuro.neuroger.content.EvaluationLawtonHelper;
import software.cneuro.neuroger.content.EvaluationMMHelper;
import software.cneuro.neuroger.content.EvaluationPASHelper;
import software.cneuro.neuroger.content.EvaluationPAntecedentsHelper;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.EvaluationPfeifferHelper;
import software.cneuro.neuroger.content.EvaluationPsychoaffectiveHelper;
import software.cneuro.neuroger.content.EvaluationSubValHelper;
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
import software.cneuro.neurogerdatabase.constant.Constant;

public class SheetHelper {

    public static void createPatientRow(Context context, Cursor cursor, String name, String lastName, HSSFSheet dataPatientSheet, int rowCount) {
        String ci = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_ID_NUMBER
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
        String creationDate = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATIENT_INSERT_DATE
                ));

        HSSFRow row = dataPatientSheet.createRow(rowCount);

        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);
        row.createCell(2).setCellValue(ci);
        row.createCell(3).setCellValue(SubjectHelper.getAge(context, birth));
        row.createCell(4).setCellValue(CalendarHelper.getDateToShow(birth));
        row.createCell(5).setCellValue(SubjectHelper.getSkinColorText(context, Integer.parseInt(skinColor)));
        row.createCell(6).setCellValue(SubjectHelper.getGenderText(context, genderId));
        row.createCell(7).setCellValue(SubjectHelper.getLevelOfSchoolingText(context, Integer.parseInt(yearsStudy)));

        row.createCell(8).setCellValue(SubjectHelper.getOccupationText(context, Integer.parseInt(occupation)));
        row.createCell(9).setCellValue(profession);
        row.createCell(10).setCellValue(SubjectHelper.getCivilStatusText(context, Integer.parseInt(civilStatus)));
        row.createCell(11).setCellValue(SubjectHelper.getCoexistenceText(context, Integer.parseInt(coexistence)));
        row.createCell(12).setCellValue(SubjectHelper.getHospitalText(context, Integer.parseInt(hospital)));
        row.createCell(13).setCellValue(SubjectHelper.getOriginText(context, Integer.parseInt(origin)));
        row.createCell(14).setCellValue(SubjectHelper.getCountryText(context, Integer.parseInt(country)));
        row.createCell(15).setCellValue(SubjectHelper.getProvinceText(context, Integer.parseInt(country), Integer.parseInt(province)));
        row.createCell(16).setCellValue(SubjectHelper.getMunicipalityText(
                context, Integer.parseInt(country), Integer.parseInt(province), Integer.parseInt(municipality)));
        row.createCell(17).setCellValue(address);
        row.createCell(18).setCellValue(phone);
        row.createCell(19).setCellValue(creationDate);
    }

    public static void createComorbidityMedicationData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.sheet_name_comorbidity_medication));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.comorbidity));
            rowHead.createCell(3).setCellValue(context.getString(R.string.medication));
            rowHead.createCell(4).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(5).setCellValue(context.getString(R.string.evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);
        // comorbidities
        String comorbiditiesJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_CHECKBOX_ID));
        ArrayList<Integer> comorbidities = StringHelper.getArrayListFromJson(comorbiditiesJson);

        int medicationQuantity = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY
                ));
        row.createCell(3).setCellValue(context.getString(medicationQuantity ==
                SUBJECT_TAKES_MORE_THAN_5_MEDICAMENT_ID ? R.string.medicaments_result_negative : R.string.medicaments_result_ok));

        double score = EvaluationPAntecedentsHelper.evaluate(context, comorbidities);
        row.createCell(4).setCellValue(score);

        row.createCell(5).setCellValue(EvaluationPAntecedentsHelper.getEvaluationText(context, score));

        for (int i = 0; i < comorbidities.size(); i++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            row.createCell(2).setCellValue(SubjectHelper.getPathologicalAntecedentsText(context, comorbidities.get(i)));
        }
    }

    public static void createSubjectiveData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_subjective_evaluation_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.health_state));
            rowHead.createCell(3).setCellValue(context.getString(R.string.card_label_memory));
            rowHead.createCell(4).setCellValue(context.getString(R.string.past_memory));
            rowHead.createCell(5).setCellValue(context.getString(R.string.difficulties));
            rowHead.createCell(6).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(7).setCellValue(context.getString(R.string.evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        int question_0 = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_SE_TEST_HEALTH_STATE
                ));
        row.createCell(2).setCellValue(SubjectiveHelper.getAnswer0Text(context, question_0));
        int question_1a = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_CURRENT
                ));
        row.createCell(3).setCellValue(SubjectiveHelper.getAnswer1AText(context, question_1a));
        int question_1b = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_SE_TEST_MEMORY_PAST
                ));
        row.createCell(4).setCellValue(SubjectiveHelper.getAnswer1BText(context, question_1b));

        int score = cursor.getInt(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_EVALUATION));
        row.createCell(6).setCellValue(score);

        row.createCell(7).setCellValue(EvaluationSubValHelper.getEvaluationText(context, score));

        String question2Json = cursor.getString(
                cursor.getColumnIndex(Constant.COL_SE_TEST_DIFFICULTIES));
        ArrayList<Integer> question2 = StringHelper.getArrayListFromJson(question2Json);
        for (int i = 0; i < question2.size(); i++) {
            row.createCell(5).setCellValue(SubjectiveHelper.getAnswer2Text(context, question2.get(i)));
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
        }
    }

    public static void createFrailData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_frail_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.card_label_fatigue));
            rowHead.createCell(3).setCellValue(context.getString(R.string.card_label_resistance));
            rowHead.createCell(4).setCellValue(context.getString(R.string.card_label_wandering));
            rowHead.createCell(5).setCellValue(context.getString(R.string.card_label_diseases));
            rowHead.createCell(6).setCellValue(context.getString(R.string.edittext_weight_loss_current_hint));
            rowHead.createCell(7).setCellValue(context.getString(R.string.edittext_weight_loss_1_year_ago_hint));
            rowHead.createCell(8).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(9).setCellValue(context.getString(R.string.evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        int fatigue = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_FATIGUE
                ));
        row.createCell(2).setCellValue(FrailHelper.getFatigueText(context, fatigue));

        int resistance = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_RESISTANCE
                ));
        row.createCell(3).setCellValue(FrailHelper.getResistanceText(context, resistance));

        int wandering = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WANDERING
                ));
        row.createCell(4).setCellValue(FrailHelper.getWanderingText(context, wandering));

        int weightCurrent = cursor.getInt(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_CURRENT));
        row.createCell(6).setCellValue(StringHelper.appendMeasureUnit(String.valueOf(weightCurrent), context.getString(R.string.weight_measurement_unit)));
        int weightYearAgo = cursor.getInt(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_WEIGHT_YEAR_AGO));
        row.createCell(7).setCellValue(
                StringHelper.appendMeasureUnit(String.valueOf(weightYearAgo), context.getString(R.string.weight_measurement_unit)));

        double score = cursor.getDouble(cursor.getColumnIndex(Constant.COL_FRAIL_TEST_EVALUATION));
        row.createCell(8).setCellValue(score);

        row.createCell(9).setCellValue(EvaluationFrailHelper.getEvaluationText(context,
                EvaluationFrailHelper.evaluate(score)));

        // Diseases
        String diseasesJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_FRAIL_TEST_DISEASES));
        ArrayList<Integer> diseases = StringHelper.getArrayListFromJson(diseasesJson);
        for (int i = 0; i < diseases.size(); i++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            if (diseases.get(i) == software.cneuro.neuroger.constant.Constant.FRAIL_DISEASES_ANSWER_WAS_NO) {
                row.createCell(5).setCellValue(context.getString(R.string.no_diseases));
            } else {
                row.createCell(5).setCellValue(FrailHelper.getDiseasesText(context, diseases.get(i)));
            }
        }
    }

    public static void createPfeifferData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_pfeiffer_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PFEIFFER_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = PfeifferHelper.getQuestionText(context, questionPos, name);
            row.createCell(2).setCellValue(questionText);

            String answerText = PfeifferHelper.getAnswerText(context, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_PFEIFFER_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationPfeifferHelper.getEvaluationText(context,
                EvaluationPfeifferHelper.evaluate(score)));
    }

    public static void createPyschofamilyData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_psychofamily_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = PASHelper.getTestTypesText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = PASHelper.getItemsList(context, questionPos, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_PSYCHOFAMILY_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationPASHelper.getEvaluationText(context,
                EvaluationPASHelper.evaluate(score)));
    }

    public static void createLawtonData(Context context, Cursor cursor, String name, String lastName, boolean isFemale, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_lawton_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_LAWTON_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = LawtonHelper.getTestTypesText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = LawtonHelper.getItemsList(context, questionPos, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_LAWTON_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationLawtonHelper.getEvaluationText(context,
                isFemale,
                score));
    }

    public static void createCDRData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_cdr_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        int answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_MEMORY
                ));
        String questionText = CDRHelper.getTestTypesText(context, 0);
        String answerText = CDRHelper.getItemsList(context, 0, answerPos);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_ORIENTATION
                ));
        questionText = CDRHelper.getTestTypesText(context, 1);
        answerText = CDRHelper.getItemsList(context, 1, answerPos);
        row = sheet.createRow(rowCountInter++);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_JUDGEMENT
                ));
        questionText = CDRHelper.getTestTypesText(context, 2);
        answerText = CDRHelper.getItemsList(context, 2, answerPos);
        row = sheet.createRow(rowCountInter++);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_COMMUNITY
                ));
        questionText = CDRHelper.getTestTypesText(context, 3);
        answerText = CDRHelper.getItemsList(context, 3, answerPos);
        row = sheet.createRow(rowCountInter++);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_HOBBIES
                ));
        questionText = CDRHelper.getTestTypesText(context, 4);
        answerText = CDRHelper.getItemsList(context, 4, answerPos);
        row = sheet.createRow(rowCountInter++);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        answerPos = cursor.getInt(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_PERSONAL_CARE
                ));
        questionText = CDRHelper.getTestTypesText(context, 5);
        answerText = CDRHelper.getItemsList(context, 5, answerPos);
        row = sheet.createRow(rowCountInter);
        row.createCell(2).setCellValue(questionText);
        row.createCell(3).setCellValue(answerText);

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_CDR_TEST_EVALUATION
                ));

        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationCDRHelper.getEvaluationText(context, score));
    }

    public static void createPyschoaffectiveData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_psychoaffective_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);
        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = PsychoaffectiveHelper.getQuestionText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = PsychoaffectiveHelper.getAnswerText(context, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationPsychoaffectiveHelper.getEvaluationText(context,
                EvaluationPsychoaffectiveHelper.evaluate(score)));
    }

    public static void createKatzData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_katz_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_KATZ_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = KatzHelper.getQuestionText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = KatzHelper.getAnswerText(context, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_KATZ_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationKatzHelper.getEvaluationText(context,
                EvaluationKatzHelper.evaluate(score)));
    }

    public static void createGDSData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_gds_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_DEPRESSION_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = GDSHelper.getQuestionText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = GDSHelper.getAnswerText(context, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_DEPRESSION_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationGDSHelper.getEvaluationText(context,
                EvaluationGDSHelper.evaluate(score)));
    }

    public static void createHHIESData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_hhies_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            createRowHead(context, rowHead);
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(4);
        HSSFCell evaluationRow = row.createCell(5);

        String answerPosJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_HHIES_TEST_ANSWERS_LIST));
        ArrayList<Integer> answerPosList = StringHelper.getArrayListFromJson(answerPosJson);
        for (int i = 0, questionPos = 0; i < answerPosList.size(); i++, questionPos++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);
            String questionText = HHIESHelper.getQuestionText(context, questionPos);
            row.createCell(2).setCellValue(questionText);

            String answerText = HHIESHelper.getAnswerText(context, answerPosList.get(i));
            row.createCell(3).setCellValue(answerText);
        }

        double score = cursor.getDouble(
                cursor.getColumnIndex(Constant.COL_HHIES_TEST_EVALUATION));
        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationHHIESHelper.getEvaluationText(context,
                EvaluationHHIESHelper.evaluate(score)));
    }

    private static void createRowHead(Context context, HSSFRow rowHead) {
        rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
        rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
        rowHead.createCell(2).setCellValue(context.getString(R.string.question));
        rowHead.createCell(3).setCellValue(context.getString(R.string.label_answer));
        rowHead.createCell(4).setCellValue(context.getString(R.string.card_label_score));
        rowHead.createCell(5).setCellValue(context.getString(R.string.evaluation));
    }

    public static void createPhysicalData(Context context, Cursor cursor, String name, String lastName, boolean isFemale, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_physical_performance_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));

            rowHead.createCell(2).setCellValue(context.getString(R.string.card_label_imc));
            rowHead.createCell(3).setCellValue(context.getString(R.string.edittext_size_hint));
            rowHead.createCell(4).setCellValue(context.getString(R.string.edittext_weight_hint));
            rowHead.createCell(5).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(6).setCellValue(context.getString(R.string.evaluation));

            rowHead.createCell(7).setCellValue(context.getString(R.string.card_label_march));
            rowHead.createCell(8).setCellValue(context.getString(R.string.edittext_time_hint));
            rowHead.createCell(9).setCellValue(context.getString(R.string.edittext_steps_hint));
            rowHead.createCell(10).setCellValue(context.getString(R.string.card_label_velocity));
            rowHead.createCell(11).setCellValue(context.getString(R.string.card_label_amplitude));
            rowHead.createCell(12).setCellValue(context.getString(R.string.card_label_frequency));
            rowHead.createCell(13).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(14).setCellValue(context.getString(R.string.evaluation));

            rowHead.createCell(15).setCellValue(context.getString(R.string.card_label_static_balance));
            rowHead.createCell(16).setCellValue(context.getString(R.string.card_label_cbx_parallel_feet));
            rowHead.createCell(17).setCellValue(context.getString(R.string.card_label_cbx_semi_tandem_position));
            rowHead.createCell(18).setCellValue(context.getString(R.string.card_label_cbx_tandem_position));
            rowHead.createCell(19).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(20).setCellValue(context.getString(R.string.evaluation));

            rowHead.createCell(21).setCellValue(context.getString(R.string.card_label_speed_squat));
            rowHead.createCell(22).setCellValue(context.getString(R.string.edittext_time_hint));
            rowHead.createCell(23).setCellValue(context.getString(R.string.card_label_speed_squat_quantity));
            rowHead.createCell(24).setCellValue(context.getString(R.string.card_label_speed_squat_count));
            rowHead.createCell(25).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(26).setCellValue(context.getString(R.string.evaluation));

            rowHead.createCell(27).setCellValue(context.getString(R.string.card_label_grip_strength));
            rowHead.createCell(28).setCellValue(context.getString(R.string.card_label_left_hand_1));
            rowHead.createCell(29).setCellValue(context.getString(R.string.card_label_right_hand_1));
            rowHead.createCell(30).setCellValue(context.getString(R.string.card_label_left_hand_2));
            rowHead.createCell(31).setCellValue(context.getString(R.string.card_label_left_hand_2));
            rowHead.createCell(32).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(33).setCellValue(context.getString(R.string.evaluation));

            rowHead.createCell(34).setCellValue(context.getString(R.string.general_score));

            rowHead.createCell(35).setCellValue(context.getString(R.string.general_evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        // IMC
        double size = cursor.getDouble(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_TALLA));
        double weight = cursor.getDouble(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_PESO));
        double imc = EvaluationPPHelper.imc(size, weight);
        row.createCell(3).setCellValue(size);
        row.createCell(4).setCellValue(weight);
        row.createCell(5).setCellValue(StringHelper.truncate(imc));
        row.createCell(6).setCellValue(EvaluationPPHelper.getImcEvaluationText(context, EvaluationPPHelper.evaluateIMC(imc)));

        // March
        double time = cursor.getDouble(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS));
        int steps = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS));
        row.createCell(8).setCellValue(time);
        row.createCell(9).setCellValue(steps);

        double velocity = EvaluationPPHelper.walkSpeed(time);
        row.createCell(10).setCellValue(StringHelper.appendWithDots(
                StringHelper.truncate(velocity), context.getString(R.string.velocity_measurement_unit)));
        double amplitude = EvaluationPPHelper.averageStepExtend(steps);
        row.createCell(11).setCellValue(StringHelper.appendWithDots(
                StringHelper.truncate(amplitude), context.getString(R.string.amplitude_measurement_unit)));
        double frequency = EvaluationPPHelper.rateMinute(time, steps);
        row.createCell(12).setCellValue(StringHelper.appendWithDots(
                StringHelper.truncate(frequency), context.getString(R.string.frequency_measurement_unit)));

        int evaluation = EvaluationPPHelper.evaluateMarch(time);
        int score = EvaluationPPHelper.evaluationScoreMarch(evaluation);
        row.createCell(13).setCellValue(context.getResources().getQuantityString(R.plurals.scores, score, score));
        row.createCell(14).setCellValue(EvaluationPPHelper.getEvaluationText(context, evaluation));

        // Static Balance
        int parallel = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS));
        row.createCell(16).setCellValue(StringHelper.getClassificationText(context, parallel == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED));

        int semiTandem = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM));
        row.createCell(17).setCellValue(StringHelper.getClassificationText(context, semiTandem == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED));

        int tandem = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_TANDEM));
        row.createCell(18).setCellValue(StringHelper.getClassificationText(context, tandem == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED));

        evaluation = EvaluationPPHelper.evaluateStaticBalance(parallel == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED, semiTandem == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED, tandem == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED);
        score = EvaluationPPHelper.evaluationScoreStaticBalance(evaluation);
        row.createCell(19).setCellValue(context.getResources().getQuantityString(R.plurals.scores, score, score));
        row.createCell(20).setCellValue(EvaluationPPHelper.getEvaluationText(context, evaluation));

        // Squads
        int squadRepetitions = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS));
        row.createCell(22).setCellValue(StringHelper.getClassificationText(context, squadRepetitions == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED));

        int squadCount = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS));
        row.createCell(23).setCellValue(String.valueOf(squadCount));

        double squadTime = cursor.getDouble(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS));
        row.createCell(24).setCellValue(String.valueOf(squadTime));

        evaluation = EvaluationPPHelper.evaluateRising(squadTime, squadRepetitions == software.cneuro.neuroger.constant.Constant.RESULT_CHECKED);
        score = EvaluationPPHelper.evaluationScoreRising(evaluation);
        row.createCell(25).setCellValue(context.getResources().getQuantityString(R.plurals.scores, score, score));
        row.createCell(26).setCellValue(EvaluationPPHelper.getEvaluationText(context, evaluation));

        // Grip Strength
        double left1 = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1));
        double right1 = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1));
        double left2 = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2));
        double right2 = cursor.getInt(cursor
                .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2));
        row.createCell(28).setCellValue(StringHelper.appendMeasureUnit(
                String.valueOf(left1), context.getString(R.string.grip_strength_measurement_unit)));
        row.createCell(29).setCellValue(StringHelper.appendMeasureUnit(
                String.valueOf(right1), context.getString(R.string.grip_strength_measurement_unit)));
        row.createCell(30).setCellValue(StringHelper.appendMeasureUnit(
                String.valueOf(left2), context.getString(R.string.grip_strength_measurement_unit)));
        row.createCell(31).setCellValue(StringHelper.appendMeasureUnit(
                String.valueOf(left2), context.getString(R.string.grip_strength_measurement_unit)));

        double maxLeftGrip = Math.max(0, Math.max(left1, left2));
        double maxRightGrip = Math.max(0, Math.max(right1, right2));

        evaluation = EvaluationPPHelper.evaluateForce(
                maxLeftGrip, maxRightGrip, isFemale);
        score = EvaluationPPHelper.evaluationScoreForce(evaluation);
        row.createCell(32).setCellValue(context.getResources().getQuantityString(R.plurals.scores, score, score));
        row.createCell(33).setCellValue(EvaluationPPHelper.getEvaluationText(context, evaluation));

        // General score
        score = cursor.getInt(cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL));
        row.createCell(34).setCellValue(score);

        row.createCell(35).setCellValue(EvaluationPPHelper.getEvaluationText(context,
                EvaluationPPHelper.evaluateGeneral(score)));
    }

    public static void createMinimentalData(Context context, Cursor cursor, String name, String lastName, int age, int levelOfSchooling, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_mini_mental_state_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.card_label_temporal));
            rowHead.createCell(3).setCellValue(context.getString(R.string.card_label_spatial));
            rowHead.createCell(4).setCellValue(context.getString(R.string.math));
            rowHead.createCell(5).setCellValue(context.getString(R.string.spelling));
            rowHead.createCell(6).setCellValue(context.getString(R.string.card_label_retention_info));
            rowHead.createCell(7).setCellValue(context.getString(R.string.card_label_reminder));
            rowHead.createCell(8).setCellValue(context.getString(R.string.card_label_language));
            rowHead.createCell(9).setCellValue(context.getString(R.string.card_label_order_3));
            rowHead.createCell(10).setCellValue(context.getString(R.string.card_label_design));
            rowHead.createCell(11).setCellValue(context.getString(R.string.card_label_score));
            rowHead.createCell(12).setCellValue(context.getString(R.string.evaluation));
        }

        // start all the list at the same row
        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        HSSFCell scoreRow = row.createCell(11);
        HSSFCell evaluationRow = row.createCell(12);

        int score = 0;

        String temporalOrientationJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_TEMPORAL_ORIENTATION));
        ArrayList<Integer> temporalOrientation = StringHelper.getArrayListFromJson(temporalOrientationJson);
        score += temporalOrientation.size();

        String spatialOrientationJson = cursor.getString(
                cursor.getColumnIndex(Constant.COL_MINIMENTAL_TEST_SPATIAL_ORIENTATION));
        ArrayList<Integer> spatialOrientation = StringHelper.getArrayListFromJson(spatialOrientationJson);
        score += spatialOrientation.size();

        int mathAttentionFlag = cursor.getInt(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION_FLAG));
        String mathAttentionJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_MATH_ATTENTION));
        ArrayList<Integer> mathAttention = new ArrayList<>();

        if (mathAttentionFlag == software.cneuro.neuroger.constant.Constant.MATH_BACKWARDS_SPELLING_TEST) {
            row.createCell(5).setCellValue(mathAttentionJson);
            score += EvaluationMMHelper.getSpellingCount(mathAttentionJson);
        } else {
            mathAttention = StringHelper.getArrayListFromJson(mathAttentionJson);
            score += EvaluationMMHelper.getMathCount(mathAttention);
        }

        String retentionRegisterInformationJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_RETENTION_REGISTER_INFORMATION));
        ArrayList<Integer> retentionRegisterInformation = StringHelper.getArrayListFromJson(retentionRegisterInformationJson);
        score += retentionRegisterInformation.size();

        String rememberingJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_REMEMBERING));
        ArrayList<Integer> remembering = StringHelper.getArrayListFromJson(rememberingJson);
        score += remembering.size();

        String languageJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_LANGUAGE));
        ArrayList<Integer> language = StringHelper.getArrayListFromJson(languageJson);
        score += language.size();

        String order3Json = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_ORDER_3));
        ArrayList<Integer> order3 = StringHelper.getArrayListFromJson(order3Json);
        score += order3.size();

        String designJson = cursor.getString(
                cursor.getColumnIndex(COL_MINIMENTAL_TEST_DESIGN));
        ArrayList<Integer> design = StringHelper.getArrayListFromJson(designJson);
        score += design.size();

        int maxSize = Math.max(temporalOrientation.size(),
                Math.max(spatialOrientation.size(),
                        Math.max(mathAttention.size(),
                                Math.max(retentionRegisterInformation.size(),
                                        Math.max(remembering.size(),
                                                Math.max(language.size(),
                                                        Math.max(order3.size(), design.size())))))));

        for (int i = 0; i < maxSize; i++) {
            if (i > 0)
                row = sheet.createRow(rowCountInter++);

            if (i < temporalOrientation.size())
                row.createCell(2).setCellValue(MMSEHelper.getOrientationTemporalText(context, temporalOrientation.get(i)));
            if (i < spatialOrientation.size())
                row.createCell(3).setCellValue(MMSEHelper.getOrientationSpatialText(context, spatialOrientation.get(i)));
            if (i < mathAttention.size())
                row.createCell(4).setCellValue(mathAttention.get(i).toString());
            if (i < retentionRegisterInformation.size())
                row.createCell(6).setCellValue(MMSEHelper.getRetentionInfoText(context, retentionRegisterInformation.get(i)));
            if (i < remembering.size())
                row.createCell(7).setCellValue(MMSEHelper.getReminderText(context, remembering.get(i)));
            if (i < language.size())
                row.createCell(8).setCellValue(MMSEHelper.getLanguageText(context, language.get(i)));
            if (i < order3.size())
                row.createCell(9).setCellValue(MMSEHelper.getLanguageOrderIn3Text(context, order3.get(i)));
            if (i < design.size())
                row.createCell(10).setCellValue(MMSEHelper.getDesignText(context, design.get(i)));
        }

        scoreRow.setCellValue(score);

        evaluationRow.setCellValue(EvaluationMMHelper.getEvaluationText(context,
                EvaluationMMHelper.evaluate(score, age, levelOfSchooling)));
    }

    public static void createClassificationData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_classification_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        int evaluation = cursor.getInt(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION));
        row.createCell(2).setCellValue(EvaluationGeneral.getEvaluation(context, evaluation));
    }

    public static void createCognitiveData(Context context, Cursor cursor, String name, String lastName, HSSFWorkbook workbook) {
        String safeName = WorkbookUtil.createSafeSheetName(context.getString(R.string.card_cognitive_performance_title));
        HSSFSheet sheet = workbook.getSheet(safeName);
        int rowCountInter = 1;
        if (sheet != null) {
            rowCountInter = sheet.getLastRowNum() + 1;
        } else {
            sheet = workbook.createSheet(safeName);
            HSSFRow rowHead = sheet.createRow(0);
            rowHead.createCell(0).setCellValue(context.getString(R.string.label_name));
            rowHead.createCell(1).setCellValue(context.getString(R.string.label_lastName));
            rowHead.createCell(2).setCellValue(context.getString(R.string.evaluation));
        }

        HSSFRow row = sheet.createRow(rowCountInter++);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(lastName);

        int evaluation = cursor.getInt(
                cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION));
        row.createCell(2).setCellValue(EvaluationGeneral.getEvaluation(context, evaluation));
    }
}
