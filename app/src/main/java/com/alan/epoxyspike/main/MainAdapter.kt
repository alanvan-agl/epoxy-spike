package com.alan.epoxyspike.main

import com.airbnb.epoxy.TypedEpoxyController
import com.alan.epoxyspike.main.epoxy_models.EpoxyItem

class MainAdapter(private val actionListener: ActionListener) : TypedEpoxyController<List<String>>() {

    override fun buildModels(data: List<String>?) {
        data?.forEachIndexed { index, s ->
            EpoxyItem(
                index = index,
                data = s
            ) {
                actionListener.onItemClicked(index)
            }.addTo(this)
        }
    }

    interface ActionListener {
        fun onItemClicked(index: Int)
    }
}