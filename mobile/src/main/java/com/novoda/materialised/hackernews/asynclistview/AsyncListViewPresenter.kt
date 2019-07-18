package com.novoda.materialised.hackernews.asynclistview

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novoda.materialised.R

internal class AsyncListViewPresenter<T, V>(
    private val loadingView: TextView,
    topStoriesView: RecyclerView,
    viewClass: Class<V>,
    viewInflater: ModelledViewInflater<V> = ModelledViewInflater(viewClass)
) : AsyncListView<T> where T : UiData<Int>, V : View, V : ModelledView<T> {

    private val adapter: SingleTypeAdapter<T, V> = SingleTypeAdapter(viewInflater)

    init {
        loadingView.text = loadingView.context.resources.getString(R.string.loading_stories)
        loadingView.visibility = View.VISIBLE
        topStoriesView.adapter = adapter
    }

    override fun updateWith(uiState: UiState<T>) {
        loadingView.visibility = View.GONE
        adapter.updateWith(uiState)
    }

    override fun showError(throwable: Throwable) {
        Log.e("HN", "something borkded", throwable)
        loadingView.setText(R.string.bork_bork)
        loadingView.visibility = View.VISIBLE
    }
}
