
package com.appyhigh.mylibrary.misc

import android.Manifest.permission
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Looper
import android.os.Vibrator
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

@RequiresPermission(permission.ACCESS_NETWORK_STATE)
fun Context.isConnectedToNetwork(): Boolean {
    val connectivityManager = this.connectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo?.isConnected ?: false
}

fun Context.getStringRes(@StringRes resId: Int): String {
    return this.getString(resId) ?: ""
}

fun Context.getColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.syncCookieWithSystemBrowser() {
    if (VersionUtils.isLollipopOrUp()) {
        CookieManager.getInstance().flush()
    } else {
        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().startSync()
        CookieSyncManager.getInstance().sync()
    }
}

fun Context.dial(tel: String?) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel")))

fun Context.sms(phone: String?, body: String = "") {
    val smsToUri = Uri.parse("smsto:$phone")
    val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
    intent.putExtra("sms_body", body)
    startActivity(intent)
}

fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

// System Services
inline val Context.connectivityManager: ConnectivityManager
    get() = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

inline val Context.alarmManager: AlarmManager
    get() = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

inline val Context.telephonyManager: TelephonyManager
    get() = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

inline val Context.activityManager: ActivityManager
    get() = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

inline val Context.notificationManager: NotificationManager
    get() = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.appWidgetManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = applicationContext.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager

inline val Context.inputMethodManager: InputMethodManager
    get() = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.clipboardManager
    get() = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

inline val Context.bluetoothManager: BluetoothManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    get() = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

inline val Context.audioManager
    get() = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

inline val Context.batteryManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

inline val Context.cameraManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = applicationContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

inline val Context.vibrator
    get() = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

fun Context.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
