package com.fingerprintjs.android.fingerprint.device_id_providers


import android.media.MediaDrm
import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.security.MessageDigest
import java.util.UUID


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public class MediaDrmIdProvider {
    public fun getMediaDrmId(): String? = executeSafe({
        mediaDrmId()
    }, null)

    private fun mediaDrmId(): String {
        val widevineUUID = UUID(WIDEWINE_UUID_MOST_SIG_BITS, WIDEWINE_UUID_LEAST_SIG_BITS)
        val wvDrm: MediaDrm?

        wvDrm = MediaDrm(widevineUUID)
        val mivevineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
        releaseMediaDRM(wvDrm)
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        md.update(mivevineId)

        return md.digest().toHexString()
    }

    private fun releaseMediaDRM(drmObject: MediaDrm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            drmObject.close()
        } else {
            drmObject.release()
        }
    }
}

private fun ByteArray.toHexString(): String {
    return this.joinToString("") {
        java.lang.String.format("%02x", it)
    }
}

private const val WIDEWINE_UUID_MOST_SIG_BITS = -0x121074568629b532L
private const val WIDEWINE_UUID_LEAST_SIG_BITS = -0x5c37d8232ae2de13L