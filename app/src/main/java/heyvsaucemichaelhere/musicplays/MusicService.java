package heyvsaucemichaelhere.musicplays;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service
{
    private static final int FIRST_KEY = 0;
    private static final int SECOND_KEY = 1;

    private BroadcastReceiver screenEventReceiver;
    private Timer timer = null;
    private boolean[] pressedKeys = new boolean[4];

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void playVsauce()
    {
        final AudioManager audioManager =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        final int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)(Settings.getVolume() * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
        MediaPlayer.create(this, Settings.getMusicId()).start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }
        }, 10000);
    }

    public synchronized void activateKey(int key)
    {
        if (key == FIRST_KEY && pressedKeys[0] && pressedKeys[1])
        {
            pressedKeys[2] = true;
        }

        if (key == SECOND_KEY && pressedKeys[0] && pressedKeys[1])
        {
            pressedKeys[3] = true;
        }

        pressedKeys[key] = true;

        if (timer == null)
        {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean goodToGo = true;

                    for(int i = 0; i < pressedKeys.length; i++)
                    {
                        if (!pressedKeys[i]) goodToGo = false;
                    }

                    if (goodToGo)
                    {
                        playVsauce();
                    }

                    for(int i = 0; i < pressedKeys.length; i++)
                    {
                        pressedKeys[i] = false;
                    }
                    timer = null;
                }
            }, 3000);
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        for(int i = 0; i < pressedKeys.length; i++)
        {
            pressedKeys[i] = false;
        }

        Settings.load(getApplicationContext());

        screenEventReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Intent.ACTION_SCREEN_ON)
                {
                    activateKey(FIRST_KEY);
                }
                else
                {
                    activateKey(SECOND_KEY);
                }
            }
        };

        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(screenEventReceiver, screenStateFilter);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent("com.android.MusicServerDied");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(screenEventReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
