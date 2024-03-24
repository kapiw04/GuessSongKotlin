package com.example.guesssong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.util.Stack

class SongsDisplayActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private var songsStack: Stack<Song> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.songs_display)
        viewPager = findViewById(R.id.vpSongs)

        populateSongs()

        viewPager.adapter = SongsAdapter(songsStack.toList())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                moveToNextSong()
            }
        })

    }

    private fun populateSongs() {
        for (i in 1..10) {
            songsStack.push(Song(i.toLong(), "Song $i", "Artist $i", 1))
        }
    }

    private fun moveToNextSong() {
        if (songsStack.isNotEmpty()) {
            songsStack.pop()
            (viewPager.adapter as? SongsAdapter)?.updateSongs(songsStack.toList())

            viewPager.setCurrentItem(0, false)
        }
    }
}
