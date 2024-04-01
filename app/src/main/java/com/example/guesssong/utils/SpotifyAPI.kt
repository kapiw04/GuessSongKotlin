package com.example.guesssong.utils

import android.content.Context
import com.example.guesssong.dataclasses.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SpotifyAPI(private val context: Context) {
    private val jsonManager = JsonManager(context)
    private fun readCreds(): String {
        val inputStream = context.assets.open("config.txt")
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().use { reader ->
            reader.forEachLine { lineList.add(it) }
        }

        return Credentials.basic(lineList[0], lineList[1])
    }

    private fun getToken(): String {
        val token = readCreds()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .addHeader("Authorization", token)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post("grant_type=client_credentials".toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(TokenResponse::class.java)
            val tokenResponse = jsonAdapter.fromJson(response.body!!.string())
            return tokenResponse?.access_token ?: ""
        }
    }

    fun getPlaylistTracks(url: String) {
        val playlistId = urlToId(url)
        val token = "Bearer ${getToken()}"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/playlists/$playlistId/")
            .addHeader("Authorization", token)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(SpotifyResponse::class.java)
            val responseStr = response.body!!.string()
            val spotifyResponse = jsonAdapter.fromJson(responseStr)

            val tracksResponse = spotifyResponse?.tracks
            val playlistName = spotifyResponse?.name ?: "playlist"
            val playlistImage =
                spotifyResponse?.images?.getOrNull(0)?.url ?: "https://via.placeholder.com/150"
            val playlist = Playlist(playlistName, playlistImage)
            val songs = tracksResponse?.items?.map {
                Song(
                    title = it.track.name,
                    artist = it.track.artists.joinToString(", ") { artist -> artist.name },
                )
            } ?: listOf()
            jsonManager.saveSongsToJson(songs, playlistName)
            jsonManager.savePlaylistToJson(playlist)
        }
    }

    private fun urlToId(url: String): String {
        val regex = Regex("playlist/(\\w+)")
        return regex.find(url)!!.groupValues[1]
    }

    fun getTracksToPlay(playlistName: String, amount: Int = 10): List<Song> {
        val tracks = jsonManager.readFromJson(playlistName)
        val _amount: Int = if (amount > tracks.size) tracks.size else amount
        val shuffledTracks = tracks.shuffled()

        return shuffledTracks.take(_amount)
    }


}

