package com.example.guesssong.utils

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.guesssong.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
