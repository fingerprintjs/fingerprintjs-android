package com.fingerprintjs.android.playground.fingerprinters_screen.tools

import android.view.View


fun View.dpToPx(dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}