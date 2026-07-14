package software.cneuro.neuroger.ui.detail.evaluation;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.content.StringHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhysicalPerformanceDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_IS_FEMALE = "is_female";

    static final String[] PHYSICAL_EVALUATION_SUMMARY_PROJECTION = new String[]{
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_TALLA,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_PESO,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_TANDEM,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2,
            software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL
    };

    private long mSubjectId;
    private boolean mIsFemale;
    private double mScore;

    private TextView mScoreMarch, mScoreStaticBalance, mScoreSquad, mScoreGripStrength;
    private TextView mIMC, mVelocity, mAmplitude, mFrequency;
    private TextView mParallelFeet, mSemiTandem, mTandem;
    private TextView mSquadRepetitions, mSquadCount, mSquadTime;
    private TextView mLeftMaxGrip, mRightMaxGrip;
    private ImageView mMarchResultIcon, mIMCResultIcon, mStaticBalanceResultIcon, mSquadResultIcon, mGripStrengthResultIcon;

    public PhysicalPerformanceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
            if (getArguments().containsKey(ARG_IS_FEMALE)) {
                mIsFemale = getArguments().getBoolean(ARG_IS_FEMALE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_physical_performance_detail, container, false);

        // IMC
        mIMC = rootView.findViewById(R.id.card_imc);

        // March
        mVelocity = rootView.findViewById(R.id.card_march_velocity);
        mAmplitude = rootView.findViewById(R.id.card_march_amplitude);
        mFrequency = rootView.findViewById(R.id.card_march_frequency);
        mScoreMarch = rootView.findViewById(R.id.card_result_march_score);

        // Static Balance
        mParallelFeet = rootView.findViewById(R.id.card_parallel_feet);
        mSemiTandem = rootView.findViewById(R.id.card_semi_tantem_position);
        mTandem = rootView.findViewById(R.id.card_tantem_position);
        mScoreStaticBalance = rootView.findViewById(R.id.card_result_static_balance_score);

        // Squads
        mSquadRepetitions = rootView.findViewById(R.id.card_squad_repetitions);
        mSquadCount = rootView.findViewById(R.id.card_squad_count);
        mSquadTime = rootView.findViewById(R.id.card_squad_time);
        mScoreSquad = rootView.findViewById(R.id.card_result_squad_score);

        // Grip Strength
        mLeftMaxGrip = rootView.findViewById(R.id.card_left_grip_strength);
        mRightMaxGrip = rootView.findViewById(R.id.card_right_grip_strength);
        mScoreGripStrength = rootView.findViewById(R.id.card_result_grip_strength_score);

        // result images
        mMarchResultIcon = rootView.findViewById(R.id.card_result_march_icon);
        mIMCResultIcon = rootView.findViewById(R.id.card_result_imc_icon);
        mStaticBalanceResultIcon = rootView.findViewById(R.id.card_result_static_balance_icon);
        mSquadResultIcon = rootView.findViewById(R.id.card_result_squad_icon);
        mGripStrengthResultIcon = rootView.findViewById(R.id.card_result_grip_strength_icon);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
        outState.putBoolean(ARG_IS_FEMALE, mIsFemale);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
            mIsFemale = savedInstanceState.getBoolean(ARG_IS_FEMALE);
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = software.cneuro.neurogerdatabase.constant.Constant.URI_TABLE_PRUEBA_RF;

        String select = "((" + software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PACIENTE_ID + " = " + mSubjectId + " ))";

        assert getActivity() != null;
        return new CursorLoader(
                getActivity(),
                baseUri,
                PHYSICAL_EVALUATION_SUMMARY_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            // IMC
            double size = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_TALLA));
            double weight = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MC_PESO));
            double imc = EvaluationPPHelper.imc(size, weight);
            mIMC.setText(StringHelper.appendWithDots(getString(R.string.card_label_imc_large), StringHelper.truncate(imc)));

            mIMCResultIcon.setImageResource(
                    ImageHelper.getImcResultDrawable(
                            EvaluationPPHelper.evaluateIMC(imc)));

            // March
            double time = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS));
            int steps = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS));

            double velocity = EvaluationPPHelper.walkSpeed(time);
            mVelocity.setText(StringHelper.appendWithDots(
                    StringHelper.truncate(velocity), getString(R.string.velocity_measurement_unit)));

            double amplitude = EvaluationPPHelper.averageStepExtend(steps);
            mAmplitude.setText(StringHelper.appendWithDots(
                    StringHelper.truncate(amplitude), getString(R.string.amplitude_measurement_unit)));

            double frequency = EvaluationPPHelper.rateMinute(time, steps);
            mFrequency.setText(StringHelper.appendWithDots(
                    StringHelper.truncate(frequency), getString(R.string.frequency_measurement_unit)));

            int evaluation = EvaluationPPHelper.evaluateMarch(time);
            int score = EvaluationPPHelper.evaluationScoreMarch(evaluation);
            mScoreMarch.setText(StringHelper.appendWithDots(getString(R.string.card_label_score),
                    getResources().getQuantityString(R.plurals.scores, score, score)));

            mMarchResultIcon.setImageResource(
                    ImageHelper.getResultDrawable(
                            evaluation));

            // Static Balance
            int parallel = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS));
            mParallelFeet.setText(StringHelper.appendWithDots(getString(R.string.card_label_cbx_parallel_feet),
                    StringHelper.getClassificationText(getActivity(), parallel == Constant.RESULT_CHECKED)));

            int semiTandem = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM));
            mSemiTandem.setText(StringHelper.appendWithDots(getString(R.string.card_label_cbx_semi_tandem_position),
                    StringHelper.getClassificationText(getActivity(), semiTandem == Constant.RESULT_CHECKED)));

            int tandem = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_EE_TANDEM));
            mTandem.setText(StringHelper.appendWithDots(getString(R.string.card_label_cbx_tandem_position),
                    StringHelper.getClassificationText(getActivity(), tandem == Constant.RESULT_CHECKED)));

            evaluation = EvaluationPPHelper.evaluateStaticBalance(parallel == Constant.RESULT_CHECKED, semiTandem == Constant.RESULT_CHECKED, tandem == Constant.RESULT_CHECKED);
            score = EvaluationPPHelper.evaluationScoreStaticBalance(evaluation);
            mScoreStaticBalance.setText(StringHelper.appendWithDots(getString(R.string.card_label_score),
                    getResources().getQuantityString(R.plurals.scores, score, score)));

            mStaticBalanceResultIcon.setImageResource(
                    ImageHelper.getResultDrawable(evaluation));


            // Squads
            int squadRepetitions = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS));
            mSquadRepetitions.setText(StringHelper.appendWithDots(getString(R.string.card_label_speed_squat_quantity),
                    StringHelper.getClassificationText(getActivity(), squadRepetitions == Constant.RESULT_CHECKED)));


            int squadCount = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS));
            mSquadCount.setText(StringHelper.appendWithDots(getString(R.string.card_label_speed_squat_count),
                    String.valueOf(squadCount)));

            double squadTime = cursor.getDouble(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS));
            mSquadTime.setText(StringHelper.appendWithDots(getString(R.string.card_detail_time),
                    String.valueOf(squadTime),
                    getString(R.string.time_measurement_unit)));

            evaluation = EvaluationPPHelper.evaluateRising(squadTime, squadRepetitions == Constant.RESULT_CHECKED);
            score = EvaluationPPHelper.evaluationScoreRising(evaluation);
            mScoreSquad.setText(StringHelper.appendWithDots(getString(R.string.card_label_score),
                    getResources().getQuantityString(R.plurals.scores, score, score)));

            mSquadResultIcon.setImageResource(
                    ImageHelper.getResultDrawable(evaluation));

            // Grip Strength
            double left1 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1));
            double right1 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1));
            double left2 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2));
            double right2 = cursor.getInt(cursor
                    .getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2));

            double maxLeftGrip = Math.max(0, Math.max(left1, left2));
            double maxRightGrip = Math.max(0, Math.max(right1, right2));

            mLeftMaxGrip.setText(StringHelper.appendWithDots(getString(R.string.card_label_better_left_grip_strength),
                    String.valueOf(maxLeftGrip), getString(R.string.grip_strength_measurement_unit)));
            mRightMaxGrip.setText(StringHelper.appendWithDots(getString(R.string.card_label_better_right_grip_strength),
                    String.valueOf(maxRightGrip), getString(R.string.grip_strength_measurement_unit)));

            evaluation = EvaluationPPHelper.evaluateForce(
                    maxLeftGrip, maxRightGrip, mIsFemale);
            score = EvaluationPPHelper.evaluationScoreForce(evaluation);
            mScoreGripStrength.setText(StringHelper.appendWithDots(getString(R.string.card_label_score), getResources().getQuantityString(R.plurals.scores, score, score)));

            mGripStrengthResultIcon.setImageResource(
                    ImageHelper.getResultDrawable(evaluation));

            // General score
            mScore = cursor.getDouble(cursor.getColumnIndex(software.cneuro.neurogerdatabase.constant.Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL));

            setEvaluation();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void setEvaluation() {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.card_score)).setText(StringHelper.appendWithDots(
                    getString(R.string.card_label_score),
                    String.valueOf(mScore),
                    getString(R.string.score_unit)
            ));

            ((TextView) getView().findViewById(R.id.card_result)).setText(
                    EvaluationPPHelper.getEvaluationText(getActivity(),
                            EvaluationPPHelper.evaluateGeneral(mScore)));
        }
    }
}
