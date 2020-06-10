package com.alan.epoxyspike.main

import com.airbnb.epoxy.TypedEpoxyController
import com.alan.epoxyspike.main.epoxy_models.EpoxyItemType1
import com.alan.epoxyspike.main.epoxy_models.EpoxyItemType2

class MainAdapter(private val actionListener: ActionListener) : TypedEpoxyController<List<Int>>() {

    override fun buildModels(data: List<Int>?) {
        data?.forEachIndexed { index, s ->
            if (index % 2 == 0) {
                EpoxyItemType1(
                    index = index,
                    data = s
                ) { itemValue ->
                    actionListener.onItemClicked(itemValue)
                }.addTo(this)
            } else {
                EpoxyItemType2(
                    index = index,
                    data = s
                ) { itemValue ->
                    actionListener.onItemClicked(itemValue)
                }.addTo(this)
            }
        }
    }

    interface ActionListener {
        fun onItemClicked(itemValue: String)
    }
}