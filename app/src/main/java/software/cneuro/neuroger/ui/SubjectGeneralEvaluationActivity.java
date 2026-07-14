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
import android.widget.LinearLayout;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.ui.detail.BaseDetailActivity;
import software.cneuro.neuroger.ui.detail.card.CardIconAndEvalItemFragment;
import software.cneuro.neuroger.ui.detail.evaluation.PhysicalPerformanceDetailFragment;
import software.cneuro.neuroger.ui.detail.subject.SubjectDetailFragment;
import software.cneuro.neuroger.ui.input.evaluation.PhysicalPerformanceInputActivity;
import software.cneuro.neuroger.ui.input.evaluation.PhysicalPerformanceInputFragment;

public class SubjectGeneralEvaluationActivity extends BaseSubjectInfoActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CardIconAndEvalItemFragment.OnCardIconItemFragmentListener {
    public static final String ARG_NEW_SUBJECT = "new_subject";

    public static final int EDIT_ACTIVITY_REQUEST_CODE = 144;

    public static final String[] SUBJECT_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_FULLNAME
    };
    // Physical
    static final String[] PHYSICAL_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EVALUACION_GENERAL
    };
    // Classification
    static final String[] CLASSIFICATION_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION
    };
    // Cognitive
    static final String[] COGNITIVE_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_ID,
            software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION
    };

    private static final int SUBJECT_LOADER_ID = 9;
    private static final int PHYSICAL_LOADER_ID = 12;
    private static final int CLASSIFICATION_LOADER_ID = 13;
    private static final int COGNITIVE_LOADER_ID = 14;

    CardIconAndEvalItemFragment subjectFragment,
            classificationFragment,
            physicalFragment,
            cognitiveFragment;

    boolean isClassificationSet;
    LinearLayout optionalTestsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_general_detail);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle intent = getIntent().getExtras();

            assert intent != null;
            updateSubject(intent);

            optionalTestsContainer = findViewById(R.id.optional_tests_container);

            subjectFragment = (CardIconAndEvalItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_subject);
            assert subjectFragment != null;
            subjectFragment.updateContent(Constant.CARD_DETAIL_SUBJECT, getString(R.string.card_label_subject_title), R.drawable.ic_patient_data);
            subjectFragment.updateContent(true, R.drawable.ic_ok);

            classificationFragment = (CardIconAndEvalItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_classification);
            assert classificationFragment != null;
            classificationFragment.updateContent(Constant.CARD_DETAIL_CLASSIFICATION, getString(R.string.card_classification_title), R.drawable.ic_classification);

            physicalFragment = (CardIconAndEvalItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_physical_performance);
            assert physicalFragment != null;
            physicalFragment.updateContent(Constant.CARD_DETAIL_PHYSICAL_EVALUATION, getString(R.string.card_physical_performance_title), R.drawable.ic_physical_performance);

            cognitiveFragment = (CardIconAndEvalItemFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.card_cognitive_performance);
            assert cognitiveFragment != null;
            cognitiveFragment.updateContent(Constant.CARD_DETAIL_COGNITIVE_PERFORMANCE, getString(R.string.card_cognitive_performance_title), R.drawable.ic_cognitive_performance);
        }

        getSupportLoaderManager().initLoader(SUBJECT_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(PHYSICAL_LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(CLASSIFICATION_LOADER_ID, null, this);
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
    public void onCardIconItemClick(String tag, boolean isDone) {
        switch (tag) {
            case Constant.CARD_DETAIL_SUBJECT: {
                Intent intent = new Intent(this, BaseDetailActivity.class);
                intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_SUBJECT);
                intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mFullName);
                intent.putExtra(SubjectDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
                break;
            }
            case Constant.CARD_DETAIL_PHYSICAL_EVALUATION:
                Intent intent;
                if (isDone) {
                    intent = new Intent(this, BaseDetailActivity.class);
                    intent.putExtra(BaseDetailActivity.ARG_TEST_TYPE, Constant.CARD_DETAIL_PHYSICAL_EVALUATION);
                    intent.putExtra(PhysicalPerformanceDetailFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(PhysicalPerformanceDetailFragment.ARG_IS_FEMALE, mGender == Constant.SUBJECT_FEMALE_ID);
                    intent.putExtra(BaseDetailActivity.ARG_SUBJECT_FULL_NAME, mName);
                } else {
                    intent = new Intent(this, PhysicalPerformanceInputActivity.class);
                    intent.putExtra(PhysicalPerformanceInputFragment.ARG_SUBJECT_ID, mSubjectId);
                    intent.putExtra(PhysicalPerformanceInputFragment.ARG_IS_FEMALE, mGender == Constant.SUBJECT_FEMALE_ID);
                    intent.putExtra(PhysicalPerformanceInputActivity.ARG_SUBJECT_NAME, mName);
                }
                startActivity(intent);
                break;
            case Constant.CARD_DETAIL_CLASSIFICATION: {
                intent = new Intent(this, SubjectClassificationActivity.class);
                intent.putExtra(SubjectClassificationActivity.ARG_SUBJECT_ID, mSubjectId);
                intent.putExtra(SubjectClassificationActivity.ARG_SUBJECT_NAME, mName);
                intent.putExtra(SubjectClassificationActivity.ARG_SUBJECT_LAST_NAME, mLastName);
                intent.putExtra(SubjectClassificationActivity.ARG_SUBJECT_GENDER, mGender);
                startActivity(intent);
                break;
            }
            case Constant.CARD_DETAIL_COGNITIVE_PERFORMANCE: {
                intent = new Intent(this, CognitiveEvaluationActivity.class);
                intent.putExtra(CognitiveEvaluationActivity.ARG_SUBJECT_ID, mSubjectId);
                intent.putExtra(CognitiveEvaluationActivity.ARG_SUBJECT_NAME, mName);
                intent.putExtra(CognitiveEvaluationActivity.ARG_SUBJECT_LAST_NAME, mLastName);
                intent.putExtra(CognitiveEvaluationActivity.ARG_SUBJECT_AGE, mAge);
                startActivity(intent);
                break;
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == PHYSICAL_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PRUEBA_RF;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PACIENTE_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    PHYSICAL_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == CLASSIFICATION_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_CLASSIFICATION;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    CLASSIFICATION_PROJECTION,
                    select,
                    null,
                    null);
        } else if (id == COGNITIVE_LOADER_ID) {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_COGNITIVE;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_PATIENT_ID + " = " + mSubjectId + "))";

            return new CursorLoader(
                    this,
                    baseUri,
                    COGNITIVE_PROJECTION,
                    select,
                    null,
                    null);
        } else {
            Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PATIENT;

            String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_ID + " = " + mSubjectId + " ))";

            return new CursorLoader(
                    this,
                    baseUri,
                    SUBJECT_PROJECTION,
                    select,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case PHYSICAL_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    int evaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EVALUACION_GENERAL));
                    physicalFragment.updateContent(true, ImageHelper.getEvaluationImage(evaluation));
                }
                break;
            }
            case CLASSIFICATION_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    int evaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_CLASSIFICATION_EVALUATION));
                    isClassificationSet = evaluation != Constant.NO_EVALUATED;
                    if (isClassificationSet)
                        classificationFragment.updateContent(true, ImageHelper.getEvaluationImage(evaluation));
//                    showOptionalTests(evaluation == Constant.RESULT_NEGATIVE);
                }
                break;
            }
            case COGNITIVE_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    int evaluation = cursor.getInt(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_COGNITIVE_EVALUATION));
                    cognitiveFragment.updateContent(true, ImageHelper.getEvaluationImage(evaluation));
                }
                break;
            }
            case SUBJECT_LOADER_ID: {
                if (cursor != null && cursor.moveToFirst()) {
                    mFullName = cursor.getString(
                            cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PATIENT_FULLNAME
                            ));
                    updateSubjectFullName();
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                updateSubject(data.getExtras());
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        } /*else if (requestCode == PATHOLOGICAL_ANTECEDENTS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getSupportLoaderManager().restartLoader(PATHOLOGICAL_ANTECEDENTS_LOADER_ID, null, this);
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        } else if (requestCode == PHYSICAL_EVALUATION_INPUT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getSupportLoaderManager().initLoader(PHYSICAL_LOADER_ID, null, this);
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        } else if (requestCode == CLASSIFICATION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getSupportLoaderManager().initLoader(CLASSIFICATION_LOADER_ID, null, this);
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled.
            } else {
                // failed, advise user.
            }
        }*/
    }

    private void updateSubjectFullName() {
        ((TextView) findViewById(R.id.subject_detail_name)).setText(mFullName);
    }

    private void showOptionalTests(boolean show) {
        optionalTestsContainer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
