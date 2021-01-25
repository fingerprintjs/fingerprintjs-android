package com.fingerprintjs.android.playground.fingerprinters_screen


import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemAdapter
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import java.util.LinkedList


interface PlaygroundView {
    fun setFingerprintItems(items: List<FingerprinterItem>)
    fun setFingerprint(fingerprint: String, version: Int, stabilityLevel: StabilityLevel)

    fun setOnStabilityChangedListener(listener: ((StabilityLevel) -> (Unit))?)
    fun setOnVersionChangedListener(listener: ((Int) -> Unit)?)
}

class PlaygroundViewImpl(
    private val activity: Activity
) : PlaygroundView, AdapterView.OnItemSelectedListener {
    private val container: RecyclerView = activity.findViewById(R.id.fingerprinters_container)
    private val viewManager = LinearLayoutManager(activity)
    private val dataset = LinkedList<FingerprinterItem>()
    private val adapter = FingerprintItemAdapter(dataset)

    private val fingerprintValueTextView: TextView =
        activity.findViewById(R.id.custom_fingerprinе_value)
    private val fingerprintDescriptionTextView: TextView =
        activity.findViewById(R.id.custom_fingerprint_heading)

    private val versionSelectorSpinner: Spinner = activity.findViewById(R.id.version_spinner)

    private val stableToggleButton: Button = activity.findViewById(R.id.stable_toggle_button)
    private val optimalToggleButton: Button = activity.findViewById(R.id.optimal_toggle_button)
    private val uniqueToggleButton: Button = activity.findViewById(R.id.unique_toggle_button)

    private var onStabilityChangedListener: (((StabilityLevel) -> (Unit)))? = null
    private var onVersionChangedListener: ((Int) -> (Unit))? = null
    
    init {
        container.layoutManager = viewManager
        container.adapter = adapter

        versionSelectorSpinner.onItemSelectedListener = this
        stableToggleButton.setOnClickListener {
            onStabilityChangedListener?.invoke(StabilityLevel.STABLE)
        }

        optimalToggleButton.setOnClickListener {
            onStabilityChangedListener?.invoke(StabilityLevel.OPTIMAL)
        }

        uniqueToggleButton.setOnClickListener {
            onStabilityChangedListener?.invoke(StabilityLevel.UNIQUE)
        }
    }

    override fun setFingerprint(fingerprint: String, version: Int, stabilityLevel: StabilityLevel) {
        activity.runOnUiThread {
            fingerprintValueTextView.text = fingerprint
            versionSelectorSpinner.setSelection(version - 1)
            when (stabilityLevel) {
                StabilityLevel.STABLE -> {
                    stableToggleButton.isSelected = true
                }
                StabilityLevel.UNIQUE -> {
                    uniqueToggleButton.isSelected = true
                }
                StabilityLevel.OPTIMAL -> {
                    optimalToggleButton.isSelected = true
                }
            }
            fingerprintDescriptionTextView.text = activity.getString(
                R.string.custom_fingerprint_heading,
                stabilityLevel.stringDescription(),
                version.toString()
            )
        }
    }


    override fun setOnStabilityChangedListener(listener: ((StabilityLevel) -> Unit)?) {
        onStabilityChangedListener = listener
    }

    override fun setOnVersionChangedListener(listener: ((Int) -> Unit)?) {
        onVersionChangedListener = listener
    }


    override fun setFingerprintItems(items: List<FingerprinterItem>) {
        activity.runOnUiThread {
            dataset.clear()
            items.forEach {
                dataset.add(it)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun StabilityLevel.stringDescription() = when (this) {
        StabilityLevel.OPTIMAL -> "Optimal"
        StabilityLevel.STABLE -> "Stable"
        StabilityLevel.UNIQUE -> "Unique"
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        onVersionChangedListener?.invoke(p2 + 1)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}