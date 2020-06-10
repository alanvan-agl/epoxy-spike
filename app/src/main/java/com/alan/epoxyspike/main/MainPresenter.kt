package com.alan.epoxyspike.main

class MainPresenter : MainContract.Presenter {

    private var view: MainContract.View? = null

    override fun onAttach(view: MainContract.View) {
        this.view = view
        view.displayData(
            1.rangeTo(100).toList()
        )
    }

    override fun onDetach() {
        this.view = null
    }

    override fun onItemClicked(itemValue: String) {
        view?.showItemClicked(itemValue)
    }
}