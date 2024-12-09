package com.fingerprintjs.android.fingerprint.device_id_providers


import android.content.ContentResolver
import android.net.Uri
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public class GsfIdProvider(
    private val contentResolver: ContentResolver?
) {

    public fun getGsfAndroidId(): String? {
        return safeWithTimeout {
            getGsfId()
        }.getOrDefault("")
    }

    private fun getGsfId(): String? {
        val URI = Uri.parse(URI_GSF_CONTENT_PROVIDER)
        val params = arrayOf(ID_KEY)
        return try {
            contentResolver!!.query(URI, null, null, params, null)!!.use { cursor ->
                check(cursor.moveToFirst() && cursor.columnCount >= 2)
                java.lang.Long.toHexString(cursor.getString(1).toLong())
            }
        } catch (_: Exception) {
            null
        }
    }
}

private const val URI_GSF_CONTENT_PROVIDER = "content://com.google.android.gsf.gservices"
private const val ID_KEY = "android_id"