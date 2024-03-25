package com.example.guesssong

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val playlistId: Long
) : Parcelable {
    override fun toString(): String {
        return "$title - $artist"
    }
}

