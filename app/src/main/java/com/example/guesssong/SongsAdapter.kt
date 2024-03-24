package com.example.guesssong

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guesssong.databinding.SongItemBinding
import java.util.Stack

class SongsAdapter(private var songs: List<Song>) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(val binding: SongItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs.last() // Get the last song
        with(holder.binding) {
            tvSongTitle.text = song.title
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun updateSongs(newSongs: List<Song>) {
        this.songs = newSongs
        notifyDataSetChanged()
    }
}
