package heyvsaucemichaelhere.musicplays.updater;

import android.content.Context;
import android.content.pm.PackageInfo;

public class SelfUpdater
{
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
                        new Installer(context).execute();
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
