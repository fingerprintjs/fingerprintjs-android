package com.fingerprintjs.android.playground.fingerprinters_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.fingerprintjs.android.playground.R

class AboutDialog() {
    fun show(activity: Activity) {
        AlertDialog
            .Builder(activity)
            .setTitle(R.string.about_dialog_title)
            .setPositiveButton(R.string.about_ok_button_text) { dialogInterface: DialogInterface, i: Int -> }
            .setView(activity.layoutInflater.inflate(R.layout.about_dialog, null)).create().show()
    }
}