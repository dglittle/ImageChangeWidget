/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.solution24hours.imagechangewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

	// resource constants for the images
	private static final int[] IMAGES = { 
		R.drawable.awake, 
		R.drawable.sleep, 
		R.drawable.pending };

	@Override
	public void onUpdate(
			Context context, 
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		//boolean newStyle = false; //true;
		//boolean oldStyle = true; //false;
		boolean newStyle = true;
		boolean oldStyle = false;

		if (newStyle) {
			ComponentName componentName = new ComponentName(context, AppWidget.class);
			int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
			
			/*
			// Change the image to the "hourglass"
			RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			updateViews.setImageViewResource(R.id.changeImageView, IMAGES[2]);
			appWidgetManager.updateAppWidget(componentName,	updateViews);
			 */
			
			// Call the service to access the http data and update the widget
			Intent intent = new Intent(context, HttpUpdateWidgetService.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			context.startService(intent);
			
		} else if (oldStyle) {
			ComponentName componentName = new ComponentName(context, AppWidget.class);

			appWidgetManager.updateAppWidget(componentName,
					buildHourglassUpdate(context, appWidgetIds));

			/*
			if (new AppPreferences(context).getImage() == 0) {
				new AppPreferences(context).setImage(1);
			}else {
				new AppPreferences(context).setImage(0);
			}
			
			appWidgetManager.updateAppWidget(componentName,
					buildUpdate(context, appWidgetIds));
			*/
		}
	}
	
	private RemoteViews buildHourglassUpdate(Context context, int[] appWidgetIds) {
		
		RemoteViews updateViews = new RemoteViews(
				context.getPackageName(),
				R.layout.widget);

		Intent intent = new Intent(context, AppWidget.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 
				0,
				intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);

		updateViews.setImageViewResource(
				R.id.changeImageView,
				IMAGES[2]);

		// Add an on-click listener for both the widget background area and the image inside...
		updateViews.setOnClickPendingIntent(
				R.id.changeImageView, 
				pendingIntent);
		updateViews.setOnClickPendingIntent(
				R.id.background, 
				pendingIntent);
		
		return (updateViews);
	}

	private RemoteViews buildUpdate(Context context, int[] appWidgetIds) {
		
		RemoteViews updateViews = new RemoteViews(
				context.getPackageName(),
				R.layout.widget);

		// Build the intent to call the service (which is also this class)
		Intent intent = new Intent(context, AppWidget.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 
				0,
				intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);

		updateViews.setImageViewResource(
				R.id.changeImageView,
				IMAGES[new AppPreferences(context).getImage()]);

		// Add an on-click listener for both the widget background area and the image inside...
		updateViews.setOnClickPendingIntent(
				R.id.changeImageView, 
				pendingIntent);
		updateViews.setOnClickPendingIntent(
				R.id.background, 
				pendingIntent);
		
		return (updateViews);
	}
}
