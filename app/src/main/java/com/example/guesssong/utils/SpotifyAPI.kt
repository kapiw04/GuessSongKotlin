package com.example.guesssong.utils

import android.content.Context
import com.example.guesssong.dataclasses.Song
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

            saveToJson(tracksResponse?.items?.map {
                Song(
                    id = 0L,
                    title = it.track.name,
                    artist = it.track.artists.joinToString(", ") { artist -> artist.name },
                    playlistId = 0L
                )
            } ?: listOf(), playlistName)
        }
    }

    private fun urlToId(url: String): String {
//      https://open.spotify.com/playlist/37i9dQZF1DX5KpP2LN299J?si=d75ddd84a077404f -> 37i9dQZF1DX5KpP2LN299J
        val regex = Regex("playlist/(\\w+)")
        return regex.find(url)!!.groupValues[1]
    }

    fun getTracksToPlay(playlistName: String, amount: Int = 10): List<Song> {
        val tracks = readFromJson(playlistName)
        val _amount: Int = if (amount > tracks.size) tracks.size else amount
        val shuffledTracks = tracks.shuffled()

        return shuffledTracks.take(_amount)
    }

    private fun saveToJson(songs: List<Song>, playlistName: String) {
        val gson = Gson()
        val songsJson = gson.toJson(songs)
        savePlaylistName(playlistName)
        saveSongsToPlaylist(playlistName, songsJson)
    }

    private fun saveSongsToPlaylist(playlistName: String, json: String) {
        val playlistFile = context.openFileOutput("$playlistName.json", Context.MODE_PRIVATE)
        playlistFile.write(json.toByteArray())
        playlistFile.close()
    }

    private fun readFromJson(playlistName: String): List<Song> {
        val file = context.openFileInput("$playlistName.json")
        val text = file.bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(text, Array<Song>::class.java).toList()
    }

    private fun savePlaylistName(playlistName: String) {
        val playlistFile = context.applicationContext.getFileStreamPath("playlists.json")
        val existingPlaylists = if (playlistFile.exists()) {
            val text = playlistFile.bufferedReader().use { it.readText() }
            Gson().fromJson(text, object : TypeToken<List<String>>() {}.type)
        } else {
            listOf<String>()
        }
        val updatedPlaylists: List<String> = existingPlaylists + playlistName
        val jsonToWrite = Gson().toJson(updatedPlaylists)
        context.applicationContext.openFileOutput("playlists.json", Context.MODE_PRIVATE).use {
            it.write(jsonToWrite.toByteArray())
        }
    }
}

data class SpotifyResponse(val tracks: TracksResponse, val name: String)
data class TracksResponse(val items: List<Item>)
data class Item(val track: Track)
data class Track(val name: String, val artists: List<Artist>)
data class Artist(val name: String)
data class TokenResponse(val access_token: String)