package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.playground.R


class FingerprintItemAdapter(
    private val dataset: List<FingerprinterItem>
) : RecyclerView.Adapter<FingerprintItemAdapter.FingerprintViewHolder>() {

    class FingerprintViewHolder(val fingerprinterItemView: FingerprinterItemViewImpl) :
        RecyclerView.ViewHolder(fingerprinterItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FingerprintViewHolder {
        val fingerprinterView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fingerprinter_card_view, parent, false) as FingerprinterItemViewImpl

        return FingerprintViewHolder(fingerprinterItemView = fingerprinterView)
    }

    override fun onBindViewHolder(holder: FingerprintViewHolder, position: Int) {
        holder.fingerprinterItemView.setTitle(dataset[position].title)
        holder.fingerprinterItemView.setDescription(dataset[position].description)
        holder.fingerprinterItemView.setFingerprintValue(dataset[position].fingerprintValue)
    }

    override fun getItemCount() = dataset.size

}