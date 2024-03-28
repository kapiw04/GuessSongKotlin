package com.example.guesssong.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guesssong.R
import com.example.guesssong.utils.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingsLayout, SettingsFragment())
            .commit()
    }
}
