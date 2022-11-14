package com.fingerprintjs.android.playground.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerprintjs.android.playground.constants.Constants
import com.fingerprintjs.android.playground.usecases.report.ReportUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val reportUseCase: ReportUseCase,
) : ViewModel() {
    private val externalLinkToOpenMutable = MutableSharedFlow<String>()
    val externalLinkToOpen: Flow<String>
        get() = externalLinkToOpenMutable

    private val sendReportEventMutable = MutableSharedFlow<String?>()
    val sendReportEvent: Flow<String?>
        get() = sendReportEventMutable

    fun onAppSourceCodeClicked() = launchUrl(Constants.Urls.FP_OSS_SOURCE_CODE)
    fun onTryFpProDemoClicked() = launchUrl(Constants.Urls.FP_PRO_DEMO)
    fun onFpProAccuracyClicked() = launchUrl(Constants.Urls.FP_PRO_ACCURACY)
    fun onGithubClicked() = launchUrl(Constants.Urls.FP_GITHUB)
    fun onLinkedinClicked() = launchUrl(Constants.Urls.FP_LINKEDIN)
    fun onTwitterClicked() = launchUrl(Constants.Urls.FP_TWITTER)
    fun onFingerprintComClicked() = launchUrl(Constants.Urls.FP_COM)
    fun onDeviceIdAndFingerprintDifferenceBannerClicked() = launchUrl(Constants.Urls.FP_DEVICE_ID_FINGERPRINT_DIFFERENCE)
    fun onTryFingerprintProDemoBannerClicked() = launchUrl(Constants.Urls.FP_PRO_DEMO)

    fun onReportIssueClicked() {
        viewModelScope.launch {
            sendReportEventMutable.emit(reportUseCase.createReportAttachment())
        }
    }

    private fun launchUrl(url: String) {
        viewModelScope.launch { externalLinkToOpenMutable.emit(url) }
    }
}
