package heyvsaucemichaelhere.musicplays.updater;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by schutzekatze on 3/23/17.
 */

public class Installer extends AsyncTask<Void, Integer, Void>
{
    private static final String APK_URL = "https://github.com/schutzekatze/MusicPlays/raw/master/MusicPlays.apk";
    private static final String APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MusicPlays.apk";

    private Context context;

    public Installer(Context context)
    {
        this.context = context;
    }

    public static void cleanup()
    {
        new File(APK_PATH).delete();
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try {
            URL url = new URL(APK_URL);
            URLConnection connection = url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();

            try (
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(APK_PATH)
            )
            {
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            }
        } catch (Exception e) {}
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {}

    @Override
    protected void onPostExecute(Void result)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(APK_PATH)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
