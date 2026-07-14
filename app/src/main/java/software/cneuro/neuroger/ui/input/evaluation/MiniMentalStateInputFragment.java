package software.cneuro.neuroger.ui.input.evaluation;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.EvaluationMMHelper;
import software.cneuro.neuroger.content.ImageHelper;
import software.cneuro.neuroger.content.MMSEHelper;
import software.cneuro.neuroger.content.StringHelper;
import software.cneuro.neuroger.ui.dialog.HelpFragment;
import software.cneuro.neuroger.ui.input.BaseWarningFragment;
import software.cneuro.neurogerdatabase.database.DatabaseInserter;
import software.cneuro.neurogerdatabase.database_async.InsertMinimentalTest_AsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MiniMentalStateInputFragment extends BaseWarningFragment implements
        View.OnClickListener,
        View.OnFocusChangeListener,
        InsertMinimentalTest_AsyncTask.OnMinimentalTestInserted,
        CompoundButton.OnCheckedChangeListener {
    public static final String ARG_SUBJECT_ID = "subject_id";

    private long mSubjectId;

    private ViewGroup mCard1View1, mCard1View2;
    private ViewGroup mCard2View;
    private ViewGroup mCard3View;
    private ViewGroup mCard4View;
    private ViewGroup mCard5View1, mCard5View2;
    private ViewGroup mCard6View;
    private RadioGroup mRgMath;
    private EditText mEditSpelling;

    private LayoutInflater mInflater;

    private EditText mCurrentOnFocusEditText;
    private int mCurrentOnFocusEditTextPosition;

    private SparseIntArray mMathValues;

    public MiniMentalStateInputFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_SUBJECT_ID)) {
                mSubjectId = getArguments().getLong(ARG_SUBJECT_ID);
            }
        }

        assert getActivity() != null;
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mMathValues = new SparseIntArray();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mini_mental_state_input, container, false);

        // Orientation
        mCard1View1 = rootView.findViewById(R.id.detail_card_1_container_1);
        mCard1View2 = rootView.findViewById(R.id.detail_card_1_container_2);

        // Retention
        mCard2View = rootView.findViewById(R.id.detail_card_2_container);

        // Math
        mCard3View = rootView.findViewById(R.id.detail_card_3_container);

        mRgMath = rootView.findViewById(R.id.math_attention_radioGroup_test_selection);

        mEditSpelling = rootView.findViewById(R.id.edit_word_spelling);

        // Reminder
        mCard4View = rootView.findViewById(R.id.detail_card_4_container);

        // Language
        mCard5View1 = rootView.findViewById(R.id.detail_card_5_container_1);
        // Order 3
        mCard5View2 = rootView.findViewById(R.id.detail_card_5_container_2);

        // Design
        mCard6View = rootView.findViewById(R.id.detail_card_6_container);

        // Help icons
        // Needed cause when clicked in temporal also was clicked in spatial => 2 dialogs were showing at once.
        rootView.findViewById(R.id.card_orientation_temp_help_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp(Constant.HELP_ORIENTATION_TEMPORAL);
            }
        });
        rootView.findViewById(R.id.card_orientation_spatial_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_retention_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_math_attention_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_reminder_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_order_in_3_help_icon).setOnClickListener(this);
        rootView.findViewById(R.id.card_design_help_icon).setOnClickListener(this);

        // Image help icon
        rootView.findViewById(R.id.card_design_image_help_icon).setOnClickListener(this);

        // save button
        rootView.findViewById(R.id.btn_action).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(ARG_SUBJECT_ID, mSubjectId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSubjectId = savedInstanceState.getLong(ARG_SUBJECT_ID);
        }

        updateContent();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ViewGroup parent = (ViewGroup) buttonView.getParent();
        parent.findViewById(R.id.card_input_edittext).setEnabled(!isChecked);
    }

    @Override
    public void onMinimentalTestInserted(long id) {
        assert getActivity() != null;

        Toast.makeText(getActivity(), getString(R.string.toast_saved_test), Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.card_orientation_spatial_help_icon:
                showHelp(Constant.HELP_ORIENTATION_SPATIAL);
                break;
            case R.id.card_retention_help_icon:
                showHelp(Constant.HELP_RETENTION);
                break;
            case R.id.card_math_attention_help_icon:
                showHelp(Constant.HELP_MATH_ATTENTION);
                break;
            case R.id.card_reminder_help_icon:
                showHelp(Constant.HELP_REMINDER);
                break;
            case R.id.card_order_in_3_help_icon:
                showHelp(Constant.HELP_ORDER);
                break;
            case R.id.card_design_help_icon:
                showHelp(Constant.HELP_DESIGN);
                break;
            case R.id.card_help_icon:
                String position = v.getTag().toString();
                showHelp(getHelp(Integer.parseInt(position)));
                break;
            case R.id.card_image_help_icon:
                position = v.getTag().toString();
                showImageHelp(getHelp(Integer.parseInt(position)));
                break;
            case R.id.card_design_image_help_icon:
                showImageHelp(Constant.HELP_DESIGN);
                break;
            case R.id.btn_action:
                showCheckDateTestBeforeLeavingMessage();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText) {
            // When changed the edittext remove the listener from the edittext selected before.
            // Make sure only one edittext has a textwatcher listener.
            // The one we are working with.
            if (!hasFocus && mCurrentOnFocusEditText != null &&
                    mCurrentOnFocusEditText.getTag().toString().equals(v.getTag().toString())) {
                mCurrentOnFocusEditText.removeTextChangedListener(mTextWatcher);
            } else if (hasFocus) {
                mCurrentOnFocusEditText = (EditText) v;
                mCurrentOnFocusEditTextPosition = Integer.parseInt(mCurrentOnFocusEditText.getTag().toString());
                (mCurrentOnFocusEditText).addTextChangedListener(mTextWatcher);
            }
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate(s.toString(), mCurrentOnFocusEditTextPosition);
        }
    };

    @Override
    public void onPositiveAnswer(int dialogId) {
        if (dialogId == CHECK_TEST_BEFORE_LEAVING_DIALOG_ID)
            saveContent();
    }

    @Override
    public void onNegativeAnswer(int dialogId) {

    }

    private void updateContent() {
        if (getContext() == null) return;

        // Orientation
        String[] array = MMSEHelper.getOrientationTemporal(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard1View1, array[i], i, false);
        }
        array = MMSEHelper.getOrientationSpatial(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard1View2, array[i], i, false);
        }

        // Retention
        array = MMSEHelper.getRetentionInfo(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard2View, array[i], i, false);
        }

        // Math Attention
        array = MMSEHelper.getMathAttention(getContext());
        int mathValue = EvaluationMMHelper.INIT_MATH_DEFAULT_VALUE;
        for (int i = 0; i < array.length; i++) {
            mathValue -= EvaluationMMHelper.MATH_SUBTRACTION_DEFAULT_VALUE;
            mMathValues.put(i, mathValue);
            addNewMathInfo(mCard3View, array[i], i, String.valueOf(mathValue));
        }

        // Reminder
        array = MMSEHelper.getReminder(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard4View, array[i], i, false);
        }

        // Language
        array = MMSEHelper.getLanguage(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewLanguageInfo(mCard5View1, array[i], i, i == 0 || i == 1 || i == 3);
        }
        array = MMSEHelper.getOrderIn3(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard5View2, array[i], i, false);
        }

        // Design
        array = MMSEHelper.getDesign(getContext());
        for (int i = 0; i < array.length; i++) {
            addNewInfo(mCard6View, array[i], i, false);
        }

    }

    private void addNewInfo(ViewGroup container, String text, int position, boolean help) {
        View infoView = mInflater.inflate(!help ? R.layout.input_checkbox : R.layout.input_checkbox_icon,
                container, false);

        CheckBox checkBox = infoView.findViewById(R.id.card_input_cbx);
        checkBox.setText(text);
        checkBox.setTag(position);

        if (help) {
            ImageButton helBtn = infoView.findViewById(R.id.card_help_icon);
            helBtn.setTag(position);
            helBtn.setOnClickListener(this);
        }

        container.addView(infoView);
    }

    private void addNewLanguageInfo(ViewGroup container, String text, int position, boolean imageHelp) {
        View infoView = mInflater.inflate(imageHelp ? R.layout.input_checkbox_2_icon :
                R.layout.input_checkbox_icon, container, false);

        CheckBox checkBox = infoView.findViewById(R.id.card_input_cbx);
        checkBox.setText(text);
        checkBox.setTag(position);

        ImageButton helBtn = infoView.findViewById(R.id.card_help_icon);
        helBtn.setTag(position);
        helBtn.setOnClickListener(this);

        if (imageHelp) {
            ImageButton imageBtn = infoView.findViewById(R.id.card_image_help_icon);
            imageBtn.setTag(position);
            imageBtn.setOnClickListener(this);
        }

        container.addView(infoView);
    }

    private void addNewMathInfo(ViewGroup container, String text, int position, String mathValue) {
        View infoView = mInflater.inflate(R.layout.input_textview_edittext_icon,
                container, false);

        TextView textView = infoView.findViewById(R.id.card_input_tv);
        textView.setText(text);
        textView.setTag(position);

        EditText editText = infoView.findViewById(R.id.card_input_edittext);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTag(position);

        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setOnFocusChangeListener(this);

        TextView textView1 = infoView.findViewById(R.id.card_help_tv);
        textView1.setText(mathValue);
        textView1.setTag(position);

        container.addView(infoView);
    }

    private void calculate(String s, int from) {
        int mathValue = EvaluationMMHelper.getValidValue(s);
        for (int i = from; i < mCard3View.getChildCount(); i++) {
            ViewGroup childView = (ViewGroup) mCard3View.getChildAt(i);
            TextView textView = childView.findViewById(R.id.card_help_tv);
            if (i != from) {
                mathValue -= EvaluationMMHelper.MATH_SUBTRACTION_DEFAULT_VALUE;
            }
            textView.setText(String.valueOf(mathValue));
        }
    }

    private int getHelp(int position) {
        int help = Constant.HELP_LANGUAGE_1;
        switch (position) {
            case 1:
                help = Constant.HELP_LANGUAGE_2;
                break;
            case 2:
                help = Constant.HELP_LANGUAGE_3;
                break;
            case 3:
                help = Constant.HELP_LANGUAGE_4;
                break;
            case 4:
                help = Constant.HELP_LANGUAGE_5;
                break;
        }
        return help;
    }

    private void saveContent() {
        ArrayList<Integer> temp_orientation_chk = getSelection(mCard1View1);
        ArrayList<Integer> spat_orientation_chk = getSelection(mCard1View2);
        ArrayList<Integer> ret_reg_info_chk = getSelection(mCard2View);
        ArrayList<Integer> calculation_chk = getMathSelection(mCard3View);
        ArrayList<Integer> memory_chk = getSelection(mCard4View);
        ArrayList<Integer> language_chk = getSelection(mCard5View1);
        ArrayList<Integer> language_order_chk = getSelection(mCard5View2);
        ArrayList<Integer> design_chk = getSelection(mCard6View);

        int flag = mRgMath.getCheckedRadioButtonId() == R.id.card_math_attention_rdb_1 ?
                Constant.MATH_COUNTING_TEST : Constant.MATH_BACKWARDS_SPELLING_TEST;
        String word = mEditSpelling.getText().toString();

        int score = temp_orientation_chk.size()
                + spat_orientation_chk.size()
                + ret_reg_info_chk.size()
                + memory_chk.size()
                + language_chk.size()
                + language_order_chk.size()
                + design_chk.size();

        if (flag == Constant.MATH_COUNTING_TEST)
            score += EvaluationMMHelper.getMathCount(calculation_chk);
        else
            score += EvaluationMMHelper.getSpellingCount(word);

        DatabaseInserter.insertMinimentalTest(
                getContext(),
                mSubjectId,
                StringHelper.getJsonString(temp_orientation_chk),
                StringHelper.getJsonString(spat_orientation_chk),
                StringHelper.getJsonString(ret_reg_info_chk),
                flag,
                flag == Constant.MATH_COUNTING_TEST ? StringHelper.getJsonString(calculation_chk) : word,
                StringHelper.getJsonString(memory_chk),
                StringHelper.getJsonString(language_chk),
                StringHelper.getJsonString(language_order_chk),
                StringHelper.getJsonString(design_chk),
                score,
                this
        );
    }

    private ArrayList<Integer> getMathSelection(ViewGroup parent) {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            ViewGroup childView = (ViewGroup) parent.getChildAt(i);
            EditText editText = childView.findViewById(R.id.card_input_edittext);

            result.add(EvaluationMMHelper.getValidValue(editText.getText().toString()));
        }

        return result;
    }

    private ArrayList<Integer> getSelection(ViewGroup parent) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View container = parent.getChildAt(i);
            CheckBox cbx = container.findViewById(R.id.card_input_cbx);
            if (cbx.isChecked()) {
                result.add(EvaluationMMHelper.getValidValue(cbx.getTag().toString()));
            }
        }
        return result;
    }

    private void showHelp(int helpID) {
        assert getActivity() != null;
        String text = MMSEHelper.getMiniMentalStateHelpText(getActivity(), helpID);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        HelpFragment dialogFragment = HelpFragment.newInstance(
                getString(R.string.dialog_help_title),
                text);
        dialogFragment.show(fm, "fragment_dialog");
    }

    private void showImageHelp(int helpID) {
        assert getActivity() != null;
        int imageHelpResourceId = ImageHelper.getMiniMentalStateImageHelpResourceId(helpID);
        Intent intent = new Intent(getActivity(), StimulusImageActivity.class);
        intent.putExtra(ImageFragment.ARG_IMAGE_RESOURCE_ID, imageHelpResourceId);
        startActivity(intent);
    }
}
