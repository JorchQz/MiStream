package com.mistream.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mistream.app.data.model.TmdbMovie
import com.mistream.app.databinding.ItemSearchResultBinding

class SearchResultsAdapter(
    private val onItemClick: (TmdbMovie) -> Unit
) : ListAdapter<TmdbMovie, SearchResultsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: TmdbMovie) {
            binding.tvTitle.text = movie.displayTitle
            binding.tvYear.text = movie.displayDate.take(4)
            binding.tvType.text = when (movie.mediaType) {
                "movie" -> "Película"
                "tv" -> "Serie"
                else -> if (movie.title.isNotEmpty()) "Película" else "Serie"
            }
            binding.tvRating.text = if (movie.voteAverage > 0) "★ ${"%.1f".format(movie.voteAverage)}" else ""
            binding.ivPoster.load(movie.posterUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_gallery)
            }
            binding.root.setOnClickListener { onItemClick(movie) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TmdbMovie>() {
            override fun areItemsTheSame(old: TmdbMovie, new: TmdbMovie) = old.id == new.id
            override fun areContentsTheSame(old: TmdbMovie, new: TmdbMovie) = old == new
        }
    }
}
