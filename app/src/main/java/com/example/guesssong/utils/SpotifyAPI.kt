package com.example.guesssong.utils

import android.content.Context
import android.util.Log
import com.example.guesssong.Song
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Credentials
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SpotifyAPI(private val context: Context) {
    fun readCreds(): String {
        val inputStream = context.assets.open("config.txt")
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().use { reader ->
            reader.forEachLine { lineList.add(it) }
        }
        return Credentials.basic(lineList[0], lineList[1])
    }

    fun getPlaylistTracks(playlistId: String = "3cEYpjA9oz9GiPac4AsH4n"):  List<Song> {
        val token = readCreds()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/playlists/$playlistId/tracks")
            .header("Authorization", token)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(TracksResponse::class.java)
            val tracksResponse = jsonAdapter.fromJson(response.body!!.string())
            return tracksResponse?.items?.map {
                Song(
                    id = 0L,
                    title = it.track.name,
                    artist = it.track.artists.joinToString(", ") { artist -> artist.name },
                    playlistId = 0L
                )
            } ?: listOf()
        }
    }
}

data class TracksResponse(val items: List<Item>)
data class Item(val track: Track)
data class Track(val name: String, val artists: List<Artist>)
data class Artist(val name: String)