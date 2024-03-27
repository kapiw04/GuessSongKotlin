package com.example.guesssong.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.guesssong.R
import com.example.guesssong.dataclasses.Song
import java.util.Stack
import kotlin.math.abs

@Suppress("DEPRECATION")
class SongsDisplayActivity: AppCompatActivity(),
            GestureDetector.OnGestureListener
{

    private lateinit var text : TextView
    private val songsStack = Stack<Song>()
    private var songs: ArrayList<Song>? = null

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            // For Android 11 and above
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For legacy versions (Below Android 11)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
        setContentView(R.layout.song_item)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            songs = intent.getParcelableArrayListExtra("songs", Song::class.java)
        } else {
            @Suppress("DEPRECATION")
            songs = intent.getParcelableArrayListExtra("songs")
        }
        songsStack.addAll(songs!!)
        text = findViewById(R.id.tvSongTitle)
        text.text = songsStack.peek().toString()

        mDetector = GestureDetectorCompat(this, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }



    @SuppressLint("SetTextI18n")
    private fun nextSong() {
        if (!songsStack.isEmpty()) {
            songsStack.pop()
            if (!songsStack.isEmpty()) {
                text.text = songsStack.peek().toString()
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
