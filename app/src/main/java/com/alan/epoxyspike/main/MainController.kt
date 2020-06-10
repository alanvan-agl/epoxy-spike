package com.alan.epoxyspike.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.alan.epoxyspike.R
import com.bluelinelabs.conductor.Controller

class MainController : Controller(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var adapter: MainAdapter

    private var recyclerView: EpoxyRecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main, container, false)
        presenter = MainPresenter()
        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        adapter = MainAdapter(presenter)
        recyclerView?.layoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.setController(adapter)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.onAttach(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.onDetach()
    }

    override fun displayData(dataList: List<Int>) {
        adapter.setData(dataList)
    }

    override fun showItemClicked(itemValue: String) {
        Toast.makeText(this.applicationContext, itemValue, Toast.LENGTH_SHORT).show()
    }
}