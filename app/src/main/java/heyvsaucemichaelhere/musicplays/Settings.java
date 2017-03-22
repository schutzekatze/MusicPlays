package heyvsaucemichaelhere.musicplays;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by schutzekatze on 3/22/17.
 */

public class Settings
{
    public static final float DEFAULT_VOLUME = 0.75f;
    public static final String MUSIC_ID_KEY = "heyvsaucemichaelhere.musicplays.Settings.MUSIC_ID_KEY";
    public static final String VOLUME_KEY = "heyvsaucemichaelhere.musicplays.Settings.VOLUME_KEY";
    public static final String SHARED_PREFERENCES_NAME = "heyvsaucemichaelhere.musicplays.Settings.SHARED_PREFERENCES_NAME";

    public synchronized static void load(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(Settings.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        Settings.setMusicId(sharedPref.getInt(Settings.MUSIC_ID_KEY, R.raw.vsauce));
        Settings.setVolume(sharedPref.getFloat(Settings.VOLUME_KEY, Settings.DEFAULT_VOLUME));
    }

    public synchronized static void save(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(Settings.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(MUSIC_ID_KEY, getMusicId());
        editor.putFloat(VOLUME_KEY, getVolume());
        editor.commit();
    }

    public synchronized static int getMusicId() {
        return musicId;
    }

    public synchronized static float getVolume() {
        return volume;
    }

    public synchronized static void setMusicId(int musicId) {
        Settings.musicId = musicId;
    }

    public synchronized static void setVolume(float volume) { Settings.volume = volume; }

    private static int musicId;
    private static float volume;
}
