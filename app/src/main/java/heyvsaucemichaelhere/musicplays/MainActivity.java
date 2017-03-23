package heyvsaucemichaelhere.musicplays;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import heyvsaucemichaelhere.musicplays.updater.SelfUpdater;
import heyvsaucemichaelhere.musicplays.updater.VersionChecker;

public class MainActivity extends AppCompatActivity
{
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioButton vaporButton = (RadioButton) findViewById(R.id.vapor_radio);
        RadioButton vsauceButton = (RadioButton) findViewById(R.id.vsauce_radio);
        RadioButton mysteryButton = (RadioButton) findViewById(R.id.mystery_radio);

        settings = Settings.getSettings(this);

        vaporButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setMusicId(R.raw.vapor);
            }
        });

        vsauceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setMusicId(R.raw.vsauce);
            }
        });

        mysteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setMusicId(R.raw.mystery);
            }
        });

        SeekBar volumeBar = (SeekBar) findViewById(R.id.volume_bar);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                settings.setVolume((float)seekBar.getProgress() / seekBar.getMax());
            }
        });

        switch(settings.getMusicId())
        {
            case R.raw.vsauce:
                vsauceButton.setChecked(true);
                break;
            case R.raw.mystery:
                mysteryButton.setChecked(true);
                break;
            case R.raw.vapor:
                vaporButton.setChecked(true);
                break;
        }

        volumeBar.setProgress((int)(volumeBar.getMax() * settings.getVolume()));

        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);

        obtainPermissions();
    }

    private static final int INTERNET_PERMISSION_CODE = 1;
    private static final int RECEIVE_BOOT_COMPLETED_PERMISSION_CODE = 2;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 3;

    private int updaterPermissions = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) return;

        switch(requestCode)
        {
            case INTERNET_PERMISSION_CODE:
            {
                updaterPermissions++;
                break;
            }
            case WRITE_EXTERNAL_STORAGE_PERMISSION_CODE:
            {
                updaterPermissions++;
                break;
            }
        }

        if (updaterPermissions == 2)
        {
            SelfUpdater.checkForUpdate(this);
        }
    }

    private void obtainPermissions()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.RECEIVE_BOOT_COMPLETED },
                        RECEIVE_BOOT_COMPLETED_PERMISSION_CODE);
            }

            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.INTERNET},
                        INTERNET_PERMISSION_CODE);
            }
            else
                updaterPermissions++;

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
            else
                updaterPermissions++;
        }
    }

    @Override
    protected void onDestroy() {
        settings.save();
        SelfUpdater.cleanup();
        super.onDestroy();
    }
}
