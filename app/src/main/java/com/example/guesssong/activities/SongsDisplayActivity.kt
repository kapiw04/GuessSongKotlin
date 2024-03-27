package com.example.guesssong.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
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
class SongsDisplayActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var text: TextView
    private val songsStack = Stack<Song>()
    private var songs: ArrayList<Song>? = null

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    enum class NodState {
        WAITING_FOR_NOD_DOWN,
        WAITING_FOR_NOD_UP,
        NOD_DETECTED
    }

    private var currentNodState = NodState.WAITING_FOR_NOD_DOWN

    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            detectNod(event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.song_item)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        songsStack.addAll(songs!!)
        text = findViewById(R.id.tvSongTitle)
        text.text = songsStack.peek().toString()

        mDetector = GestureDetectorCompat(this, this)
    }



    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and above
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            songs = intent.getParcelableArrayListExtra("songs", Song::class.java)
        } else {
            @Suppress("DEPRECATION")
            songs = intent.getParcelableArrayListExtra("songs")
        }
    }

    fun detectNod(event: SensorEvent) {
        val axis = event.values[0]

        val thresholdDown = -7.0f
        val thresholdUp = 2.0f

        when (currentNodState) {
            NodState.WAITING_FOR_NOD_DOWN -> {
                if (axis < thresholdDown) {
                    currentNodState = NodState.WAITING_FOR_NOD_UP
                }
            }
            NodState.WAITING_FOR_NOD_UP -> {
                if (axis > thresholdUp) {
                    onNodDetected()
                    currentNodState = NodState.WAITING_FOR_NOD_DOWN
                }
            }
            NodState.NOD_DETECTED -> {
                // Change color of the background

            }
        }
    }

    private fun onNodDetected() {
        nextSong()
        currentNodState = NodState.WAITING_FOR_NOD_DOWN
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
