package com.example.guesssong.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guesssong.R
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.btnStart)
        val uploadButton: Button = findViewById(R.id.btnUploadPlaylist)

        startButton.setOnClickListener {
            handleStartButton()
        }

        uploadButton.setOnClickListener {
            handleUploadButton()
        }
    }

    private fun handleStartButton() {
        val intent = Intent(this, PickPlaylistActivity::class.java)
        startActivity(intent)
    }

    private fun handleUploadButton() {
        val intent = Intent(this, UploadPlaylistActivity::class.java)
        startActivity(intent)
    }
}


