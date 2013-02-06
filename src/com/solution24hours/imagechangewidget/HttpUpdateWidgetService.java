package com.solution24hours.imagechangewidget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

// made using Lars Vogel's UpdateWidgetService as a template
public class HttpUpdateWidgetService extends Service {
	
	// resource constants for the images
	private static final int[] IMAGES = { 
		R.drawable.awake, 
		R.drawable.sleep, 
		R.drawable.pending };

	@Override
	public void onStart(Intent intent, int startId) {
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		ComponentName thisWidget = new ComponentName(getApplicationContext(), AppWidget.class);

		{
			// Change the image to the "hourglass"
			RemoteViews updateViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
			updateViews.setImageViewResource(R.id.changeImageView, R.drawable.pending);
			appWidgetManager.updateAppWidget(thisWidget, updateViews);
		}
		
		// Do the HTTP request...
		boolean success = true;
		try {
			double x = new Random().nextDouble();
			String httpAddress = "";
			if (x < 0.5) {
				httpAddress = "http://www.google.com"; 
			} else {
				httpAddress = "http://blahblahblah5678123.com";
			}
			
			// hack:
			//httpAddress = "http://www.google.com";
			
			URL url = new URL (httpAddress);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String line;
		    while ((line = in.readLine()) != null) {
		    }
		    in.close();
		} catch (MalformedURLException e) {
			// TODO
			success = false;
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO
			success = false;
			//throw new RuntimeException(e);
		}
		// hack:
		//success = true;
		
		if (success) {
			// Toggle the icon
			new AppPreferences(getApplicationContext()).toggleImage();
		}
		
		{
			Context context = getApplicationContext();
			// Change the image again...
			RemoteViews updateViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
			updateViews.setImageViewResource(R.id.changeImageView, IMAGES[new AppPreferences(context).getImage()]);
			
			// Add an on-click listener for both the widget background area and the image inside...
			int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			Intent intent2 = new Intent(context, AppWidget.class);
			intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					context, 
					0,
					intent2, 
					PendingIntent.FLAG_UPDATE_CURRENT);
			updateViews.setOnClickPendingIntent(R.id.changeImageView, pendingIntent);
			updateViews.setOnClickPendingIntent(R.id.background, pendingIntent);

			appWidgetManager.updateAppWidget(thisWidget, updateViews);
		}
		

		// Update the icon and listeners...
/*
		
		RemoteViews updateViews = new RemoteViews(
				getApplicationContext().getPackageName(),
				R.layout.widget);


		//updateViews.setImageViewResource(R.id.changeImageView, IMAGES[new AppPreferences(context).getImage()]);


		appWidgetManager.updateAppWidget(thisWidget, updateViews);
*/
		stopSelf();
		
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
