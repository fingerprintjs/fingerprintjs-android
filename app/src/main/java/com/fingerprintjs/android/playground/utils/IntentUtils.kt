package com.fingerprintjs.android.playground.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.fingerprintjs.android.playground.BuildConfig
import com.fingerprintjs.android.playground.constants.Constants.DEVELOPERS_EMAIL
import java.io.File

object IntentUtils {
    fun openUrl(activity: Activity, url: String) {
        runCatching {
            val uri = Uri.parse(url)!!
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
        }.onFailure {
            Toast.makeText(
                activity,
                "Error opening $url",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendReport(activity: Activity, reportAttachmentPath: String?) {
        val uri = if (reportAttachmentPath != null)
            FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", File(reportAttachmentPath))
        else null
        val sendEmailIntent = Intent(
            Intent.ACTION_SEND, Uri.fromParts(
                "mailto", "", null
            )
        ).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPERS_EMAIL))
            if (uri != null) {
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        if (sendEmailIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(sendEmailIntent)
        }
    }
}
