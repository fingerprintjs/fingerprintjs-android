package com.fingerprintjs.android.playground.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdScreenState
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintScreenState
import com.fingerprintjs.android.playground.utils.getDeviceId
import com.fingerprintjs.android.playground.utils.getFingerprint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val fingerprinter: Fingerprinter,
) : ViewModel() {

    private val fingerprintScreenState: StateFlow<FingerprintScreenState>
        get() = fingerprintScreenStateMutable
    private val fingerprintScreenStateMutable = MutableStateFlow(createFingerprintScreenInitialState())

    private val deviceIdScreenState: StateFlow<DeviceIdScreenState>
        get() = deviceIdScreenStateMutable
    private val deviceIdScreenStateMutable = MutableStateFlow(createDeviceIdScreenInitialState())

    val homeScreenState: StateFlow<HomeScreenState?> = combineTransform(
        fingerprintScreenState,
        deviceIdScreenState
    ) { v1, v2 ->
        emit(
            HomeScreenState(
                fingerprintScreenState = v1,
                deviceIdScreenState = v2
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private var updateDeviceIdJob: Job? = null
    private var updateFingerprintJob: Job? = null

    init {
        onDeviceIdReload()
        onFingerprintReload()
    }

    fun onDeviceIdVersionChanged(newVersion: Fingerprinter.Version) {
        updateDeviceId(newVersion)
    }

    fun onFingerprintVersionChanged(newVersion: Fingerprinter.Version) {
        updateFingerprint(newVersion, fingerprintScreenState.value.stabilityLevel)
    }

    fun onFingerprintStabilityLevelChanged(newStabilityLevel: StabilityLevel) {
        updateFingerprint(fingerprintScreenState.value.version, newStabilityLevel)
    }

    fun onDeviceIdReload() {
        updateDeviceId(deviceIdScreenState.value.version)
    }

    fun onFingerprintReload() {
        with(fingerprintScreenState.value) {
            updateFingerprint(version = version, stabilityLevel = stabilityLevel)
        }
    }

    private fun updateDeviceId(version: Fingerprinter.Version) {
        if (updateDeviceIdJob?.isActive == true)
            return

        updateDeviceIdJob = viewModelScope.launch {
            deviceIdScreenStateMutable.emit(
                deviceIdScreenStateMutable.value.copy(
                    version = version,
                )
            )

            val deviceIdResult = fingerprinter.getDeviceId(version)
            deviceIdScreenStateMutable.emit(
                DeviceIdScreenState(
                    version = version,
                    deviceIdInfo = createDeviceIdInfoReadyState(deviceIdResult)
                )
            )
        }
    }

    private fun updateFingerprint(version: Fingerprinter.Version, stabilityLevel: StabilityLevel) {
        if (updateFingerprintJob?.isActive == true)
            return

        updateFingerprintJob = viewModelScope.launch {
            fingerprintScreenStateMutable.emit(
                fingerprintScreenStateMutable.value.copy(
                    version = version,
                    stabilityLevel = stabilityLevel,
                )
            )

            val fingerprint = fingerprinter.getFingerprint(
                version = version,
                stabilityLevel = stabilityLevel
            )
            val signals = withContext(Dispatchers.IO) {
                fingerprinter.getFingerprintingSignalsProvider().getSignalsMatching(
                    version = version,
                    stabilityLevel = stabilityLevel
                )
            }
            fingerprintScreenStateMutable.emit(
                FingerprintScreenState(
                    version = version,
                    stabilityLevel = stabilityLevel,
                    fingerprintInfo = createFingerprintInfoReadyState(
                        fingerprint = fingerprint,
                        signals = signals,
                    )
                )
            )
        }
    }
}
