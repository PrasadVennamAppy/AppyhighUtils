
package com.appyhigh.mylibrary.misc

import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream


fun ByteArray.saveFile(path: String) {
    val file = File(path)
    if (!file.exists()) {
        file.createNewFile()
    }
    val output = FileOutputStream(file)
    output.write(this)
    output.flush()
    output.close()
}

fun ByteArray.toBitmap(opts: BitmapFactory.Options? = null) = BitmapFactory.decodeByteArray(this, 0, size, opts)

fun ByteArray.toHex(): String {
    var des = ""
    var tmp: String
    for (i in this.indices) {
        tmp = Integer.toHexString(this[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}