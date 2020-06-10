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
}