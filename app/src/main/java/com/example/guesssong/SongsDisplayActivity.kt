package com.example.guesssong

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import java.util.Stack
import kotlin.math.abs

class SongsDisplayActivity : AppCompatActivity(),
            GestureDetector.OnGestureListener
{

    private lateinit var text : TextView
    private var songsStack: Stack<Song> = Stack()

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_item)

        populateSongs()
        text = findViewById(R.id.tvSongTitle)
        text.text = songsStack.peek().title

        mDetector = GestureDetectorCompat(this, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun populateSongs() {
        for (i in 1..10) {
            songsStack.push(Song(i.toLong(), "Song $i", "Artist $i", 1))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun nextSong() {
        if (!songsStack.isEmpty()) {
            songsStack.pop()
            if (!songsStack.isEmpty()) {
                text.text = songsStack.peek().title
            } else {
                text.text = "No more songs"
            }
        }
    }

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onShowPress(e: MotionEvent) {
        // Do nothing
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false

    override fun onLongPress(e: MotionEvent) {
        // Do nothing
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        e1?.let { start ->
            e2.let { end ->
                val deltaX = end.x - start.x
                // Swipe left
                if (abs(deltaX) > 100 && deltaX < 0) {
                    nextSong()
                    return true
                }
            }
        }
        return false
    }
}
