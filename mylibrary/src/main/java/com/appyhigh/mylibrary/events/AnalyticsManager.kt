package com.appyhigh.mylibrary.events

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.File
import java.util.*

object AnalyticsManager {
    private val binaryPlaces = arrayOf(
        "/data/bin/", "/system/bin/", "/system/xbin/", "/sbin/",
        "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/",
        "/data/local/"
    )
    private lateinit var sAppContext: Context
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var cleverTapDefaultInstance: CleverTapAPI

    private fun canSend(): Boolean {
        return true
    }

    private fun canPush(): Boolean {
        return true
    }

    @Synchronized
    fun initialize(context: Context) {
        try {
            sAppContext = context
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            try {
                cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(context)!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            /*ApxorSDK.initialize(R.string.apxor_id, context)*/
            setProperty(
                "DeviceType",
                getDeviceType(context)
            )
            setProperty(
                "Rooted",
                java.lang.Boolean.toString(isRooted())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setProperty(
        propertyName: String,
        propertyValue: String
    ) {
        if (canSend()) {
            mFirebaseAnalytics.setUserProperty(propertyName, propertyValue)
        }
    }

    fun logEvent(eventName: String) {
        try {
            if (canSend()) {
                mFirebaseAnalytics.logEvent(eventName, Bundle())
                pushCTEvent(eventName)
                /*apxorLogEvent(eventName)*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logEvent(eventName: String, params: Bundle) {
        try {
            if (canSend()) {
                mFirebaseAnalytics.logEvent(eventName, params)
                pushCTEventWithParams(
                    eventName,
                    bundleToMap(params)
                )
                /* apxorLogEventWithParams(
                     eventName,
                     getAttributes(params)
                 )*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bundleToMap(extras: Bundle): HashMap<String?, Any?>? {
        val map =
            HashMap<String?, Any?>()
        try {
            val ks = extras.keySet()
            for (key in ks) {
                map[key] = extras.getString(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }

    fun setCurrentScreen(
        activity: Activity,
        screenName: String?
    ) {
        if (canSend()) {
            if (null != screenName) {
                mFirebaseAnalytics.setCurrentScreen(activity, screenName, screenName)
                cleverTapDefaultInstance.recordScreen(screenName)
                /*ApxorSDK.trackScreen(screenName)*/
            }
        }
    }

    private fun getDeviceType(c: Context): String {
        try {
            val uiModeManager = c.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            return when (uiModeManager.currentModeType) {
                Configuration.UI_MODE_TYPE_TELEVISION -> "TELEVISION"
                Configuration.UI_MODE_TYPE_WATCH -> "WATCH"
                Configuration.UI_MODE_TYPE_NORMAL -> if (isTablet(
                        c
                    )
                ) "TABLET" else "PHONE"
                Configuration.UI_MODE_TYPE_UNDEFINED -> "UNKOWN"
                else -> ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "UNKOWN"
        }
    }

    private fun isRooted(): Boolean {
        for (p in binaryPlaces) {
            val su = File(p + "su")
            if (su.exists()) {
                return true
            }
        }
        return false
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources.configuration.smallestScreenWidthDp >= 600
    }

    private fun pushCTEvent(eventName: String?) {
        if (canPush()) {
            cleverTapDefaultInstance.pushEvent(eventName)
        }
    }

    private fun pushCTEventWithParams(
        eventName: String?,
        hashMap: HashMap<String?, Any?>?
    ) {
        if (canPush()) {
            cleverTapDefaultInstance.pushEvent(eventName, hashMap)
        }
    }

/*
    private fun apxorLogEvent(eventName: String?) {
        if (canSend()) {
            ApxorSDK.logAppEvent(eventName)
        }
    }
*/

/*
    private fun apxorLogEventWithParams(
        eventName: String?,
        attributes: Attributes?
    ) {
        if (canSend()) {
            ApxorSDK.logAppEvent(eventName, attributes)
        }
    }
*/

/*
    private fun getAttributes(bundle: Bundle): Attributes {
        val attributes = Attributes()
        try {
            for (key in bundle.keySet()) {
                attributes.putAttribute(key, bundle[key].toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return attributes
    }
*/

    fun pushCTProfile(hashMap: HashMap<String?, Any?>?) {
        cleverTapDefaultInstance.pushProfile(hashMap)
    }
}