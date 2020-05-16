package uk.co.zedeff.lastly.domain.main

import android.databinding.ViewDataBinding
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import uk.co.zedeff.lastly.BR
import uk.co.zedeff.lastly.R
import uk.co.zedeff.lastly.databinding.SearchResultRowBinding

interface OnArtistSelectedListener {
    fun onArtistSelected(artist: ArtistViewModel, artistImageView: View, artistNameView: View)
}

class ArtistListAdapter : RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {
    private var artists: Array<ArtistViewModel> = emptyArray()
    private var listener: OnArtistSelectedListener? = null

    fun setListener(listener: OnArtistSelectedListener?) {
        this.listener = listener
    }

    fun update(artists: Array<ArtistViewModel>) {
        this.artists = artists
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchResultRowBinding.inflate(layoutInflater, parent, false)

        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int = artists.size

    class ArtistViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistViewModel, listener: OnArtistSelectedListener?) {
            binding.setVariable(BR.artist, artist)
            binding.executePendingBindings()
            val artistImageView = binding.root.findViewById<ImageView>(R.id.artist_image)
            val artistNameView = binding.root.findViewById<TextView>(R.id.artist_name)
            ViewCompat.setTransitionName(artistImageView, "${artist.name}-image")
            ViewCompat.setTransitionName(artistNameView, artist.name)
            itemView.setOnClickListener { listener?.onArtistSelected(artist, artistImageView, artistNameView) }
        }
    }
}
