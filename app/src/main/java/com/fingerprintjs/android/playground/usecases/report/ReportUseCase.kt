package com.fingerprintjs.android.playground.usecases.report

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val reportAttachmentCreator: ReportAttachmentCreator,
    private val application: Application,
) {
    /**
     * @return path to file in external dir
     */
    suspend fun createReportAttachment(): String? = withContext(Dispatchers.IO) {
        try {
            val reportsDirPath = application.getExternalFilesDir(null)?.absolutePath?.let {
                "$it/reports"
            }
            val reportsDir = File(reportsDirPath!!)
            if (reportsDir.exists()) {
                reportsDir.deleteRecursively()
            }
            reportsDir.mkdirs()
            val reportFilePath = "$reportsDirPath/attachment.json"
            val reportFile = File(reportFilePath)
            val json = reportAttachmentCreator.createReportAttachment()
            reportFile.writeText(json)
            reportFilePath
        } catch (t: Throwable) {
            null
        }
    }
}
