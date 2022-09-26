package com.acorn.myframeapp.ui.exoplayer

import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.android.synthetic.main.activity_exo_player.*

/**
 * Created by acorn on 2022/9/26.
 */
class ExoPlayerActivity : BaseNoViewModelActivity() {
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
    }

    override fun initView() {
        super.initView()
        initExo()
    }

    private fun initExo() {
        exoPlayer = ExoPlayer.Builder(this).build()
        // Attach player to the view.
        playerView.player = exoPlayer
        // Set the media item to be played.
        exoPlayer.setMediaItem(MediaItem.fromUri("rtsp://admin:wcy12345@192.168.0.203"))
        // Prepare the player.
        exoPlayer.prepare()
    }
}