package com.example.guesssong.utils

import android.content.Context
import com.example.guesssong.dataclasses.Playlist
import com.example.guesssong.dataclasses.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonManager (private var context: Context){

    fun savePlaylistToJson(playlist: Playlist) {
        val gson = Gson()
        val file = context.getFileStreamPath("playlists.json")
        val playlists: MutableList<Playlist> = if (file?.exists() == true) {
            val json =
                context.openFileInput("playlists.json").bufferedReader().use { it.readText() }
            gson.fromJson(json, object : TypeToken<List<Playlist>>() {}.type)
        } else mutableListOf()

        if (playlists.contains(playlist)) return
        playlists.add(playlist)
        updatePlaylistFile(playlists)
    }


    fun readFromJson(playlistName: String): List<Song> {
        val file = context.openFileInput("$playlistName.json")
        val text: String = file.bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(text, Array<Song>::class.java).toList()
    }

    fun saveSongsToJson(songs: List<Song>, playlistName: String) {
        val gson = Gson()
        val songsJson = gson.toJson(songs)
        val songsFile = context.openFileOutput("$playlistName.json", Context.MODE_PRIVATE)
        songsFile.write(songsJson.toByteArray())
        songsFile.close()
    }
    fun updatePlaylistFile(playlists: List<Playlist>) {
        val gson = Gson()
        val updatedPlaylistsJson = gson.toJson(playlists)
        context.openFileOutput("playlists.json", Context.MODE_PRIVATE).use {
            it.write(updatedPlaylistsJson.toByteArray())
        }
    }

}