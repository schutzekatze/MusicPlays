package heyvsaucemichaelhere.musicplays.updater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class SelfUpdater
{
    private static final String UPDATE_AVAILABLE_TITLE = "Update available";
    private static final String UPDATE_AVAILABLE_MESSAGE = "There's an update available, do you want to download it?";

    public static void checkForUpdate(final Context context)
    {
        new VersionChecker() {
            @Override
            protected void onPostExecute(Integer result) {
                PackageInfo pInfo = null;
                try
                {
                    pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    int versionCode = pInfo.versionCode;

                    if (versionCode < result)
                    {
                        new AlertDialog.Builder(context)
                                .setTitle(UPDATE_AVAILABLE_TITLE )
                                .setMessage(UPDATE_AVAILABLE_MESSAGE)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        new Installer(context).execute();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (Exception e) {}
            }
        }.execute();
    }

    public static void cleanup()
    {
        Installer.cleanup();
    }
}
