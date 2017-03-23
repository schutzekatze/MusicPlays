package heyvsaucemichaelhere.musicplays.updater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import heyvsaucemichaelhere.musicplays.R;

/**
 * Created by schutzekatze on 3/23/17.
 */

public class Installer extends AsyncTask<Void, Integer, Void>
{
    private static final String APK_URL = "https://github.com/schutzekatze/MusicPlays/raw/master/MusicPlays.apk";
    private static final String APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MusicPlays.apk";
    private static final String PROGRESS_TITLE = "Update";
    private static final String PROGRESS_MESSAGE = "Downloading...";

    private Context context;
    private ProgressDialog progressDialog;

    public Installer(Context context)
    {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(PROGRESS_TITLE);
        progressDialog.setMessage(PROGRESS_MESSAGE);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            progressDialog.setProgressDrawable(context.getDrawable(android.R.drawable.progress_horizontal));
        }
        else
        {
            progressDialog.setProgressDrawable(context.getResources().getDrawable(android.R.drawable.progress_horizontal));
        }
        progressDialog.setIndeterminate(false);
        progressDialog.show();
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
                    publishProgress((int) (total * 100.0 / fileLength));
                    output.write(data, 0, count);
                }
            }
        } catch (Exception e) {}
        progressDialog.dismiss();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
        progressDialog.setProgress((int)(progress[0] / 100.0 * progressDialog.getMax()));
    }

    @Override
    protected void onPostExecute(Void result)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(APK_PATH)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
