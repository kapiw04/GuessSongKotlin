package com.example.guesssong.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guesssong.R
import com.example.guesssong.activities.SongsDisplayActivity
import com.example.guesssong.dataclasses.Playlist
import com.example.guesssong.dataclasses.Song

class PlaylistAdapter(private val playlists: PriorityList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    private var playlistsList: List<Playlist> = playlists.toList()

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.tvPlaylistName)
        val playlistImage: ImageView = itemView.findViewById(R.id.ivPlaylistImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playlists.toList().size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        Log.d("playlists", "Playlists: $playlists")

        holder.playlistName.text = playlistsList[position].name
        Glide.with(holder.itemView.context)
            .load(playlistsList[position].image)
            .override(getSizeOfImage(holder.itemView.context).toInt(), getSizeOfImage(holder.itemView.context).toInt())
            .into(holder.playlistImage)
        holder.itemView.setOnClickListener {
            handlePlaylistClick(holder.itemView.context, playlistsList[position])
        }
    }

    private fun getSizeOfImage(context: Context): Float {
        val screenWidth: Float = context.resources.displayMetrics.widthPixels.toFloat()
        val marginSize: Float = context.resources.getDimension(R.dimen.margin)
        val frameSize: Float = context.resources.getDimension(R.dimen.frame)
        return screenWidth / 2 - (marginSize + frameSize)
    }

    private fun handlePlaylistClick(context: Context, playlist: Playlist) {
        val spotifyAPI = SpotifyAPI(context)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val amount = preferences.getString("songsPerGame", "5") ?: "5"
        val songs: List<Song> = spotifyAPI.getTracksToPlay(playlist.name, amount.toInt())

        val intent = Intent(context, SongsDisplayActivity::class.java).apply {
            putParcelableArrayListExtra("songs", ArrayList(songs))
        }
        val oldIndex = playlistsList.indexOf(playlist)
        playlists.moveToFront(playlist)
        playlistsList = playlists.toList()
        JsonManager(context).updatePlaylistFile(playlistsList)
        notifyItemRangeChanged(0, oldIndex + 1)
        context.startActivity(intent)
    }
}