package xyz.jamework.settings;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import group.pals.android.lib.ui.lockpattern.prefs.SecurityPrefs;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class UserSettingActivity extends PreferenceActivity {

	private static final int REQ_CREATE_PATTERN = 1;
	private static final int REQ_ENTER_PATTERN = 2;
	private char[] savedPattern;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		if (sharedPreferences.getBoolean("prefPatternDialog", true)) {
			setListenerForDisabledPattern();
		}
		SecurityPrefs.setAutoSavePattern(this, true);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			if (resultCode == RESULT_OK) {
				setListenerForEnabledPattern(data);
			}
			break;
		}// REQ_CREATE_PATTERN
		case REQ_ENTER_PATTERN: {
			/*
			 * NOTE that there are 4 possible result codes!!!
			 */
			switch (resultCode) {
			case RESULT_OK:
				Intent intent = new Intent(
						LockPatternActivity.ACTION_CREATE_PATTERN, null,
						UserSettingActivity.this, LockPatternActivity.class);
				startActivityForResult(intent, REQ_CREATE_PATTERN);
				break;
			case RESULT_CANCELED:
				// The user cancelled the task
				break;
			case LockPatternActivity.RESULT_FAILED:
				// The user failed to enter the pattern
				break;
			case LockPatternActivity.RESULT_FORGOT_PATTERN:
				// The user forgot the pattern and invoked your recovery
				// Activity.
				break;
			}

			/*
			 * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
			 * the number of tries that the user did.
			 */
			int retryCount = data.getIntExtra(
					LockPatternActivity.EXTRA_RETRY_COUNT, 0);

			break;
		}// REQ_ENTER_PATTERN
		}
	}

	@SuppressWarnings("deprecation")
	public void setListenerForDisabledPattern() {
		Preference prefPattern = findPreference("prefPattern");
		prefPattern
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(
								LockPatternActivity.ACTION_CREATE_PATTERN,
								null, UserSettingActivity.this,
								LockPatternActivity.class);
						startActivityForResult(intent, REQ_CREATE_PATTERN);
						return false;
					}
				});
	}

	@SuppressWarnings("deprecation")
	private void setListenerForEnabledPattern(Intent data) {
		Preference prefPattern = findPreference("prefPattern");
		prefPattern.setTitle(R.string.pref_lock_pattern_change);
		prefPattern.setSummary(R.string.pref_lock_pattern_summary_change);
		savedPattern = data
				.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
		prefPattern
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(
								LockPatternActivity.ACTION_COMPARE_PATTERN,
								null, UserSettingActivity.this,
								LockPatternActivity.class);
						intent.putExtra(LockPatternActivity.EXTRA_PATTERN,
								savedPattern);
						startActivityForResult(intent, REQ_ENTER_PATTERN);
						return false;
					}
				});
		PatternDialogPreference pref = (PatternDialogPreference) findPreference("prefPatternDialog");
		pref.perBool();
	}

}
