package com.fingerprintjs.android.playground.fingerprinters_screen.adapter.description


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.fingerprintjs.android.playground.R


interface DescriptionItemView {
    fun setName(name: String)
    fun setValue(value: String)
    fun setHeading(heading: String)
}

class DescriptionItemViewImpl : FrameLayout,
    DescriptionItemView {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : super(context, attrs, defStyle)

    private val heading: TextView by lazy {
        findViewById<TextView>(R.id.description_item_heading)
    }

    private val itemView: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.item_view)
    }

    private val name: TextView by lazy {
        findViewById<TextView>(R.id.description_item_name)
    }

    private val value: TextView by lazy {
        findViewById<TextView>(R.id.description_item_value)
    }

    override fun setName(name: String) {
        heading.visibility = View.GONE
        itemView.visibility = View.VISIBLE
        this.name.visibility = View.VISIBLE
        this.name.text = name
    }

    override fun setValue(value: String) {
        this.value.visibility = View.VISIBLE
        this.value.text = value
    }

    override fun setHeading(heading: String) {
        itemView.visibility = View.GONE
        this.heading.visibility = View.VISIBLE
        this.heading.text = heading
    }
}