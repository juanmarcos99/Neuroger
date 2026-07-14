package software.cneuro.neuroger.ui.input.evaluation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.CalendarHelper;
import software.cneuro.neuroger.content.EvaluationPPHelper;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertPathologicalAnt_AsyncTask;
import software.cneuro.neurogerdatabase.database_async.InsertTestPP_AsyncTask;
import software.cneuro.neurogertheme.chronometer.ChronometerWithMilliseconds;

public class PhysicalPerformanceInputFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        InsertTestPP_AsyncTask.OnTestPPInserted,
        InsertPathologicalAnt_AsyncTask.OnPathologicalAntInserted {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_IS_FEMALE = "is_female";

    private long mSubjectId;
    private boolean mIsFemale;

    private double mLeftMaxGrip, mRightMaxGrip;
    private int mEvalScoreMarch, mEvalScoreStaticBalance, mEvalScoreRising, mEvalScoreForce;
    private boolean mIsMarchChronometerRunning1, mIsMarchChronometerRunning2, mIsStaticBalanceChronometerRunning, mIsSquadChronometerRunning;
    private double mMarchTime;
    private int mMarchSteps;

    private MediaPlayer mPlayer;

    private View mStaticBalanceChronoContainer;

    private ChronometerWithMilliseconds mMarchChronometer1, mMarchChronometer2, mStaticBalanceChronometer,mSquadChronometer;
    private TextView mResultIMC, mResultVelocity, mResultAmplitude, mResultFrequency;
    private TextView mScoreMarch, mScoreStaticBalance, mScoreSquad, mScoreGripStrength;
    private EditText mSize, mWeight, mMarchSteps1, mMarchSteps2, mSquadCount, mLeftHandStrength1, mRightHandStrength1, mLeftHandStrength2, mRightHandStrength2;
    private CheckBox mCbxParallelFeet, mCbxPosSemiTandem, mCbxPosTandem, mCbxSquad;
    private ImageView mIMCResultIcon, mMarchResultIcon, mStaticBalanceResultIcon, mSquadResultIcon, mGripStrengthResultIcon;

    private OnPhysicalPerformanceInteractionListener mListener;
    private ImageButton mBtnStaticBalanceChronometer, mBtnSquadChronometer;

    public PhysicalPerformanceInputFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_physical_performance_input, container, false);

        // textview
        mResultIMC = rootView.findViewById(R.id.card_result_imc);
        mResultVelocity = rootView.findViewById(R.id.card_result_march_velocity);
        mResultAmplitude = rootView.findViewById(R.id.card_result_march_amplitude);
        mResultFrequency = rootView.findViewById(R.id.card_result_march_frequency);

        mScoreMarch = rootView.findViewById(R.id.card_result_march_score);
        mScoreStaticBalance = rootView.findViewById(R.id.card_result_static_balance_score);
        mScoreSquad = rootView.findViewById(R.id.card_result_squad_score);
        mScoreGripStrength = rootView.findViewById(R.id.card_result_grip_strength_score);

        // edittext
        mSize = rootView.findViewById(R.id.card_edit_imc_size);
        mWeight = rootView.findViewById(R.id.card_edit_imc_weight);
        mMarchSteps1 = rootView.findViewById(R.id.card_edit_march_steps_1);
        mMarchSteps2 = rootView.findViewById(R.id.card_edit_march_steps_2);
        mSquadCount = rootView.findViewById(R.id.card_edit_squad_count);
        mLeftHandStrength1 = rootView.findViewById(R.id.card_edit_grip_strength_left_hand_1);
        mRightHandStrength1 = rootView.findViewById(R.id.card_edit_grip_strength_right_hand_1);
        mLeftHandStrength2 = rootView.findViewById(R.id.card_edit_grip_strength_left_hand_2);
        mRightHandStrength2 = rootView.findViewById(R.id.card_edit_grip_strength_right_hand_2);

        // result images
        mIMCResultIcon = rootView.findViewById(R.id.card_result_imc_icon);
        mMarchResultIcon = rootView.findViewById(R.id.card_result_march_icon);
        mStaticBalanceResultIcon = rootView.findViewById(R.id.card_result_static_balance_icon);
        mSquadResultIcon = rootView.findViewById(R.id.card_result_squad_icon);
        mGripStrengthResultIcon = rootView.findViewById(R.id.card_result_grip_strength_icon);

        // checkboxes
        mCbxParallelFeet = rootView.findViewById(R.id.card_cbx_parallel_feet);
        mCbxParallelFeet.setOnCheckedChangeListener(this);
        mCbxPosSemiTandem = rootView.findViewById(R.id.card_cbx_semi_tantem_position);
        mCbxPosSemiTandem.setOnCheckedChangeListener(this);
        mCbxPosTandem = rootView.findViewById(R.id.card_cbx_tantem_position);
        mCbxPosTandem.setOnCheckedChangeListener(this);

        mCbxSquad = rootView.findViewById(R.id.card_cbx_squad_repetitions);
        mCbxSquad.setOnCheckedChangeListener(this);

        // chronometers
        mMarchChronometer1 = rootView.findViewById(R.id.card_chronometer_march_time_1);
        mMarchChronometer2 = rootView.findViewById(R.id.card_chronometer_march_time_2);

        mStaticBalanceChronoContainer = rootView.findViewById(R.id.static_balance_result_container);
        mStaticBalanceChronometer = rootView.findViewById(R.id.card_chronometer_static_balance);
        mStaticBalanceChronometer.setOnChronometerTickListener(new ChronometerWithMilliseconds.OnChronometerTickListener() {
            public void onChronometerTick(ChronometerWithMilliseconds chronometer) {
                String[] temp = chronometer.getText().toString().split(":");
                int seconds = Integer.parseInt(temp[1]);
                if (seconds >= 10) { //When reaches 10 seconds.

                    //Define here what happens when the Chronometer reaches the time above.
                    mStaticBalanceChronoContainer.setBackgroundColor(getResources().getColor(R.color.physical_performance_result_background));
                    chronometer.stop();

                    // Play sound.
                    startPlayer();

                    mBtnStaticBalanceChronometer.setEnabled(true);
                    mIsStaticBalanceChronometerRunning = false;
                }
            }
        });

        mSquadChronometer = rootView.findViewById(R.id.card_chronometer_squad_time);

        // help buttons
        //rootView.findViewById(R.id.card_imc_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_march_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_static_balance_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_speed_squat_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_grip_strength_help_icon).setOnClickListener(this);

        // calculate buttons
        rootView.findViewById(R.id.card_btn_calculate_imc).setOnClickListener(this);
        rootView.findViewById(R.id.card_btn_calculate_march_1).setOnClickListener(this);
        rootView.findViewById(R.id.card_btn_calculate_march_2).setOnClickListener(this);
        rootView.findViewById(R.id.card_btn_calculate_squad).setOnClickListener(this);
        rootView.findViewById(R.id.card_btn_calculate_right_hand_grip_strength).setOnClickListener(this);

        // chronometer buttons
        rootView.findViewById(R.id.card_btn_chronometer_march_1).setOnClickListener(this);
        rootView.findViewById(R.id.card_btn_chronometer_march_2).setOnClickListener(this);

        mBtnStaticBalanceChronometer = rootView.findViewById(R.id.card_btn_chronometer_static_balance);
        mBtnStaticBalanceChronometer.setOnClickListener(this);

        mBtnSquadChronometer = rootView.findViewById(R.id.card_btn_chronometer_squad);
        mBtnSquadChronometer.setOnClickListener(this);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    private void startPlayer() {
        mPlayer = MediaPlayer.create(getActivity(), R.raw.blip2);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releasePlayer();
            }
        });
        mPlayer.start();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            try {
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.card_cbx_squad_repetitions) {
            mSquadChronometer.setEnabled(isChecked);
            mBtnSquadChronometer.setEnabled(mCbxSquad.isChecked());
        } else {
            int evaluation = EvaluationPPHelper.evaluateStaticBalance(
                    mCbxParallelFeet.isChecked(), mCbxPosSemiTandem.isChecked(), mCbxPosTandem.isChecked());
            mEvalScoreStaticBalance = EvaluationPPHelper.evaluationScoreStaticBalance(evaluation);

            mStaticBalanceResultIcon.setImageResource(
                    ImageHelper.getResultDrawable(evaluation));
            mScoreStaticBalance.setText(mEvalScoreStaticBalance + " " + getString(R.string.score_unit));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_march_help_icon:
                mListener.showHelp(Constant.HELP_MARCH);
                break;
            case R.id.card_static_balance_help_icon:
                mListener.showHelp(Constant.HELP_STATIC_BALANCE);
                break;
            case R.id.card_speed_squat_help_icon:
                mListener.showHelp(Constant.HELP_SQUAD);
                break;
            case R.id.card_grip_strength_help_icon:
                mListener.showHelp(Constant.HELP_GRIP_STRENGTH);
                break;
            case R.id.card_btn_chronometer_march_1:
                if (!mIsMarchChronometerRunning2)
                    mIsMarchChronometerRunning1 = isStartedChronometer(mMarchChronometer1, mIsMarchChronometerRunning1);
                break;
            case R.id.card_btn_chronometer_march_2:
                if (!mIsMarchChronometerRunning1)
                    mIsMarchChronometerRunning2 = isStartedChronometer(mMarchChronometer2, mIsMarchChronometerRunning2);
                break;
            case R.id.card_btn_chronometer_static_balance:
                mStaticBalanceChronometer.setText("00:00:0");
                isStartedStaticBalanceChronometer(mStaticBalanceChronometer, mIsStaticBalanceChronometerRunning);
                break;
            case R.id.card_btn_chronometer_squad:
                mIsSquadChronometerRunning = isStartedChronometer(mSquadChronometer, mIsSquadChronometerRunning);
                break;
            case R.id.card_btn_calculate_imc:
                if (validateIMCEntries()) {
                    calculateIMC();
                }
                break;
            case R.id.card_btn_calculate_march_1:
                if (validateMarchEntries(true)) {
                    mIsMarchChronometerRunning1 = stopChronometer(mMarchChronometer1, mIsMarchChronometerRunning1);
                    calculateMarch(true);
                }
                break;
            case R.id.card_btn_calculate_march_2:
                if (validateMarchEntries(false)) {
                    mIsMarchChronometerRunning2 = stopChronometer(mMarchChronometer2, mIsMarchChronometerRunning2);
                    calculateMarch(false);
                }
                break;
            case R.id.card_btn_calculate_squad:
                if (validateSquadEntries()) {
                    mIsSquadChronometerRunning = stopChronometer(mSquadChronometer, mIsSquadChronometerRunning);
                    calculateSquad();
                }
                break;
            case R.id.card_btn_calculate_right_hand_grip_strength:
                if (validateGripStrengthEntries()) {
                    calculateGripStrength();
                }
                break;
            case R.id.btn_action:
                if (mListener != null) {
                    mListener.onSaveContent();
                }
                break;
        }
    }

    private void isStartedStaticBalanceChronometer(ChronometerWithMilliseconds chronometer, boolean running) {
        if (running) {
        } else {
            mStaticBalanceChronoContainer.setBackgroundColor(getResources().getColor(R.color.primary));
            mBtnStaticBalanceChronometer.setEnabled(false);
            mIsStaticBalanceChronometerRunning = true;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    private boolean stopChronometer(ChronometerWithMilliseconds chronometer, boolean running) {
        if (running) {
            chronometer.stop();
        }
        return false;
    }

    private boolean isStartedChronometer(ChronometerWithMilliseconds chronometer, boolean running) {
        if (running) {
            chronometer.stop();
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
        return !running;
    }

    private void calculateIMC() {
        double size = EvaluationPPHelper.zeroIfEmptyDouble(mSize.getText().toString());
        double weight = EvaluationPPHelper.zeroIfEmptyDouble(mWeight.getText().toString());

        double imc = EvaluationPPHelper.imc(size, weight);
        mResultIMC.setText(StringHelper.appendWithDots(getString(R.string.card_label_imc_large), StringHelper.truncate(imc)));

        int evaluation = EvaluationPPHelper.evaluateIMC(imc);

        mIMCResultIcon.setImageResource(
                ImageHelper.getImcResultDrawable(
                        evaluation));
    }

    private void calculateMarch(boolean firstMarch) {
        if (firstMarch) {
            mMarchTime = EvaluationPPHelper.zeroIfEmptyDouble(CalendarHelper.getDurationInSeconds(mMarchChronometer1));
            mMarchSteps = EvaluationPPHelper.zeroIfEmpty(mMarchSteps1.getText().toString());
        } else {
            mMarchTime = EvaluationPPHelper.zeroIfEmptyDouble(CalendarHelper.getDurationInSeconds(mMarchChronometer2));
            mMarchSteps = EvaluationPPHelper.zeroIfEmpty(mMarchSteps2.getText().toString());
        }

        double velocity = EvaluationPPHelper.walkSpeed(mMarchTime);
        mResultVelocity.setText(StringHelper.appendMeasureUnit(
                StringHelper.truncate(velocity), getString(R.string.velocity_measurement_unit)));

        double amplitude = EvaluationPPHelper.averageStepExtend(mMarchSteps);
        mResultAmplitude.setText(StringHelper.appendMeasureUnit(
                StringHelper.truncate(amplitude), getString(R.string.amplitude_measurement_unit)));

        double frequency = EvaluationPPHelper.rateMinute(mMarchTime, mMarchSteps);
        mResultFrequency.setText(StringHelper.appendMeasureUnit(
                StringHelper.truncate(frequency), getString(R.string.frequency_measurement_unit)));

        int evaluation = EvaluationPPHelper.evaluateMarch(mMarchTime);
        mEvalScoreMarch = EvaluationPPHelper.evaluationScoreMarch(evaluation);

        mMarchResultIcon.setImageResource(
                ImageHelper.getResultDrawable(
                        evaluation));
        mScoreMarch.setText(mEvalScoreMarch + " " + getString(R.string.score_unit));
    }

    private void calculateSquad() {
        int evaluation = EvaluationPPHelper.evaluateRising(
                EvaluationPPHelper.zeroIfEmptyDouble(CalendarHelper.getDurationInSeconds(mSquadChronometer)), mCbxSquad.isChecked());
        mEvalScoreRising = EvaluationPPHelper.evaluationScoreRising(evaluation);

        mSquadResultIcon.setImageResource(
                ImageHelper.getResultDrawable(evaluation));
        mScoreSquad.setText(mEvalScoreRising + " " + getString(R.string.score_unit));

    }

    private void calculateGripStrength() {
        double left1 = EvaluationPPHelper.zeroIfEmptyDouble(mLeftHandStrength1.getText().toString());
        double right1 = EvaluationPPHelper.zeroIfEmptyDouble(mRightHandStrength1.getText().toString());
        double left2 = EvaluationPPHelper.zeroIfEmptyDouble(mLeftHandStrength2.getText().toString());
        double right2 = EvaluationPPHelper.zeroIfEmptyDouble(mRightHandStrength2.getText().toString());

        mLeftMaxGrip = Math.max(mLeftMaxGrip, Math.max(left1, left2));
        mRightMaxGrip = Math.max(mRightMaxGrip, Math.max(right1, right2));

        int evaluation = EvaluationPPHelper.evaluateForce(
                mLeftMaxGrip, mRightMaxGrip, mIsFemale);
        mEvalScoreForce = EvaluationPPHelper.evaluationScoreForce(evaluation);

        mGripStrengthResultIcon.setImageResource(
                ImageHelper.getResultDrawable(
                        evaluation));
        mScoreGripStrength.setText(mEvalScoreForce + " " + getString(R.string.score_unit));
    }

    private boolean validateIMCEntries() {
        if (TextUtils.isEmpty(mSize.getText())) {
            mSize.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mWeight.getText())) {
            mWeight.setError(getString(R.string.toast_invalid_value));
            return false;
        }
        return true;
    }

    private boolean validateMarchEntries(boolean firstMarch) {
        if (firstMarch) {
            if (TextUtils.isEmpty(mMarchChronometer1.getText())) {
                mMarchChronometer1.setError(getString(R.string.toast_invalid_value));
                return false;
            } else if (TextUtils.isEmpty(mMarchSteps1.getText())) {
                mMarchSteps1.setError(getString(R.string.toast_invalid_value));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(mMarchChronometer2.getText())) {
                mMarchChronometer2.setError(getString(R.string.toast_invalid_value));
                return false;
            } else if (TextUtils.isEmpty(mMarchSteps2.getText())) {
                mMarchSteps2.setError(getString(R.string.toast_invalid_value));
                return false;
            }
        }
        return true;
    }

    private boolean validateSquadEntries() {
        if (TextUtils.isEmpty(mSquadChronometer.getText())) {
            mSquadChronometer.setError(getString(R.string.toast_invalid_value));
            return false;
        }
        return true;
    }

    private boolean validateGripStrengthEntries() {
        if (TextUtils.isEmpty(mRightHandStrength1.getText())) {
            mRightHandStrength1.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mRightHandStrength2.getText())) {
            mRightHandStrength2.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mLeftHandStrength1.getText())) {
            mLeftHandStrength1.setError(getString(R.string.toast_invalid_value));
            return false;
        } else if (TextUtils.isEmpty(mLeftHandStrength2.getText())) {
            mLeftHandStrength2.setError(getString(R.string.toast_invalid_value));
            return false;
        }
        return true;
    }

    private void showDialog(int helpId) {
        FragmentManager fm = getChildFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_evaluation_title),
                StringHelper.getHelpText(getActivity(), helpId));
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    public void saveContent() {
        // General score
        int score = EvaluationPPHelper.evaluationScoreGeneral(mEvalScoreMarch, mEvalScoreStaticBalance, mEvalScoreRising, mEvalScoreForce);
        // General evaluation
        int evaluation = EvaluationPPHelper.evaluateGeneral(score);

        DatabaseInserter.insertTestPP(getActivity(),
                mSubjectId,
                mMarchTime,
                mMarchSteps,
                mCbxParallelFeet.isChecked() ? Constant.RESULT_CHECKED : Constant.RESULT_NOT_CHECKED,
                mCbxPosSemiTandem.isChecked() ? Constant.RESULT_CHECKED : Constant.RESULT_NOT_CHECKED,
                mCbxPosTandem.isChecked() ? Constant.RESULT_CHECKED : Constant.RESULT_NOT_CHECKED,
                EvaluationPPHelper.zeroIfEmptyDouble(CalendarHelper.getDurationInSeconds(mSquadChronometer)),
                mCbxSquad.isChecked() ? Constant.RESULT_CHECKED : Constant.RESULT_NOT_CHECKED,
                mCbxSquad.isChecked() ? Constant.MAX_SQUADS_REPETITIONS : EvaluationPPHelper.zeroIfEmpty(mSquadCount.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mRightHandStrength1.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mLeftHandStrength1.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mRightHandStrength2.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mLeftHandStrength2.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mWeight.getText().toString()),
                EvaluationPPHelper.zeroIfEmptyDouble(mSize.getText().toString()),
                evaluation,
                score,
                this);
    }

    @Override
    public void onTestPPInserted(long performanceId) {
        // DatabaseInserter.insertPathologicalAnt(getActivity(), mSubjectId, performanceId, mPathoAntSelected, PhysicalPerformanceInputFragment.this);

        if (mListener != null) {
            mListener.onSaved();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnPhysicalPerformanceInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement OnPhysicalPerformanceInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPathologicalAntInserted() {
        mListener.onSaved();
    }

    public interface OnPhysicalPerformanceInteractionListener {
        void showHelp(int helpID);

        void onSaveContent();

        void onSaved();
    }

}
