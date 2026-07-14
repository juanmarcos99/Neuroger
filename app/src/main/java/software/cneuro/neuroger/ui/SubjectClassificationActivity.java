package software.cneuro.neuroger.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationClassificationHelper;
import software.cneuro.neuroger.content.EvaluationFrailHelper;
import software.cneuro.neuroger.content.EvaluationGeneral;
import software.cneuro.neuroger.content.EvaluationKatzHelper;
import software.cneuro.neuroger.content.EvaluationLawtonHelper;
import software.cneuro.neuroger.content.EvaluationPASHelper;
import software.cneuro.neuroger.content.EvaluationPAntecedentsHelper;
import software.cneuro.neuroger.content.EvaluationPsychoaffectiveHelper;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.content.KatzHelper;
import software.cneuro.neuroger.content.LawtonHelper;
import software.cneuro.neuroger.content.PASHelper;
import software.cneuro.neuroger.content.PsychoaffectiveHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.detail.BaseDetailActivity;
import software.cneuro.neuroger.ui.detail.card.CardEvaluationItemFragment;
import software.cneuro.neuroger.ui.detail.evaluation.FrailScaleDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.PathologicalAntecedentsDetailFragment;
import software.cneuro.neuroger.ui.detail.evaluation.SubjectiveEvaluationDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.KatzIndexDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.LawtonDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PASDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PsychoaffectiveDetailFragment;
import software.cneuro.neuroger.ui.input.evaluation.BaseInputActivity;
import software.cneuro.neuroger.ui.input.evaluation.FrailScaleInputFragment;
import software.cneuro.neuroger.ui.input.evaluation.PathologicalAntecedentsInputFragment;
import software.cneuro.neuroger.ui.input.evaluation.SubjectiveEvaluationInputFragment;
import software.cneuro.neuroger.ui.input.questionnaire.QuestionnairePagerActivity;
import software.cneuro.neuroger.ui.input.questionnaire.QuestionnairePagerFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertClassification_AsyncTask;

public class SubjectClassificationActivity extends BaseSubjectInfoActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CardEvaluationItemFragment.OnCardEvaluationFragmentListener,
        InsertClassification_AsyncTask.OnClassificationInserted {
    // To know if i need to insert
    protected static final int PATHOLOGICAL_ANTECEDENTS_LOADER_ID = 11;
    // Pathological
    protected static final String[] PATHOLOGICAL_ANTECEDENTS_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION
    };
    // Subjective
    static final String[] SUBJECTIVE_EVALUATION_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_EVALUATION
    };
    // Katz
    static final String[] KATZ_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_KATZ_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_KATZ_TEST_EVALUATION
    };
    // Frail
    static final String[] FRAIL_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_FRAIL_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_FRAIL_TEST_EVALUATION
    };
    // Lawton
    static final String[] LAWTON_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_LAWTON_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_LAWTON_TEST_EVALUATION
    };
    // Psychofamily
    static final String[] PAS_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOFAMILY_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOFAMILY_TEST_EVALUATION
    };
    // Psychoaffective
    static final String[] PAFFECTIVE_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOFAMILY_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION
    };

    // Classification
    static final String[] CLASSIFICATION_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION
    };

    private static final int CLASSIFICATION_LOADER_ID = 15;
    private static final int SUBJECTIVE_LOADER_ID = 13;
    private static final int KATZ_INDEX_LOADER_ID = 226;
    private static final int FRAIL_SCALE_LOADER_ID = 227;
    private static final int LAWTON_MODIFIED_SCALE_LOADER_ID = 228;
    private static final int PSYCHOFAMILY_ASSESSMENT_SCALE_LOADER_ID = 229;
    private static final int PSYCHOAFFECTIVE_SCALE_LOADER_ID = 300;

    CardEvaluationItemFragment antecedentsFragment,
            subjectiveFragment,
            katzFragment,
            frailFragment,
            lawtonFragment,
            pasFragment,
            paffectiveFragment;

    private boolean isClassificationSet;
    private int medicationQuantity;
    private int antecedentsEvaluation,
            subjectiveEvaluation,
            katzEvaluation,
            frailEvaluation,
            lawtonEvaluation,
            pasEvaluation,
            paffectiveEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_classification);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle intent = getIntent().getExtras();

        assert intent != null;
        updateSubject(intent);

        initEvaluationsValues();

        if (savedInstanceState == null) {
            antecedentsFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_pathological_antecedents);
            assert antecedentsFragment != null;
            antecedentsFragment.updateContent(Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS, getString(R.string.card_antecedents_title));

            subjectiveFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_subjective_evaluation);
            assert subjectiveFragment != null;
            subjectiveFragment.updateContent(Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION, getString(R.string.card_subjective_evaluation_title));

            katzFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_katz);
            assert katzFragment != null;
            katzFragment.updateContent(Constant.CARD_DETAIL_KATZ_INDEX, getString(R.string.card_katz_title));

            frailFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_frail);
            assert frailFragment != null;
            frailFragment.updateContent(Constant.CARD_DETAIL_FRAIL_SCALE, getString(R.string.card_frail_title));

            lawtonFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_lawton);
            assert lawtonFragment != null;
            lawtonFragment.updateContent(Constant.CARD_DETAIL_LAWTON_MODIFIED_SCALE, getString(R.string.card_lawton_title));

            pasFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_psychofamily);
            assert pasFragment != null;
            pasFragment.updateContent(Constant.CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE, getString(R.string.card_psychofamily_title));

            paffectiveFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_psychoaffective);
            assert paffectiveFragment != null;
            paffectiveFragment.updateContent(Constant.CARD_DETAIL_PSYCHOAFFECTIVE_SCALE, getString(R.string.card_psychoaffective_title));
        }

        // CLASSIFICATION
        getSupportLoaderManager().initLoader(CLASSIFICATION_LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardEvaluationItemClick(String tag, boolean isDone) {
        switch (tag) {
            case Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS);
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(PathologicalAntecedentsDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, BaseInputActivity.class);
                    intent.putExtra(BaseInputActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PATHOLOGICAL_ANTECEDENTS);
                    intent.putExtra(BaseInputActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(PathologicalAntecedentsInputFragment.ARG_SUBJECT_ID, mSubjectId);
                }
                intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_pathological_antecedents_input));
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION);
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(SubjectiveEvaluationDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, BaseInputActivity.class);
                    intent.putExtra(BaseInputActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_SUBJECTIVE_EVALUATION);
                    intent.putExtra(BaseInputActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(SubjectiveEvaluationInputFragment.ARG_SUBJECT_ID, mSubjectId);
                }
                intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_subjective_evaluation));
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_KATZ_INDEX: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_KATZ_INDEX);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_katz));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(KatzIndexDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_KATZ);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, KatzHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_katz));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_FRAIL_SCALE: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_FRAIL_SCALE);
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(FrailScaleDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, BaseInputActivity.class);
                    intent.putExtra(BaseInputActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_FRAIL_SCALE);
                    intent.putExtra(BaseInputActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(FrailScaleInputFragment.ARG_SUBJECT_ID, mSubjectId);
                }
                intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_frail_input));
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_LAWTON_MODIFIED_SCALE: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_LAWTON_MODIFIED_SCALE);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_lawton));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(LawtonDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(LawtonDetailFragment.ARG_SUBJECT_GENDER, mGender);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_LAWTON);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, LawtonHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_GENDER, mGender);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_lawton));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PSYCHOFAMILY_ASSESSMENT_SCALE);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_psychofamily));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(PASDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_PSYCHOFAMILY);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, PASHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_psychofamily));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_PSYCHOAFFECTIVE_SCALE: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PSYCHOAFFECTIVE_SCALE);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_psychoaffective));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(PsychoaffectiveDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_PSYCHOAFFECTIVE);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, PsychoaffectiveHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_psychoaffective));
                }
                startActivity(intent);
                break;
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == PATHOLOGICAL_ANTECEDENTS_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PATHOLOGICAL_ANTECEDENTS;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    PATHOLOGICAL_ANTECEDENTS_SUMMARY_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == SUBJECTIVE_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    SUBJECTIVE_EVALUATION_SUMMARY_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == KATZ_INDEX_LOADER_ID) { // KATZ
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_KATZ_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_KATZ_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    KATZ_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == FRAIL_SCALE_LOADER_ID) { // FRAIL
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_FRAIL_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_FRAIL_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    FRAIL_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == LAWTON_MODIFIED_SCALE_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_LAWTON_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_LAWTON_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    LAWTON_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == PSYCHOFAMILY_ASSESSMENT_SCALE_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PSYCHOFAMILY_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOFAMILY_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    PAS_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == PSYCHOAFFECTIVE_SCALE_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PSYCHOAFFECTIVE_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOAFFECTIVE_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    PAFFECTIVE_PROJECTION,
                    select,
                    null,
                    null);

        } else {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CLASSIFICATION;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    CLASSIFICATION_PROJECTION,
                    select,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case PATHOLOGICAL_ANTECEDENTS_LOADER_ID: {
                assert antecedentsFragment != null;
                if (cursor != null && cursor.moveToFirst()) {
                    medicationQuantity = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_MEDICATION_QUANTITY));
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATHOLOGICAL_ANTECEDENTS_EVALUATION));
                    antecedentsEvaluation = EvaluationPAntecedentsHelper.evaluate(score);
                    antecedentsFragment.updateContent(true, ImageHelper.getEvaluationImage(antecedentsEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case SUBJECTIVE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    subjectiveEvaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_SE_TEST_EVALUATION));
                    subjectiveFragment.updateContent(true, ImageHelper.getEvaluationImage(subjectiveEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case KATZ_INDEX_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_KATZ_TEST_EVALUATION));
                    katzEvaluation = EvaluationKatzHelper.evaluate(score);
                    katzFragment.updateContent(true, ImageHelper.getEvaluationImage(katzEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case FRAIL_SCALE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_FRAIL_TEST_EVALUATION));
                    frailEvaluation = EvaluationFrailHelper.evaluate(score);
                    frailFragment.updateContent(true, ImageHelper.getEvaluationImage(frailEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case LAWTON_MODIFIED_SCALE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_LAWTON_TEST_EVALUATION));
                    lawtonEvaluation = EvaluationLawtonHelper.evaluate(score, mGender == Constant.SUBJECT_FEMALE_ID);
                    lawtonFragment.updateContent(true, ImageHelper.getEvaluationImage(lawtonEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case PSYCHOFAMILY_ASSESSMENT_SCALE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOFAMILY_TEST_EVALUATION));
                    pasEvaluation = EvaluationPASHelper.evaluate(score);
                    pasFragment.updateContent(true, ImageHelper.getEvaluationImage(pasEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case PSYCHOAFFECTIVE_SCALE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PSYCHOAFFECTIVE_TEST_EVALUATION));
                    paffectiveEvaluation = EvaluationPsychoaffectiveHelper.evaluate(score);
                    paffectiveFragment.updateContent(true, ImageHelper.getEvaluationImage(paffectiveEvaluation));
                    verifyAndSaveClassification();
                }
                break;
            }
            case CLASSIFICATION_LOADER_ID: {
                isClassificationSet = (cursor != null && cursor.moveToFirst());

                if (isClassificationSet) {
                    int evaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION));
                    showGeneralEvaluation(evaluation);
                }

                // ANTECEDENTS
                getSupportLoaderManager().initLoader(PATHOLOGICAL_ANTECEDENTS_LOADER_ID, null, this);
                // SUBJECTIVE
                getSupportLoaderManager().initLoader(SUBJECTIVE_LOADER_ID, null, this);
                // KATZ
                getSupportLoaderManager().initLoader(KATZ_INDEX_LOADER_ID, null, this);
                // FRAIL
                getSupportLoaderManager().initLoader(FRAIL_SCALE_LOADER_ID, null, this);
                // LAWTON
                getSupportLoaderManager().initLoader(LAWTON_MODIFIED_SCALE_LOADER_ID, null, this);
                // PSYCHOFAMILY
                getSupportLoaderManager().initLoader(PSYCHOFAMILY_ASSESSMENT_SCALE_LOADER_ID, null, this);
                // PSYCHOAFFECTIVE
                getSupportLoaderManager().initLoader(PSYCHOAFFECTIVE_SCALE_LOADER_ID, null, this);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClassificationInserted() {

    }

    private void initEvaluationsValues() {
        isClassificationSet = false;

        antecedentsEvaluation = Constant.NO_EVALUATED;
        subjectiveEvaluation = Constant.NO_EVALUATED;
        katzEvaluation = Constant.NO_EVALUATED;
        frailEvaluation = Constant.NO_EVALUATED;
        lawtonEvaluation = Constant.NO_EVALUATED;
        pasEvaluation = Constant.NO_EVALUATED;
        paffectiveEvaluation = Constant.NO_EVALUATED;
    }

    private void verifyAndSaveClassification() {
        // already saved
        if (isClassificationSet) return;

        if (antecedentsEvaluation != Constant.NO_EVALUATED &&
                subjectiveEvaluation != Constant.NO_EVALUATED &&
                katzEvaluation != Constant.NO_EVALUATED &&
                frailEvaluation != Constant.NO_EVALUATED &&
                lawtonEvaluation != Constant.NO_EVALUATED &&
                pasEvaluation != Constant.NO_EVALUATED &&
                paffectiveEvaluation != Constant.NO_EVALUATED)
            DatabaseInserter.insertClassification(this,
                    mSubjectId,
                    antecedentsEvaluation,
                    medicationQuantity,
                    subjectiveEvaluation,
                    katzEvaluation,
                    frailEvaluation,
                    lawtonEvaluation,
                    pasEvaluation,
                    paffectiveEvaluation,
                    EvaluationClassificationHelper.evaluate(
                            antecedentsEvaluation,
                            medicationQuantity,
                            subjectiveEvaluation,
                            katzEvaluation,
                            frailEvaluation,
                            lawtonEvaluation,
                            pasEvaluation,
                            paffectiveEvaluation
                    ),
                    this);
    }

    private void showGeneralEvaluation(int value) {
        TextView result = findViewById(R.id.general_evaluation_result);
        result.setVisibility(View.VISIBLE);
        result.setText(StringHelper.appendWithDots(getString(R.string.general_classification), EvaluationGeneral.getEvaluation(this, value)));
    }
}
