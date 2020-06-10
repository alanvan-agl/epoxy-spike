package com.alan.epoxyspike.main

import com.alan.epoxyspike.base.LifecyclePresenter

object MainContract {

    interface Presenter : LifecyclePresenter<View>

    interface View {
        fun displayData(dataList: List<Int>)
    }

}