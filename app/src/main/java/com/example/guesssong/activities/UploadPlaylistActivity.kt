package com.example.guesssong.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.guesssong.R
import com.example.guesssong.utils.SpotifyAPI

class UploadPlaylistActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_playlist)

        val uploadButton: Button = findViewById(R.id.btnUploadPlaylist)
        val textInput: EditText = findViewById(R.id.etPlaylistUrl)
        uploadButton.setOnClickListener {
            val playlistUrl = textInput.text.toString()
            val SDK_INT: Int = android.os.Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = StrictMode.ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val spotifyAPI = SpotifyAPI(this)
                spotifyAPI.getPlaylistTracks(playlistUrl)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}