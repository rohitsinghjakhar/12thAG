package com.roni.class12thagjetnotes.students.viewers

import android.media.browse.MediaBrowser
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.roni.class12thagjetnotes.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.MediaItem
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null
    private var videoUrl: String = ""
    private var videoTitle: String = ""
    private var playWhenReady = true
    private var currentPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoUrl = intent.getStringExtra("video_url") ?: ""
        videoTitle = intent.getStringExtra("title") ?: "Video"

        setupToolbar()

        if (videoUrl.isNotEmpty()) {
            initializePlayer()
        } else {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = videoTitle
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            // Convert YouTube URL if needed
            val finalUrl = convertYouTubeUrl(videoUrl)
            val mediaItem = MediaItem.fromUri(finalUrl)
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentPosition)
            exoPlayer.prepare()

            // Add listener for player events
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Player.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        Player.STATE_ENDED -> {
                            // Video ended
                        }
                        Player.STATE_IDLE -> {
                            // Player idle
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@VideoPlayerActivity,
                        "Error playing video: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun convertYouTubeUrl(url: String): String {
        // If it's a YouTube URL, you might want to extract the video ID and use it with YouTube Player API
        // For now, we'll keep it simple
        return when {
            url.contains("youtube.com/watch?v=") -> {
                // Extract video ID from standard YouTube URL
                val videoId = url.substringAfter("watch?v=").substringBefore("&")
                // For ExoPlayer, you would need to use YouTube Extract or similar
                url // Return original for now
            }
            url.contains("youtu.be/") -> {
                // Extract video ID from short YouTube URL
                val videoId = url.substringAfter("youtu.be/").substringBefore("?")
                url // Return original for now
            }
            else -> url
        }
    }

    override fun onStart() {
        super.onStart()
        if (player == null) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playWhenReady = exoPlayer.playWhenReady
            currentPosition = exoPlayer.currentPosition
            exoPlayer.release()
        }
        player = null
    }
}