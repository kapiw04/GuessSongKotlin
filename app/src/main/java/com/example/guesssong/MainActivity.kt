package com.example.guesssong

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guesssong.utils.SpotifyAPI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.btnStart)
        button.setOnClickListener {
            val SDK_INT: Int = android.os.Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = ThreadPolicy.Builder()
                    .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                val spotifyAPI = SpotifyAPI(this)
                val songs: List<Song> = spotifyAPI.getPlaylistTracks()
                val intent = Intent(this, SongsDisplayActivity::class.java)
                intent.putParcelableArrayListExtra("songs", ArrayList(songs))
                startActivity(intent)
            }
        }
    }


}


