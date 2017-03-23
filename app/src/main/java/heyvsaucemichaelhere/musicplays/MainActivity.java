package heyvsaucemichaelhere.musicplays;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;

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
    }

    @Override
    protected void onDestroy() {
        settings.save();
        super.onDestroy();
    }
}
