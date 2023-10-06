package com.fingerprintjs.android.fingerprint.info_providers


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.Safe
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface PackageManagerDataSource {
    public fun getApplicationsList(): List<PackageInfo>
    public fun getSystemApplicationsList(): List<PackageInfo>
}

public data class PackageInfo(
    val packageName: String
) {
    override fun toString(): String {
        return packageName
    }
}

internal class PackageManagerDataSourceImpl(
    private val packageManager: PackageManager?,
) : PackageManagerDataSource {
    @SuppressLint("QueryPermissionsNeeded")
    override fun getApplicationsList() = safeWithTimeout(timeoutMs = Safe.timeoutLong) {
        packageManager!!
            .getInstalledApplications(PackageManager.GET_META_DATA)
            .map {
                PackageInfo(
                    it!!.packageName!!
                )
            }
    }.getOrDefault(emptyList())


    @SuppressLint("QueryPermissionsNeeded")
    override fun getSystemApplicationsList() = safeWithTimeout(timeoutMs = Safe.timeoutLong) {
        packageManager!!
            .getInstalledApplications(PackageManager.GET_META_DATA)
            .filter {
                it!!.sourceDir!!.contains("/system/")
            }
            .map {
                PackageInfo(
                    it!!.packageName!!
                )
            }
    }.getOrDefault(emptyList())
}
