package com.brunofelixdev.mytodoapp.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.ui.activity.MainActivity
import com.brunofelixdev.mytodoapp.ui.fragment.AddItemFragment

class AppWidget : AppWidgetProvider() {

    companion object {
        const val KEY_WIDGET = "key-widget"
        const val EXTRAS_VALUE = "open-form-screen"
        private val TAG = AppWidget::class.java.simpleName
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate")
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(KEY_WIDGET, EXTRAS_VALUE)
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val views = RemoteViews(context.packageName, R.layout.app_widget)

            views.setOnClickPendingIntent(R.id.layout_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}