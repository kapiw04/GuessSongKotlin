package com.example.guesssong.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guesssong.databinding.ActivityPickPlaylistBinding
import com.example.guesssong.utils.PlaylistAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

        val playlists = readPlaylistNames()

        if (playlists.isEmpty()) {
            val intent = Intent(this, UploadPlaylistActivity::class.java)
            startActivity(intent)
            return
        }

        playlistAdapter = PlaylistAdapter(
            playlists
        )
        binding.rvPlaylists.layoutManager = LinearLayoutManager(this)
        binding.rvPlaylists.adapter = playlistAdapter
    }

    private fun readPlaylistNames(): List<String> {
        val fileName = "playlists.json"
        val names: List<String> = try {
            val json = applicationContext.openFileInput(fileName).bufferedReader().use { it.readText() }
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
        } catch (e: FileNotFoundException) {
            listOf()
        } catch (e: IOException) {
            listOf()
        }

        return names
    }

}