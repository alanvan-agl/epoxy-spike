package com.alan.epoxyspike.main.epoxy_models

import android.widget.TextView
import com.alan.epoxyspike.R
import com.alan.epoxyspike.base.KotlinModel

class EpoxyItem(
    index: Int,
    val data: String,
    val onClickCallback: () -> Unit
) : KotlinModel(R.layout.epoxy_item) {

    init {
        id("${javaClass.simpleName} - $index")
    }

    private val textView: TextView by bind(R.id.textView)

    override fun bind() {
        textView.text = data
        textView.setOnClickListener {
            onClickCallback()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EpoxyItem) return false
        if (!super.equals(other)) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}