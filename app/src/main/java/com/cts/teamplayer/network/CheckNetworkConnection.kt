package com.cts.teamplayer.network


import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import com.cts.teamplayer.R
import com.cts.teamplayer.util.FunctionHelper
import com.cts.teamplayer.util.MyConstants.BASE_URL

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


/**
 * @author RAM
 *
 *
 * This class checks device is connected with internet or not.
 */
object CheckNetworkConnection {
    fun isReachable(ctx: Context, view: View): Boolean {
        return isReachable(ctx, view, true)
    }


    @JvmOverloads
    fun isConnection(ctx: Context, view: View, showToast: Boolean = true): Boolean {
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = connectivityManager.activeNetworkInfo
        if (ni != null && ni.isAvailable && ni.isConnected) {
            return true
        } else {
            if (showToast)
                FunctionHelper.showSnackMessage(view, R.string.please_check_internet)

            //Toast.makeText(ctx, R.string.please_check_internet, Toast.LENGTH_SHORT).show();

            return false
        }
    }


    fun isConnection1(ctx: Context, showToast: Boolean = true): Boolean {
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = connectivityManager.activeNetworkInfo
        if (ni != null && ni.isAvailable && ni.isConnected) {
            return true
        } else {
            if (showToast)
                Toast.makeText(ctx, R.string.please_check_internet, Toast.LENGTH_SHORT).show()

            return false
        }
    }


    private fun isReachable(ctx: Context, view: View, showToast: Boolean): Boolean {
        var connected = false
        val instanceURL = BASE_URL
        var socket: Socket?
        try {
            socket = Socket()
            val socketAddress = InetSocketAddress(instanceURL, 80)
            socket.connect(socketAddress, 5000)
            if (socket.isConnected) {
                connected = true
                socket.close()
            } else {
                if (showToast)
                    FunctionHelper.showSnackMessage(view, "Server Not Responding")
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            socket = null
        }
        return connected
    }


}