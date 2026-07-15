package software.cneuro.neurogertheme;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomActionView extends LinearLayout {

	public CustomActionView(final Context context) {
		this(context, null);
	}

	public CustomActionView(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					software.cneuro.neurogertheme.R.styleable.CustomActionView);

			setOrientation(LinearLayout.HORIZONTAL);
			setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(software.cneuro.neurogertheme.R.layout.custom_action_view, this, true);

			Drawable drawable = a
					.getDrawable(software.cneuro.neurogertheme.R.styleable.CustomActionView_srcAction);
			if (drawable != null) {
				ImageView image = (ImageView) getChildAt(0);
				image.setImageDrawable(drawable);
			}

			String fontName = a
					.getString(software.cneuro.neurogertheme.R.styleable.CustomActionView_fontAction);
			String text = a.getString(software.cneuro.neurogertheme.R.styleable.CustomActionView_textAction);

			TextViewFont textView = (TextViewFont) getChildAt(1);
			if (text != null) {
				textView.setText(text);
			}
			if (fontName != null) {

				Typeface myTypeface = Typeface.createFromAsset(getContext()
						.getAssets(), fontName);
				textView.setTypeface(myTypeface);
			}

			textView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			textView.setGravity(Gravity.CENTER);

			a.recycle();
		}
	}
}
