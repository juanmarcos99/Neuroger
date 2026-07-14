package software.cneuro.neuroger.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;
import software.cneuro.neuroger.content.SubjectHelper;

public class BaseSubjectInfoActivity extends AppCompatActivity {
    public static final String ARG_SUBJECT_ID = "subject_id";
    public static final String ARG_SUBJECT_NAME = "name";
    public static final String ARG_SUBJECT_LAST_NAME = "last_name";
    public static final String ARG_SUBJECT_PHONE = "phone";
    public static final String ARG_SUBJECT_AGE = "age";
    public static final String ARG_SUBJECT_BIRTH = "birth";
    public static final String ARG_SUBJECT_GENDER = "gender";
    public static final String ARG_SUBJECT_LEVEL_OF_SCHOOLING = "level_of_schooling";
    public static final String ARG_COMPENSATED = "compensated";

    protected long mSubjectId;
    protected String mFullName;
    protected String mName, mLastName;
    protected int mGender;
    protected int mAge;
    protected int mLevelOfSchooling;
    protected int mCompensated;

    protected void updateSubject(Bundle intent) {
        mSubjectId = intent.getLong(ARG_SUBJECT_ID, Constant.NO_ID);

        mName = intent.getString(ARG_SUBJECT_NAME);
        mFullName = SubjectHelper.getName(intent.getString(ARG_SUBJECT_NAME),
                intent.getString(ARG_SUBJECT_LAST_NAME), false);
        ((TextView) findViewById(R.id.subject_detail_name)).setText(SubjectHelper.getName(
                intent.getString(ARG_SUBJECT_NAME),
                intent.getString(ARG_SUBJECT_LAST_NAME), true));

        mGender = intent.getInt(ARG_SUBJECT_GENDER);
        mAge = intent.getInt(ARG_SUBJECT_AGE);
        mLevelOfSchooling = intent.getInt(ARG_SUBJECT_LEVEL_OF_SCHOOLING);
        mCompensated = intent.getInt(ARG_COMPENSATED, Constant.SUBJECT_COMPENSATED_ID);
    }
}
