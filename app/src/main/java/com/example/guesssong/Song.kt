package com.example.guesssong

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val playlistId: Long
)
{
    override fun toString(): String {
        return "$title - $artist"
    }
}

