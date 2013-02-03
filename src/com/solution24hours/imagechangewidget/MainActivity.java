package com.solution24hours.imagechangewidget;

import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "App widget ready to be added!", Toast.LENGTH_LONG)
				.show();
		new AppPreferences(this).setImage(0);
		finish();
	}

}
