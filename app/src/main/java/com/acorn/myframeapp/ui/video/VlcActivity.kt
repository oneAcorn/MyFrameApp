package com.acorn.myframeapp.ui.video

import android.net.Uri
import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.activity_vlc.*
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

/**
 * Created by acorn on 2022/11/4.
 */
class VlcActivity : BaseNoViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vlc)
    }

}