package heyvsaucemichaelhere.musicplays;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    public static final int TIMEOUT = 10000;
    public static final String TAG = "MusicService";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private boolean playing = false;
    private Settings settings;
    private KeyCombinationReceiver keyCombinationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        settings = Settings.getSettings(this);

        keyCombinationReceiver = new KeyCombinationReceiver(this) {
            @Override
            public void onKeyCombination() {
                playMusic();
            }
        };
        keyCombinationReceiver.start();

        Log.d(TAG, "Music service created");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Music service destroyed");

        keyCombinationReceiver.stop();
        super.onDestroy();
    }

    private synchronized void playMusic()
    {
        Log.d(TAG, "Music play requested");

        if (playing) {
            Log.d(TAG, "Music already playing, aborting the request");
            return;
        }
        playing = true;

        final AudioManager audioManager =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        final int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)(settings.getVolume() * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
        MediaPlayer.create(this, settings.getMusicId()).start();

        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                synchronized (MusicService.this)
                {
                    playing = false;
                    Log.d(TAG, "Music no longer playing");
                }
            }
        }, TIMEOUT);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent();
        intent.setAction("com.android.MusicServerDied");
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
