package com.roni.class12thagjetnotes.students.viewers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.storage.FirebaseStorage
import com.roni.class12thagjetnotes.databinding.ActivityVideoPlayerBinding

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
            handleVideoSource(videoUrl)
        } else {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = videoTitle
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    /**
     * Determine what kind of URL this is (Firebase Storage, YouTube, or normal HTTP)
     */
    private fun handleVideoSource(url: String) {
        when {
            url.startsWith("gs://") -> {
                // Firebase Storage URL
                loadFirebaseVideo(url)
            }
            url.contains("youtube.com") || url.contains("youtu.be") -> {
                // YouTube URL
                openYouTubeVideo(url)
            }
            url.startsWith("http") || url.startsWith("https") -> {
                // Normal web video
                initializePlayer(url)
            }
            else -> {
                Toast.makeText(this, "Unsupported video source", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    /**
     * Convert Firebase Storage gs:// link to HTTPS download URL
     */
    private fun loadFirebaseVideo(gsUrl: String) {
        try {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(gsUrl)
            binding.progressBar.visibility = View.VISIBLE

            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    val httpsUrl = uri.toString()
                    initializePlayer(httpsUrl)
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to load video: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }

        } catch (e: Exception) {
            Toast.makeText(this, "Invalid Firebase URL", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Open YouTube video in external player (YouTube app or browser)
     */
    private fun openYouTubeVideo(youtubeUrl: String) {
        Toast.makeText(this, "Opening YouTube video...", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        startActivity(intent)
        finish()
    }

    /**
     * Initialize and prepare ExoPlayer for direct HTTP/HTTPS video URLs
     */
    private fun initializePlayer(finalUrl: String) {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(finalUrl)
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentPosition)
            exoPlayer.prepare()

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
                        Player.STATE_READY -> binding.progressBar.visibility = View.GONE
                        Player.STATE_ENDED -> Unit
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@VideoPlayerActivity,
                        "Playback error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
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

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playWhenReady = exoPlayer.playWhenReady
            currentPosition = exoPlayer.currentPosition
            exoPlayer.release()
        }
        player = null
    }
}
