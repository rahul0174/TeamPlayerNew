package com.cts.teamplayer.util

import android.content.Context
import android.graphics.Typeface

import java.util.HashMap

/* Created by Ram Bhawan 19/11/18.
 */

object FontCache {
    private val fontCache = HashMap<String, Typeface>()

    fun getTypeface(fontname: String, context: Context): Typeface? {
        var typeface = fontCache[fontname]

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$fontname")
            } catch (e: Exception) {
                /*getContext().getAssets(), "fonts/" + fontName*/
                return null
            }

            fontCache[fontname] = typeface
        }

        return typeface
    }

}

