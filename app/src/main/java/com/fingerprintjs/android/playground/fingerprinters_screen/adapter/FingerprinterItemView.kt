package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.fingerprintjs.android.playground.R


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

    private val titleView: TextView by lazy {
        findViewById<TextView>(R.id.fingerprinter_title)
    }
    private val fingerprintValueView: TextView by lazy {
        findViewById<TextView>(R.id.fingerprinter_value)
    }
    private val descriptionView: TextView by lazy {
        findViewById<TextView>(R.id.fingerprinter_description)
    }

    private var isExpanded: Boolean = false

    override fun setTitle(title: String) {
        titleView.text = title
    }

    override fun setFingerprintValue(value: String) {
        fingerprintValueView.text = value
    }

    override fun setDescription(description: List<FingerprintSectionDescription>) {
        descriptionView.text = prepareDescriptionString(description)
    }

    private fun toggleExpanding() {
        descriptionView.visibility = if (isExpanded) View.GONE else View.VISIBLE
        isExpanded = !isExpanded
    }

    private fun prepareDescriptionString(description: List<FingerprintSectionDescription>): String {
        val sb = StringBuilder()
        description.forEach {
            sb.append("\n\n${it.name.toUpperCase()}\n")
            it.fields.forEach { sb.append("${it.first}  |  ${it.second}\n") }
        }
        return sb.toString()
    }
}