package com.acorn.myframeapp.ui.video

import android.os.Bundle
import android.os.Looper
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
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
        exoPlayer.setMediaItem(MediaItem.fromUri("webrtc://192.168.0.210/01/front/ai?eip=192.168.0.210"))
        exoPlayer.addListener(object :Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                //like Player.STATE_BUFFERING(4种)
//                Player.STATE_ENDED
                logI("stateChanged:$playbackState")
                if(playbackState==Player.STATE_ENDED){
                    exoPlayer.prepare()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                logI("onPlayerError ${error.message}")
            }
        })
        //Fire Event,特定时间点发送Event
        exoPlayer.createMessage { messageType, message ->
            logI("msg:$messageType,$message")
        }
            .setLooper(Looper.getMainLooper())
            .setPosition(5000) //5秒位置发送消息
            .setPayload("payloads message")
            .setDeleteAfterDelivery(true)
            .send()
        // Prepare the player.
        exoPlayer.prepare()
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }
}