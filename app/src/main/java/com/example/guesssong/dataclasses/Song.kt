package com.example.guesssong.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val title: String,
    val artist: String,
) : Parcelable {
    override fun toString(): String {
        return "$title - $artist"
    }
}


data class SpotifyResponse(val tracks: TracksResponse, val name: String, val images: List<PlaylistImage>)
data class TracksResponse(val items: List<Item>)
data class Item(val track: Track)
data class Track(val name: String, val artists: List<Artist>)
data class Artist(val name: String)
data class TokenResponse(val access_token: String)
data class PlaylistImage(val url: String)
data class Playlist(val name: String, val image: String) {
    override fun toString(): String {
        return name
    }
}