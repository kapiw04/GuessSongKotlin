package com.example.guesssong

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guesssong.databinding.SongItemBinding

class SongsAdapter(private var songs: List<Song>) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(val binding: SongItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        with(holder.binding) {
            tvSongTitle.text = song.title
        }
    }

    override fun getItemCount(): Int = songs.size
}
