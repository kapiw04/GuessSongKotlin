package com.example.guesssong.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class CountDownTimer(private val millisInFuture: Long, private val countDownInterval: Long = 1000) {
    private var job: Job? = null
    private var timeLeft = millisInFuture
    var isRunning = false
        private set

    fun start(scope: CoroutineScope) {
        stop()
        job = scope.launch {
            isRunning = true
            while (isActive && timeLeft > 0) {
                delay(countDownInterval)
                timeLeft -= countDownInterval
                tick(timeLeft)
                if (timeLeft <= 0) {
                    onFinish()
                }
            }
        }
    }

    fun stop() {
        isRunning = false
        job?.cancel()
        job = null
    }

    fun reset() {
        stop()
        timeLeft = millisInFuture
    }

    open fun tick(timeLeft: Long) {
    }

    abstract fun onFinish()
}
