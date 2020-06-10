package com.alan.epoxyspike.main.epoxy_models

import android.widget.TextView
import com.alan.epoxyspike.R
import com.alan.epoxyspike.base.KotlinModel

class EpoxyItemType2(
    index: Int,
    val data: Int,
    val onClickCallback: (String) -> Unit
) : KotlinModel(R.layout.epoxy_item) {

    init {
        id("${javaClass.simpleName} - $index")
    }

    private val textView: TextView by bind(R.id.textView)

    override fun bind() {
        val value = context!!.getString(R.string.type2, data + 1)
        textView.text = value
        textView.setOnClickListener {
            onClickCallback(value)
        }
    }
}