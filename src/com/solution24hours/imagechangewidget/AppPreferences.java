/**
 * 
 */
package com.solution24hours.imagechangewidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author BABU
 * 
 */
public class AppPreferences {
	private static final String APP_SHARED_PREFS = "Image_Change_Widget_preferences";
	private SharedPreferences apppSharedPreferences;
	private Editor preferencesEditor;

	@SuppressWarnings("static-access")
	public AppPreferences(Context context) {
		apppSharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS,
				context.MODE_PRIVATE);
		preferencesEditor = apppSharedPreferences.edit();
	}

	public boolean setImage(int number) {
		preferencesEditor.putInt("Number", number);
		preferencesEditor.commit();
		return true;

	}

	public int getImage() {
		return apppSharedPreferences.getInt("Number", 0);
	}
	
	public void toggleImage() {
		int newValue = (getImage() == 1 ? 0 : 1);
		setImage(newValue);
	}

	

}
