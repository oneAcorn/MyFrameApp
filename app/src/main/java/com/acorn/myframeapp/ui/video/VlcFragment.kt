package com.acorn.myframeapp.ui.video

import android.net.Uri
import android.view.View
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
        playVideo("rtsp://admin:wcy12345@192.168.8.203", mMediaPlayer)
    }

    private fun initPlayerView() {
        mLibVLC = generateLibVlc()
        mMediaPlayer = generateMediaPlayer(mLibVLC!!, vlcLayout)
        mMediaPlayer?.setEventListener { event ->
            //这两个一直在回调,不log
            if (event.type != MediaPlayer.Event.TimeChanged && event.type != MediaPlayer.Event.PositionChanged) {
                logI("VLC Event:$event,${event.type}")
            }
            when (event.type) {
                MediaPlayer.Event.Buffering -> {
                    pb.visibility = View.VISIBLE
                }
                else -> {
                    pb.visibility = View.GONE
                }
            }
        }
    }

    private fun generateLibVlc(): LibVLC {
        //参考https://www.cnblogs.com/saryli/p/5047924.html
        return LibVLC(requireContext(), ArrayList<String>().apply {
            add("--drop-late-frames") //丢弃晚的帧
            add("--skip-frames") //跳过帧
            add("--rtsp-tcp")
//            add("--realrtsp-caching 150") //realRtsp 缓存时间(毫秒)
            add("--file-caching=2000") //低延迟设置
            add("--low-delay")  //低延迟设置
            add("--no-audio") //关闭音频
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
            //低延迟设置
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
            mMediaPlayer?.attachViews(vlcLayout, null, true, false)
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