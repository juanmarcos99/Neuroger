package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 18/03/2016.
 */
public class InsertSubEvaluationTest_AsyncTask extends AsyncTask<Void, Void, Void> {

    private final long idPatient;
    private final long healthState;
    private final int memoryCurrent;
    private final int memoryPast;
    private final String difficulties;
    private final int iniCognitiveImpairment;
    private final int courseOfDeterioration;
    private final int evolutionTime;
    private final double evaluation;

    private final OnSubEvaluationTestInserted mCallback;
    private final ContentResolver cr;

    public InsertSubEvaluationTest_AsyncTask(Context context,
                                             long idPatient,
                                             int healthState,
                                             int memoryCurrent,
                                             int memoryPast,
                                             String difficulties,
                                             int iniCognitiveImpairment,
                                             int courseOfDeterioration,
                                             int evolutionTime,
                                             double evaluation,
                                             OnSubEvaluationTestInserted callback) {
        this.idPatient = idPatient;
        this.healthState = healthState;
        this.memoryCurrent = memoryCurrent;
        this.memoryPast = memoryPast;
        this.difficulties = difficulties;
        this.iniCognitiveImpairment = iniCognitiveImpairment;
        this.courseOfDeterioration = courseOfDeterioration;
        this.evolutionTime = evolutionTime;
        this.evaluation = evaluation;

        this.mCallback = callback;
        this.cr = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(Constant.COL_SE_TEST_PATIENT_ID, idPatient);
        values.put(Constant.COL_SE_TEST_HEALTH_STATE, healthState);
        values.put(Constant.COL_SE_TEST_MEMORY_CURRENT, memoryCurrent);
        values.put(Constant.COL_SE_TEST_MEMORY_PAST, memoryPast);
        values.put(Constant.COL_SE_TEST_DIFFICULTIES, difficulties);
        values.put(Constant.COL_SE_TEST_INI_COGNITIVE_IMPAIRMENT, iniCognitiveImpairment);
        values.put(Constant.COL_SE_TEST_COURSE_OF_DETERIORATION, courseOfDeterioration);
        values.put(Constant.COL_SE_TEST_EVOLUTION_TIME, evolutionTime);
        values.put(Constant.COL_SE_TEST_EVALUATION, evaluation);

        cr.insert(Constant.URI_TABLE_SUBJECTIVE_EVALUATION_TEST, values);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCallback.onTestInserted();
    }

    public interface OnSubEvaluationTestInserted {
        void onTestInserted();
    }

}
