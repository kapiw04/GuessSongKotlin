package com.example.guesssong.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.guesssong.databinding.ActivityPickPlaylistBinding
import com.example.guesssong.dataclasses.Playlist
import com.example.guesssong.utils.PlaylistAdapter
import com.example.guesssong.utils.PriorityList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class PickPlaylistActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPickPlaylistBinding
    private lateinit var playlistAdapter: PlaylistAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playlists = readPlaylists(this)

        if (playlists.isEmpty()) {
            val intent = Intent(this, UploadPlaylistActivity::class.java)
            startActivity(intent)
            return
        }

        playlistAdapter = PlaylistAdapter(
            PriorityList(playlists)
        )
        binding.rvPlaylists.layoutManager = GridLayoutManager(this, 2)
        binding.rvPlaylists.adapter = playlistAdapter
    }

    private fun readPlaylists(context: Context): List<Playlist>{
        val fileName = "playlists.json"
        val file = File(context.filesDir, fileName)
        val jsonString: String = try {
            FileInputStream(file).bufferedReader().use {
                it.readText()
            }
        } catch (e: FileNotFoundException) {
            return emptyList()
        } catch (e: IOException) {
            return emptyList()
        }

        val gson = Gson()
        val listType = object : TypeToken<List<Playlist>>() {}.type
        return gson.fromJson(jsonString, listType)
    }


}