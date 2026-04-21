package com.mistream.app.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mistream.app.data.model.StreamItem
import com.mistream.app.databinding.ItemStreamBinding

class StreamsAdapter(
    private val onStreamClick: (StreamItem) -> Unit
) : ListAdapter<StreamItem, StreamsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemStreamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stream: StreamItem) {
            binding.tvQuality.text = buildString {
                when {
                    stream.isLatino -> append("Latino ")
                    stream.isCastellano -> append("Castellano ")
                }
                append(stream.quality)
            }
            binding.tvRdBadge.visibility = if (stream.isRealDebrid) View.VISIBLE else View.GONE
            binding.tvSource.text = stream.name.lines().firstOrNull()?.trim() ?: stream.name
            binding.tvInfo.text = stream.title.lines().firstOrNull()?.trim() ?: ""
            binding.root.setOnClickListener { onStreamClick(stream) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StreamItem>() {
            override fun areItemsTheSame(old: StreamItem, new: StreamItem) =
                old.url == new.url && old.infoHash == new.infoHash
            override fun areContentsTheSame(old: StreamItem, new: StreamItem) = old == new
        }
    }
}
