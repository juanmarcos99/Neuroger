package software.cneuro.neurogertheme;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class DontPressWithParentImageButton extends AppCompatImageButton {
	public DontPressWithParentImageButton(Context context) {
		this(context, null, 0);
	}

	public DontPressWithParentImageButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DontPressWithParentImageButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View
				&& ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}
}
