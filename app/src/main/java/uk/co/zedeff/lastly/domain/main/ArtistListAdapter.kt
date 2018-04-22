package uk.co.zedeff.lastly.domain.main

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.zedeff.lastly.BR
import uk.co.zedeff.lastly.databinding.SearchResultRowBinding

class ArtistListAdapter : RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {

    private var artists: Array<ArtistViewModel> = arrayOf()

    fun update(artists: Array<ArtistViewModel>) {
        this.artists = artists
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchResultRowBinding.inflate(layoutInflater, parent, false)

        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int = artists.size

    class ArtistViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistViewModel) {
            binding.setVariable(BR.artist, artist)
            binding.executePendingBindings()
        }
    }
}
