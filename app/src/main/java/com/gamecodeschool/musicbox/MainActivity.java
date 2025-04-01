package com.gamecodeschool.musicbox;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView songListView;
    TextView songInfoTextView;
    Button playButton;

    // Song titles for the ListView
    String[] songTitles = {"Song One", "Song Two", "Song Three"};

    // Raw file references
    int[] songFiles = {R.raw.song1, R.raw.song2, R.raw.song3};

    MediaPlayer mediaPlayer;
    int currentSongIndex = -1;  // No song selected yet
    boolean isPlaying = false;  // Track play/pause state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the XML components
        songListView = findViewById(R.id.songListView);
        songInfoTextView = findViewById(R.id.songInfoTextView);
        playButton = findViewById(R.id.playButton);

        // Set up ListView with song titles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, songTitles);
        songListView.setAdapter(adapter);

        // Handle ListView item clicks
        songListView.setOnItemClickListener((adapterView, view, position, id) -> {
            currentSongIndex = position; // Update which song was selected
            songInfoTextView.setText("Selected: " + songTitles[position]);

            // Prepare the MediaPlayer with the selected song
            prepareMediaPlayer();

            // Auto-play the song on selection
            playSong();
        });

        // Handle Play/Pause button clicks
        playButton.setOnClickListener(view -> {
            if (currentSongIndex == -1) {
                songInfoTextView.setText("Select a song first!");
                return;
            }

            if (isPlaying) {
                pauseSong();
            } else {
                playSong();
            }
        });
    }

    // Prepare MediaPlayer with the selected song
    private void prepareMediaPlayer() {
        releaseMediaPlayer(); // Release old song if playing
        mediaPlayer = MediaPlayer.create(this, songFiles[currentSongIndex]);
    }

    // Play the song
    private void playSong() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            playButton.setText("Pause");
            songInfoTextView.setText("Now Playing: " + songTitles[currentSongIndex]);
        }
    }

    // Pause the song
    private void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            playButton.setText("Play");
            songInfoTextView.setText("Paused: " + songTitles[currentSongIndex]);
        }
    }

    // Release MediaPlayer resources
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            playButton.setText("Play");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
}
