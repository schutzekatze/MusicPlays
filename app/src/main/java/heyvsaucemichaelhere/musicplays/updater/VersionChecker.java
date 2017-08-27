package heyvsaucemichaelhere.musicplays.updater;

import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by schutzekatze on 3/23/17.
 */

public abstract class VersionChecker extends AsyncTask<Void, Integer, Integer>
{
    private static final String VERSION_URL = "https://raw.githubusercontent.com/schutzekatze/MusicPlays/master/VersionCode";

    @Override
    protected Integer doInBackground(Void... params)
    {
        int versionCode = -1;

        try
        {
            URL url = new URL(VERSION_URL);
            URLConnection connection = url.openConnection();
            connection.connect();

            try (InputStream input = new BufferedInputStream(url.openStream()); Scanner scanner = new Scanner(input))
            {
                versionCode = Integer.parseInt(scanner.nextLine());
            }
        } catch (Exception e) {}

        return versionCode;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {}

    @Override
    protected abstract void onPostExecute(Integer result);
}
