package com.alan.epoxyspike.base

interface LifecyclePresenter<V> {
    fun onAttach(view: V) {}
    fun onDetach() {}
}