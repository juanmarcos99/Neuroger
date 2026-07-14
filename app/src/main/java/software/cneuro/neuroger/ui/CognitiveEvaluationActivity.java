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
import software.cneuro.neuroger.content.CDRHelper;
import software.cneuro.neuroger.content.EvaluationCDRHelper;
import software.cneuro.neuroger.content.EvaluationCognitiveHelper;
import software.cneuro.neuroger.content.EvaluationGDSHelper;
import software.cneuro.neuroger.content.EvaluationGeneral;
import software.cneuro.neuroger.content.EvaluationHHIESHelper;
import software.cneuro.neuroger.content.EvaluationMMHelper;
import software.cneuro.neuroger.content.EvaluationPfeifferHelper;
import software.cneuro.neuroger.content.GDSHelper;
import software.cneuro.neuroger.content.HHIESHelper;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.content.PfeifferHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.detail.BaseDetailActivity;
import software.cneuro.neuroger.ui.detail.card.CardEvaluationItemFragment;
import software.cneuro.neuroger.ui.detail.evaluation.MiniMentalStateDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.CDRDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.GDSDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.HHIESDetailFragment;
import software.cneuro.neuroger.ui.detail.questionnaire.PfeifferDetailFragment;
import software.cneuro.neuroger.ui.input.evaluation.BaseInputActivity;
import software.cneuro.neuroger.ui.input.evaluation.MiniMentalStateInputFragment;
import software.cneuro.neuroger.ui.input.questionnaire.QuestionnairePagerActivity;
import software.cneuro.neuroger.ui.input.questionnaire.QuestionnairePagerFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertCognitive_AsyncTask;

public class CognitiveEvaluationActivity extends BaseSubjectInfoActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CardEvaluationItemFragment.OnCardEvaluationFragmentListener,
        InsertCognitive_AsyncTask.OnCognitiveInserted {

    // CDR Escala clínica para la demencia
    static final String[] CDR_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_MEMORY,
            software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_EVALUATION
    };
    // HHIE-S
    static final String[] HHIES_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_HHIES_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_HHIES_TEST_EVALUATION
    };
    // Pfeiffer
    static final String[] PFEIFFER_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PFEIFFER_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PFEIFFER_TEST_EVALUATION
    };
    // Minimental
    static final String[] MINI_MENTAL_STATE_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_EVALUATION
    };
    // Geriatric depression scale
    static final String[] GDS_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_EVALUATION
    };
    // Cognitive
    static final String[] COGNITIVE_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION
    };
    // To know if i need to insert
    private static final int COGNITIVE_LOADER_ID = 156;
    private static final int CDR_LOADER_ID = 221;
    private static final int PFEIFFER_LOADER_ID = 222;
    private static final int HHIES_LOADER_ID = 225;
    private static final int MINI_MENTAL_ID = 14;
    private static final int GDS_LOADER_ID = 224;

    CardEvaluationItemFragment cdrFragment,
            pfeifferFragment,
            hhiesFragment,
            miniMentalFragment,
            gdsFragment;
    private int cdrEvaluation;

    private boolean isCognitiveSet;
    private int mmseEvaluation,
            gdsEvaluation,
            hhiesEvaluation,
            pfeifferEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_cognitive_evaluation);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle intent = getIntent().getExtras();

        assert intent != null;
        updateSubject(intent);

        initEvaluationsValues();

        if (savedInstanceState == null) {
            cdrFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_cdr);
            assert cdrFragment != null;
            cdrFragment.updateContent(Constant.CARD_DETAIL_CDR, getString(R.string.card_cdr_title));

            pfeifferFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_pfeiffer);
            assert pfeifferFragment != null;
            pfeifferFragment.updateContent(Constant.CARD_DETAIL_PFEIFFER, getString(R.string.card_pfeiffer_title));

            hhiesFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_hhies);
            assert hhiesFragment != null;
            hhiesFragment.updateContent(Constant.CARD_DETAIL_HHIES, getString(R.string.card_hhies_title));

            miniMentalFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_mini_mental);
            assert miniMentalFragment != null;
            miniMentalFragment.updateContent(Constant.CARD_DETAIL_MINI_MENTAL_STATE, getString(R.string.card_mini_mental_state_title));

            gdsFragment = (CardEvaluationItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_gds);
            assert gdsFragment != null;
            gdsFragment.updateContent(Constant.CARD_DETAIL_GDS, getString(R.string.card_gds_title));
        }
        // COGNITIVE
        getSupportLoaderManager().initLoader(COGNITIVE_LOADER_ID, null, this);
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
            case Constant.CARD_DETAIL_CDR: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_CDR);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_cdr));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(CDRDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_CDR);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, CDRHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_cdr));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_PFEIFFER: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PFEIFFER);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_pfeiffer));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(PfeifferDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_PFEIFFER);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, PfeifferHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_pfeiffer));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_HHIES: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_HHIES);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_hhies));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(HHIESDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_HHIES);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, HHIESHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_hhies));
                }
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_MINI_MENTAL_STATE: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_MINI_MENTAL_STATE);
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(MiniMentalStateDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(MiniMentalStateDetailFragment.ARG_SUBJECT_AGE, mAge);
                    intent.putExtra(MiniMentalStateDetailFragment.ARG_SUBJECT_LEVEL_OF_SCHOOLING, mLevelOfSchooling);
                } else {
                    intent = new Intent(this, BaseInputActivity.class);
                    intent.putExtra(BaseInputActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_MINI_MENTAL_STATE);
                    intent.putExtra(BaseInputActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(MiniMentalStateInputFragment.ARG_SUBJECT_ID, mSubjectId);
                }
                intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_mini_mental_state_input));
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_GDS: {
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_GDS);
                    intent.putExtra(BaseActivity.ARG_TITLE, getString(R.string.title_activity_questionary_geriatric));
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                    intent.putExtra(GDSDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                } else {
                    intent = new Intent(this, QuestionnairePagerActivity.class);
                    intent.putExtra(QuestionnairePagerFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(QuestionnairePagerFragment.ARG_QUESTIONNAIRE_TYPE, Constant.QUESTIONNAIRE_GDS);
                    intent.putExtra(QuestionnairePagerFragment.ARG_TOTAL_VIEWS, GDSHelper.getTotalItems(this));
                    intent.putExtra(QuestionnairePagerActivity.ARG_SUBJECT_NAME, mFullName);
                    intent.putExtra(QuestionnairePagerActivity.ARG_TITLE, getString(R.string.title_activity_questionary_geriatric));
                }
                startActivity(intent);
                break;
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == CDR_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CDR_TEST;
            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_PATIENT_ID + " = " + mSubjectId + "))";
            return new CursorLoader(
                    this,
                    baseUri,
                    CDR_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == PFEIFFER_LOADER_ID) { // PFEIFFER
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PFEIFFER_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PFEIFFER_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    PFEIFFER_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == HHIES_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_HHIES_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_HHIES_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    HHIES_PROJECTION,
                    select,
                    null,
                    null);

        } else if (id == MINI_MENTAL_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_MINIMENTAL_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_TEST_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    MINI_MENTAL_STATE_SUMMARY_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == GDS_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_DEPRESSION_TEST;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    GDS_PROJECTION,
                    select,
                    null,
                    null);

        } else {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_COGNITIVE;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    COGNITIVE_PROJECTION,
                    select,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case CDR_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_CDR_TEST_EVALUATION));
                    cdrEvaluation = (int) EvaluationCDRHelper.evaluate(score);
                    cdrFragment.updateContent(true, ImageHelper.getEvaluationImage(cdrEvaluation));
                    verifyAndSaveCognitive();
                }
                break;
            }
            case PFEIFFER_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PFEIFFER_TEST_EVALUATION));
                    pfeifferEvaluation = EvaluationPfeifferHelper.evaluate(score);
                    pfeifferFragment.updateContent(true, ImageHelper.getEvaluationImage(pfeifferEvaluation));
                    verifyAndSaveCognitive();
                }
                break;
            }
            case HHIES_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_HHIES_TEST_EVALUATION));
                    hhiesEvaluation = EvaluationHHIESHelper.evaluate(score);
                    hhiesFragment.updateContent(true, ImageHelper.getEvaluationImage(hhiesEvaluation));
                    verifyAndSaveCognitive();
                }
                break;
            }
            case MINI_MENTAL_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_MINIMENTAL_EVALUATION));
                    mmseEvaluation = EvaluationMMHelper.evaluate(score, mAge, mLevelOfSchooling);
                    miniMentalFragment.updateContent(true, ImageHelper.getEvaluationImage(mmseEvaluation));
                    verifyAndSaveCognitive();
                }
                break;
            }
            case GDS_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    double score = cursor.getDouble(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_DEPRESSION_TEST_EVALUATION));
                    gdsEvaluation = EvaluationGDSHelper.evaluate(score);
                    gdsFragment.updateContent(true, ImageHelper.getEvaluationImage(gdsEvaluation));
                    verifyAndSaveCognitive();
                }
                break;
            }
            case COGNITIVE_LOADER_ID: {
                isCognitiveSet = (cursor != null && cursor.moveToFirst());

                if (isCognitiveSet) {
                    int evaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION));
                    showGeneralEvaluation(evaluation);
                }

                // CDR
                getSupportLoaderManager().initLoader(CDR_LOADER_ID, null, this);
                // PFEIFFER
                getSupportLoaderManager().initLoader(PFEIFFER_LOADER_ID, null, this);
                // HHIE-S
                getSupportLoaderManager().initLoader(HHIES_LOADER_ID, null, this);
                // MINIMENTAL
                getSupportLoaderManager().initLoader(MINI_MENTAL_ID, null, this);
                // GDS
                getSupportLoaderManager().initLoader(GDS_LOADER_ID, null, this);

                break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onCognitiveInserted() {

    }

    private void initEvaluationsValues() {
        isCognitiveSet = false;

        cdrEvaluation = Constant.NO_EVALUATED;
        hhiesEvaluation = Constant.NO_EVALUATED;
        pfeifferEvaluation = Constant.NO_EVALUATED;
        mmseEvaluation = Constant.NO_EVALUATED;
        gdsEvaluation = Constant.NO_EVALUATED;
    }

    private void verifyAndSaveCognitive() {
        // already saved
        if (isCognitiveSet) return;

        if (mmseEvaluation != Constant.NO_EVALUATED &&
                gdsEvaluation != Constant.NO_EVALUATED &&
                hhiesEvaluation != Constant.NO_EVALUATED &&
                pfeifferEvaluation != Constant.NO_EVALUATED &&
                cdrEvaluation != Constant.NO_EVALUATED)
            DatabaseInserter.insertCognitive(this,
                    mSubjectId,
                    cdrEvaluation,
                    gdsEvaluation,
                    hhiesEvaluation,
                    mmseEvaluation,
                    pfeifferEvaluation,
                    EvaluationCognitiveHelper.evaluate(
                            cdrEvaluation,
                            gdsEvaluation,
                            hhiesEvaluation,
                            mmseEvaluation,
                            pfeifferEvaluation
                    ),
                    this);
    }

    private void showGeneralEvaluation(int value) {
        TextView result = findViewById(R.id.general_evaluation_result);
        result.setVisibility(View.VISIBLE);
        result.setText(StringHelper.appendWithDots(getString(R.string.general_classification), EvaluationGeneral.getEvaluation(this, value)));
    }
}