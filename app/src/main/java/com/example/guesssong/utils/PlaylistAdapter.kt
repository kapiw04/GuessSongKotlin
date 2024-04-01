package com.example.guesssong.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guesssong.R
import com.example.guesssong.activities.SongsDisplayActivity
import com.example.guesssong.dataclasses.Song

class PlaylistAdapter(private val playlistNames: PriorityList<String>): RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    private var playlistNamesList: List<String> = playlistNames.toList()
    class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.tvPlaylistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playlistNames.toList().size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        Log.d("playlists", "Playlists: $playlistNames")

        holder.playlistName.text = playlistNamesList[position]
        holder.itemView.setOnClickListener {
            handlePlaylistClick(holder.itemView.context, playlistNamesList[position])
        }
    }

    private fun handlePlaylistClick(context: Context, playlistName: String) {
        val spotifyAPI = SpotifyAPI(context)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val amount = preferences.getString("songsPerGame", "5") ?: "5"
        val songs: List<Song> = spotifyAPI.getTracksToPlay(playlistName, amount.toInt())

        val intent = Intent(context, SongsDisplayActivity::class.java).apply {
            putParcelableArrayListExtra("songs", ArrayList(songs))
        }
        val fromPosition = playlistNamesList.indexOf(playlistName)
        playlistNames.moveToFront(playlistName)
        playlistNamesList = playlistNames.toList()
        
        val toPosition = 0

        notifyItemMoved(fromPosition, toPosition)
        if (fromPosition != RecyclerView.NO_POSITION) {
            notifyItemRangeChanged(toPosition, fromPosition - toPosition + 1)
        }

        context.startActivity(intent)
    }
}