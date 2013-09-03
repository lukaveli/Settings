package xyz.jamework.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class PatternDialogPreference extends DialogPreference {

	public PatternDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			persistBoolean(true);
		}
		super.onDialogClosed(positiveResult);
	}

	public void perBool() {
		persistBoolean(false);
	}

}
