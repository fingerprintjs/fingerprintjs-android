package com.fingerprintjs.android.playground.fingerprinters_screen.adapter.description


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.playground.R


class DescriptionItemAdapter(
    private val dataset: List<DescriptionItem>
) : RecyclerView.Adapter<DescriptionItemAdapter.DescriptionItemViewHolder>() {

    class DescriptionItemViewHolder(val descriptionItemView: DescriptionItemViewImpl) :
        RecyclerView.ViewHolder(descriptionItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionItemViewHolder {
        val fingerprinterView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.description_item, parent, false) as DescriptionItemViewImpl

        return DescriptionItemViewHolder(
            descriptionItemView = fingerprinterView
        )
    }

    override fun onBindViewHolder(holder: DescriptionItemViewHolder, position: Int) {
        val item = dataset[position]
        if (item.isHeading) {
            holder.descriptionItemView.setHeading(item.name)
        } else {
            holder.descriptionItemView.setName(item.name)
            holder.descriptionItemView.setValue(item.value)
        }
    }

    override fun getItemCount() = dataset.size

}