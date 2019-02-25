package com.novoda.materialised.hackernews.asynclistview

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView

import com.novoda.materialised.R

class AsyncListViewPresenter<T, V> private constructor(
    private val loadingView: TextView,
    private val topStoriesView: RecyclerView,
    private val viewInflater: ModelledViewInflater<V>
) : AsyncListView<T> where T : ViewData<Int>, V : View, V : ModelledView<T> {

    private var adapter: SingleTypeAdapter<T, V>? = null

    constructor(
        loadingView: TextView,
        topStoriesView: RecyclerView,
        viewClass: Class<V>
    ) : this(loadingView, topStoriesView, ModelledViewInflater<V>(viewClass))

    init {
        init()
    }

    private fun init() {
        val textView = loadingView
        textView.text = textView.context.resources.getString(R.string.loading_stories)
        loadingView.visibility = View.VISIBLE
        adapter = SingleTypeAdapter(viewInflater)
        topStoriesView.adapter = adapter
    }

    override fun updateWith(viewModel: ViewModel<T>) {
        loadingView.visibility = View.GONE
        adapter!!.updateWith(viewModel)
    }

    override fun showError(throwable: Throwable) {
        Log.e("HN", "something borkded", throwable)
        loadingView.setText(R.string.bork_bork)
        loadingView.visibility = View.VISIBLE
    }
}
