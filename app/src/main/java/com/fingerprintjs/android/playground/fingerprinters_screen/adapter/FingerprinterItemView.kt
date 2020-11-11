package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.description.DescriptionItem
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.description.DescriptionItemAdapter
import java.util.*


interface FingerprinterItemView {
    fun setTitle(title: String)
    fun setFingerprintValue(value: String)
    fun setDescription(description: List<FingerprintSectionDescription>)
}

class FingerprinterItemViewImpl : CardView, FingerprinterItemView {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : super(context, attrs, defStyle) {
        setOnClickListener { toggleExpanding() }
    }

    private val container: RecyclerView by lazy {
        val container = findViewById<RecyclerView>(R.id.fingerprinter_description)
        container.layoutManager = viewManager
        container.adapter = adapter
        container
    }
    private val viewManager = LinearLayoutManager(context)
    private val dataset = LinkedList<DescriptionItem>()
    private val adapter =
        DescriptionItemAdapter(
            dataset
        )

    private val titleView: TextView by lazy {
        findViewById<TextView>(R.id.fingerprinter_title)
    }
    private val fingerprintValueView: TextView by lazy {
        findViewById<TextView>(R.id.fingerprinter_value)
    }

    private val expandMoreIcon: ImageView by lazy {
        findViewById<ImageView>(R.id.expand_more_icon)
    }

    private val expandLessIcon: ImageView by lazy {
        findViewById<ImageView>(R.id.expand_less_icon)
    }


    private var isExpanded: Boolean = false

    override fun setTitle(title: String) {
        titleView.text = title
    }

    override fun setFingerprintValue(value: String) {
        fingerprintValueView.text = value
    }

    override fun setDescription(description: List<FingerprintSectionDescription>) {
        dataset.clear()
        description.forEach {
            dataset.add(
                DescriptionItem(
                    it.name,
                    "",
                    true
                )
            )
            it.fields.forEach { field ->
                dataset.add(
                    DescriptionItem(
                        field.first,
                        field.second,
                        false
                    )
                )
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun toggleExpanding() {
        container.visibility = if (isExpanded) View.GONE else View.VISIBLE
        if (!isExpanded) {
            expandLessIcon.visibility = View.VISIBLE
            expandMoreIcon.visibility = View.GONE
        } else {
            expandLessIcon.visibility = View.GONE
            expandMoreIcon.visibility = View.VISIBLE
        }
        isExpanded = !isExpanded
    }

}