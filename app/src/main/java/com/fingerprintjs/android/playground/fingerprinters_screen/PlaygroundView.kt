package com.fingerprintjs.android.playground.fingerprinters_screen


import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemAdapter
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import java.util.*


interface PlaygroundView {
    fun setFingerprintItems(items: List<FingerprinterItem>)
}

class PlaygroundViewImpl(
    activity: Activity
) : PlaygroundView {
    private val container: RecyclerView = activity.findViewById(R.id.fingerprinters_container)
    private val viewManager = LinearLayoutManager(activity)
    private val dataset = LinkedList<FingerprinterItem>()
    private val adapter = FingerprintItemAdapter(dataset)

    init {
        container.layoutManager = viewManager
        container.adapter = adapter
    }

    override fun setFingerprintItems(items: List<FingerprinterItem>) {
        dataset.clear()
        items.forEach {
            dataset.add(it)
        }
        adapter.notifyDataSetChanged()
    }
}