package com.alan.epoxyspike.main

import com.alan.epoxyspike.base.LifecyclePresenter

object MainContract {

    interface Presenter : LifecyclePresenter<View>, MainAdapter.ActionListener {
        override fun onItemClicked(itemValue: String)
    }

    interface View {
        fun displayData(dataList: List<Int>)
        fun showItemClicked(itemValue: String)
    }

}