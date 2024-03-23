package com.example.guesssong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.vpSongs)

        val playlistId: Long = 1
        val songs = listOf(
            Song(1, "Song 1", "Artist 1", playlistId),
            Song(2, "Song 2", "Artist 2", playlistId),
            Song(3, "Song 3", "Artist 3", playlistId),
            Song(4, "Song 4", "Artist 4", playlistId),
            Song(5, "Song 5", "Artist 5", playlistId),
            Song(6, "Song 6", "Artist 6", playlistId),
            Song(7, "Song 7", "Artist 7", playlistId),
            Song(8, "Song 8", "Artist 8", playlistId),
            Song(9, "Song 9", "Artist 9", playlistId),
            Song(10, "Song 10", "Artist 10", playlistId),

        )

        viewPager.adapter = SongsAdapter(songs)
    }
}