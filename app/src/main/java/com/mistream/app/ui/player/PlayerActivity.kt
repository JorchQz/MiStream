package com.mistream.app.ui.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mistream.app.databinding.ActivityPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : FragmentActivity() {

    companion object {
        const val EXTRA_STREAM_URL = "extra_stream_url"
        const val EXTRA_TITLE = "extra_title"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_STREAM_URL) ?: run { finish(); return }
        binding.tvPlayerTitle.text = intent.getStringExtra(EXTRA_TITLE) ?: ""
        initPlayer(url)
    }

    private fun initPlayer(url: String) {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    binding.progressBuffering.visibility =
                        if (state == Player.STATE_BUFFERING) View.VISIBLE else View.GONE
                    if (state == Player.STATE_READY) binding.tvError.visibility = View.GONE
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.tvError.text = "Error de reproducción: ${error.localizedMessage}"
                    binding.tvError.visibility = View.VISIBLE
                    binding.progressBuffering.visibility = View.GONE
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}
