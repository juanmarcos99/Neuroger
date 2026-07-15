package software.cneuro.neurogertheme;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

/**
 * Created by klaudia on 7/6/2015.
 */
public class RadioButtonFont extends AppCompatRadioButton {
    public RadioButtonFont(Context context) {
        super(context);
    }

    public RadioButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.TextViewFont);
            String fontName = a.getString(R.styleable.TextViewFont_myFont);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

    public RadioButtonFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.TextViewFont);
            String fontName = a.getString(R.styleable.TextViewFont_myFont);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadioButtonFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.TextViewFont);
            String fontName = a.getString(R.styleable.TextViewFont_myFont);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
