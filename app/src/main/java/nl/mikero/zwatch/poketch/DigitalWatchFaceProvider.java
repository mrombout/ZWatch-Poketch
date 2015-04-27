package nl.mikero.zwatch.poketch;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DigitalWatchFaceProvider extends AppWidgetProvider {

    private Typeface fontface;

    private static final int fontColor = Color.argb(255, 56, 80, 48);

    @Override
    public void onEnabled(Context context) {
        fontface = Typeface.createFromAsset(context.getAssets(), "HMPoketchDigital.ttf");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        context.getApplicationContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if(Intent.ACTION_TIME_TICK.equals(action) || Intent.ACTION_TIME_CHANGED.equals(action)) {
            updateAppWidget(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidget(context);
    }

    private RemoteViews updateTime(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.digital_watch_face_provider);

        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        Bitmap timeText = Bitmap.createBitmap(240, 240, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(timeText);
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setSubpixelText(false);
        paint.setTypeface(fontface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fontColor);
        paint.setTextSize(98);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(currentTime, 122, 140, paint);

        views.setImageViewBitmap(R.id.analog_appwidget_clock5, timeText);

        return views;
    }

    private void updateAppWidget(Context context) {
        RemoteViews views = updateTime(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, DigitalWatchFaceProvider.class), views);
    }
}


