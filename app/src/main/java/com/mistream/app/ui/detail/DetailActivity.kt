package com.mistream.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.mistream.app.data.model.StreamItem
import com.mistream.app.databinding.ActivityDetailBinding
import com.mistream.app.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : FragmentActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
        const val EXTRA_MEDIA_TYPE = "extra_media_type"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        val mediaType = intent.getStringExtra(EXTRA_MEDIA_TYPE) ?: "movie"

        setupStreamsRecyclerView()
        observeViewModel()
        viewModel.loadDetail(movieId, mediaType)
    }

    private fun setupStreamsRecyclerView() {
        binding.rvStreams.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = StreamsAdapter { stream -> onStreamSelected(stream) }
        }
    }

    private fun onStreamSelected(stream: StreamItem) {
        if (stream.url != null && !stream.isRealDebrid) {
            launchPlayer(stream.url, viewModel.detail.value?.displayTitle ?: "")
        } else {
            viewModel.resolveStream(stream)
        }
    }

    private fun launchPlayer(url: String, title: String) {
        startActivity(
            Intent(this, PlayerActivity::class.java)
                .putExtra(PlayerActivity.EXTRA_STREAM_URL, url)
                .putExtra(PlayerActivity.EXTRA_TITLE, title)
        )
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.detail.collect { detail ->
                detail ?: return@collect
                binding.tvTitle.text = detail.displayTitle
                binding.tvOverview.text = detail.overview
                binding.tvRating.text = "★ ${"%.1f".format(detail.voteAverage)}"
                binding.tvYear.text = detail.displayDate.take(4)
                binding.tvGenres.text = detail.genres.joinToString(", ") { it.name }
                binding.ivBackdrop.load(detail.backdropUrl) { crossfade(true) }
                binding.ivPoster.load(detail.posterUrl) { crossfade(true) }
            }
        }
        lifecycleScope.launch {
            viewModel.streams.collect { streams ->
                (binding.rvStreams.adapter as? StreamsAdapter)?.submitList(streams)
                if (streams.isNotEmpty()) {
                    val best = streams.first()
                    val lang = when { best.isLatino -> " Latino"; best.isCastellano -> " Castellano"; else -> "" }
                    binding.btnPlayBest.text = "▶ Reproducir:$lang ${best.quality}"
                    binding.btnPlayBest.visibility = View.VISIBLE
                    binding.btnPlayBest.setOnClickListener { onStreamSelected(best) }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoadingStreams.collect { loading ->
                binding.progressStreams.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launch {
            viewModel.streamStatus.collect { status ->
                binding.tvStreamStatus.text = status
            }
        }
        lifecycleScope.launch {
            viewModel.resolvedUrl.collect { url ->
                launchPlayer(url, viewModel.detail.value?.displayTitle ?: "")
            }
        }
    }
}
