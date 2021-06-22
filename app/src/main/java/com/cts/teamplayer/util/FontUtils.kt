package com.cts.teamplayer.util

import android.content.Context
import android.graphics.Typeface
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView

/**
 * Created by JeannyPrAndroid on 08-Sep-17.
 */

object FontUtils {

    /**
     * @param contxt
     * @param textView
     */
    fun setFontDenkOneRegular(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/DenkOne-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setFontRobotoBold(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Roboto-Bold.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setFontRobotoLight(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Roboto-Light.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setFontDinerDot(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/FontdinerdotcomLoungy.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    /**
     * @param contxt
     * @param textView
     */
    fun setFontRobotoRegular(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Roboto-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setFontopensensRegular(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/OpenSans-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setPoppinsRegular(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Poppins-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setPoppinsRelarEditText(contxt: Context, vararg textView: EditText) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Poppins-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

    fun setPoppinsRelarAutocomplete(contxt: Context, vararg textView: AutoCompleteTextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/Poppins-Regular.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }


    /*
    public static void setFontopensensRegularForEdit(Context contxt, TextInputLayout... textView)
    {
        Typeface face = Typeface.createFromAsset(contxt.getAssets(), "fonts/OpenSans-Regular.ttf");
        for (TextInputLayout tv : textView)
        {
            tv.setTypeface(face);
        }
    }*/

    fun setFontOpenSansBold(contxt: Context, vararg textView: TextView) {
        val face = Typeface.createFromAsset(contxt.assets, "fonts/OpenSans-Bold.ttf")
        for (tv in textView) {
            tv.typeface = face
        }
    }

}
