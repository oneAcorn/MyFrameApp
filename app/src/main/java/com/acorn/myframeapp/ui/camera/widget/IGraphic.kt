package com.acorn.myframeapp.ui.camera.widget

/**
 * Created by acorn on 2023/2/1.
 */
interface IGraphic {
    /**
     * Adjusts the supplied value from the image scale to the view scale.
     */
    fun scale(imagePixel: Float): Float

    /**
     * Adjusts the x coordinate from the image's coordinate system to the view coordinate system.
     */
    fun translateX(x: Float): Float

    /**
     * Adjusts the y coordinate from the image's coordinate system to the view coordinate system.
     */
    fun translateY(y: Float): Float
}