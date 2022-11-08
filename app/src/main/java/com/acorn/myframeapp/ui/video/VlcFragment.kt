package com.acorn.myframeapp.ui.video

import android.net.Uri
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.fragment_vlc.*
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

/**
 * Created by acorn on 2022/11/7.
 */
class VlcFragment : BaseFragment<BaseNetViewModel>() {
    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var isPause = false

    override fun initView() {
        super.initView()
        initPlayerView()
    }

    override fun initData() {
        super.initData()
        //"rtmp://ns8.indexforce.com/home/mystream"
        //"rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp"
        playVideo("rtmp://ns8.indexforce.com/home/mystream", mMediaPlayer)
    }

    private fun initPlayerView() {
        mLibVLC = generateLibVlc()
        mMediaPlayer = generateMediaPlayer(mLibVLC!!, vlcLayout)
    }

    private fun generateLibVlc(): LibVLC {
        return LibVLC(requireContext(), ArrayList<String>().apply {
            add("--no-drop-late-frames")
            add("--no-skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
    }

    private fun generateMediaPlayer(libVlc: LibVLC, layout: VLCVideoLayout): MediaPlayer {
        val mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer.attachViews(layout, null, true, false)
        return mediaPlayer
    }

    private fun playVideo(url: String, mediaPlayer: MediaPlayer?) {
        Media(mLibVLC, Uri.parse(url)).apply {
            setHWDecoderEnabled(true, false);
            addOption(":network-caching=150");
            addOption(":clock-jitter=0");
            addOption(":clock-synchro=0");
            mediaPlayer?.media = this
        }.release()
        mediaPlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer?.pause()
        isPause = true
//        logI("onPause")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume:$isPause,${mMediaPlayer?.isPlaying},")
        if (isPause) {
            mMediaPlayer?.attachViews(vlcLayout,null,true,false)
            mMediaPlayer?.play()
            isPause = false
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.stop()
        mMediaPlayer?.detachViews()
        mMediaPlayer?.release()
        mLibVLC?.release()
    }

    override fun getLayoutId(): Int = R.layout.fragment_vlc

    override fun getViewModel(): BaseNetViewModel? = null
}