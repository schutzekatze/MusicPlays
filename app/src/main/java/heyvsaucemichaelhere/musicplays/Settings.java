package heyvsaucemichaelhere.musicplays;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings
{
    private static final float DEFAULT_VOLUME = 0.75f;
    private static final String MUSIC_ID_KEY = "heyvsaucemichaelhere.musicplays.Settings.MUSIC_ID_KEY";
    private static final String VOLUME_KEY = "heyvsaucemichaelhere.musicplays.Settings.VOLUME_KEY";
    private static final String SHARED_PREFERENCES_NAME = "heyvsaucemichaelhere.musicplays.Settings.SHARED_PREFERENCES_NAME";

    private static Settings settings = null;

    public static synchronized Settings getSettings(Context context)
    {
        if (settings == null)
        {
            settings = new Settings(context);
        }

        return settings;
    }

    private Context context;
    private int musicId;
    private float volume;

    public Settings(Context context)
    {
        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(Settings.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        setMusicId(sharedPref.getInt(Settings.MUSIC_ID_KEY, R.raw.vsauce));
        setVolume(sharedPref.getFloat(Settings.VOLUME_KEY, Settings.DEFAULT_VOLUME));
    }

    public synchronized void save()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(Settings.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(MUSIC_ID_KEY, getMusicId());
        editor.putFloat(VOLUME_KEY, getVolume());
        editor.commit();
    }

    public synchronized int getMusicId() { return musicId; }

    public synchronized float getVolume() { return volume; }

    public synchronized void setMusicId(int musicId) { this.musicId = musicId; }

    public synchronized void setVolume(float volume) { this.volume = volume; }
}
