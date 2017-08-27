package heyvsaucemichaelhere.musicplays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public abstract class KeyCombinationReceiver extends BroadcastReceiver {

    public static final int UNLOCKS_FOR_ACTIVATION = 5;
    public static final int KEY_COMBINATION_TIME_WINDOW = 3000;
    public static final String TAG = "KeyCombinationReceiver";

    private Context context;
    private IntentFilter screenStateFilter;

    public KeyCombinationReceiver(Context context) {
        this.context = context;
        screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    public abstract void onKeyCombination();

    public void start() { context.registerReceiver(this, screenStateFilter); }
    public void stop() { context.unregisterReceiver(this); }

    private int counter = 0;

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        if (counter == 0) {
            counter = 1;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "Time window expired");
                    synchronized (KeyCombinationReceiver.this) {
                        if (counter >= UNLOCKS_FOR_ACTIVATION) {
                            Log.d(TAG, "Key combination successful (counter = " + counter + ")");
                            onKeyCombination();
                        } else {
                            Log.d(TAG, "Key combination failed (counter = " + counter + ")");
                        }
                        counter = 0;
                    }
                }
            }, KEY_COMBINATION_TIME_WINDOW);
        }
        else {
            counter++;
        }
    }

}
