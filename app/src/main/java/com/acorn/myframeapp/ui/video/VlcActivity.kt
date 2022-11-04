package com.acorn.myframeapp.ui.video

import android.net.Uri
import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.activity_vlc.*
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

/**
 * Created by acorn on 2022/11/4.
 */
class VlcActivity : BaseNoViewModelActivity() {
    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vlc)
    }

    override fun initView() {
        super.initView()
        initPlayerView()
    }

    private fun initPlayerView() {
        mLibVLC = LibVLC(this, ArrayList<String>().apply {
            add("--no-drop-late-frames")
            add("--no-skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
        mMediaPlayer = MediaPlayer(mLibVLC)
        mMediaPlayer?.attachViews(vlcLayout, null, true, false)
    }

    override fun initData() {
        super.initData()
        Media(mLibVLC, Uri.parse("http://192.168.0.155/res/video/02010001.mp4")).apply {
            setHWDecoderEnabled(true, false);
            addOption(":network-caching=150");
            addOption(":clock-jitter=0");
            addOption(":clock-synchro=0");
            mMediaPlayer?.media = this
        }.release()
        mMediaPlayer?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.stop()
        mMediaPlayer?.detachViews()
        mMediaPlayer?.release()
        mLibVLC?.release()
    }
}