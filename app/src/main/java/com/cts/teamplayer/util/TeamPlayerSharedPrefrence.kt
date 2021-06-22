package com.cts.teamplayer.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/* Created by Ram Bhawan 20/11/18.
         */

class TeamPlayerSharedPrefrence  constructor() {


    private enum class Keys private constructor(val label: String) {
        LOGIN("LOGIN"),
        USERNAME("user_name"), ACCESS_TOKEN("access_token"),BUSINESS_NAME("business-name"), EMAIL("email_id"), LANGUAGE("language"), APPTOKEN("app_token"), FCMKEY("fcm_key"), USERID(
            "user_id"),
        ROAL("roal"),SITE_ID("site_id"),SITE_IMG_URL("site_image_url"), USERTYPE("user_type"), GOOGLE_ID("google_id"), PHONE("phone"), USERPROFILE("user_pic")
    }

    /**
     * This Method Clear shared preference.
     */
    fun clear() {
        val editor = _pref!!.edit()
        editor.clear()
        editor.commit()
    }

    fun setGoogleID(value: String) {
        setString(Keys.GOOGLE_ID.label, value)
    }


    fun getGoogleID(defaultValue: String): String? {
        return getString(Keys.GOOGLE_ID.label, defaultValue)
    }

    fun setEmail(value: String) {
        setString(Keys.EMAIL.label, value)
    }


    fun getEmail(defaultValue: String): String? {
        return getString(Keys.EMAIL.label, defaultValue)
    }


    fun setPhone(value: String) {
        setString(Keys.PHONE.label, value)
    }


    fun getPhone(defaultValue: String): String? {
        return getString(Keys.PHONE.label, defaultValue)
    }


    fun setLanguage(value: String) {
        setString(Keys.LANGUAGE.label, value)
    }


    fun getLanguage(defaultValue: String): String? {
        return getString(Keys.LANGUAGE.label, defaultValue)
    }

    fun setSiteImageUrl(value: String) {
        setString(Keys.SITE_IMG_URL.label, value)
    }


    fun getSiteImageUrl(defaultValue: String): String? {
        return getString(Keys.SITE_IMG_URL.label, defaultValue)
    }


    fun setAppToken(value: String) {
        setString(Keys.APPTOKEN.label, value)
    }


    fun getAppToken(defaultValue: String): String? {
        return getString(Keys.APPTOKEN.label, defaultValue)
    }

    fun setBusinessName(value: String) {
        setString(Keys.BUSINESS_NAME.label, value)
    }


    fun getBusinessName(defaultValue: String): String? {
        return getString(Keys.BUSINESS_NAME.label, defaultValue)
    }

    fun setSiteId(value: String) {
        setString(Keys.SITE_ID.label, value)
    }


    fun getSiteId(defaultValue: String): String? {
        return getString(Keys.SITE_ID.label, defaultValue)
    }

    fun setAccessToken(value: String) {
        setString(Keys.ACCESS_TOKEN.label, value)
    }


    fun getAccessToken(defaultValue: String): String? {
        return getString(Keys.ACCESS_TOKEN.label, defaultValue)
    }
    fun setFcmKey(value: String) {
        setString(Keys.FCMKEY.label, value)
    }


    fun getFcmKey(defaultValue: String): String? {
        return getString(Keys.FCMKEY.label, defaultValue)
    }


    fun setUSERID(value: String) {
        setString(Keys.USERID.label, value)
    }


    fun getUSERID(defaultValue: String): String? {
        return getString(Keys.USERID.label, defaultValue)
    }

    fun setRoal(value: String) {
        setString(Keys.ROAL.label, value)
    }


    fun getRoal(defaultValue: String): String? {
        return getString(Keys.ROAL.label, defaultValue)
    }


    fun setUserType(value: String) {
        setString(Keys.USERTYPE.label, value)
    }


    fun getUserType(defaultValue: String): String? {
        return getString(Keys.USERTYPE.label, defaultValue)
    }

    fun setUserProfile(value: String) {
        setString(Keys.USERPROFILE.label, value)
    }


    fun getUserProfile(defaultValue: String): String? {
        return getString(Keys.USERPROFILE.label, defaultValue)
    }


    fun setLogin(value: String) {
        setString(Keys.LOGIN.label, value)
    }

    fun getLogin(defaultValue: String): String? {
        return getString(Keys.LOGIN.label, defaultValue)
    }

    fun setUserName(value: String) {
        setString(Keys.USERNAME.label, value)
    }

    fun getUserName(defaultValue: String): String? {
        return getString(Keys.USERNAME.label, defaultValue)
    }

    private fun setString(key: String?, value: String?) {
        if (key != null && value != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putString(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }
    }

    private fun setInt(key: String?, value: Int) {
        if (key != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putInt(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }
    }

    private fun setLong(key: String?, value: Long?) {
        if (key != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putLong(key, value!!)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }
    }

    private fun getString(key: String?, defaultValue: String): String? {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getString(key, defaultValue)
        } else defaultValue
    }

    private fun getInt(key: String?, defaultValue: Int): Int {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getInt(key, defaultValue)
        } else defaultValue
    }

    private fun getLong(key: String?, defaultValue: Long): Long {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getLong(key, defaultValue)
        } else defaultValue
    }

    private fun setBoolean(key: String?, value: Boolean) {
        if (key != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putBoolean(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }
    }

    private fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getBoolean(key, defaultValue)
        } else defaultValue
    }

    companion object {
        val TAG = TeamPlayerSharedPrefrence::class.java.name
        private var _pref: SharedPreferences? = null
        private var _instance: TeamPlayerSharedPrefrence? = null
        private val PRIVATE_MODE = 0
        val SHARED_PREF_NAME = "RMC_"

        fun getInstance(context: Context): TeamPlayerSharedPrefrence {
            if (_pref == null) {
                _pref = context
                    .getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE)
            }
            if (_instance == null) {
                _instance = TeamPlayerSharedPrefrence()
            }
            return _instance as TeamPlayerSharedPrefrence
        }
    }

}