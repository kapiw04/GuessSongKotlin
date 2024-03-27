package com.example.guesssong.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guesssong.R
import com.example.guesssong.dataclasses.Song
import com.example.guesssong.utils.SpotifyAPI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.btnStart)
        val uploadButton: Button = findViewById(R.id.btnUploadPlaylist)

        startButton.setOnClickListener {
            val SDK_INT: Int = android.os.Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val spotifyAPI = SpotifyAPI(this)
                val songs: List<Song> = spotifyAPI.getTracksToPlay(12)
                val intent = Intent(this, SongsDisplayActivity::class.java)
                intent.putParcelableArrayListExtra("songs", ArrayList(songs))
                startActivity(intent)
            }
        }

        uploadButton.setOnClickListener {
            val intent = Intent(this, UploadPlaylistActivity::class.java)
            startActivity(intent)
        }
    }


}


