package software.cneuro.neurogertheme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

public class ButtonFont extends AppCompatButton {
	public ButtonFont(final Context context) {
		this(context, null);
	}

	public ButtonFont(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ButtonFont(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);

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

