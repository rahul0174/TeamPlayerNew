package com.cts.teamplayer.util

/**
 * Created by user on 3/6/2018.
 */

interface DrawableClickListener {

    enum class DrawablePosition {
        TOP, BOTTOM, START, END
    }

    fun onClick(target: DrawablePosition)
}