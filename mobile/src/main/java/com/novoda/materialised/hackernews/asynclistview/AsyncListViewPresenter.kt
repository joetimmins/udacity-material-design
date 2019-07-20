package com.novoda.materialised.hackernews.asynclistview

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novoda.materialised.R
import com.novoda.materialised.hackernews.stories.view.UiStory

internal class AsyncListViewPresenter(
    private val loadingView: TextView,
    topStoriesView: RecyclerView
) : AsyncListView<UiStory> {

    private val adapter: StoryCardAdapter = StoryCardAdapter()

    init {
        loadingView.text = loadingView.context.resources.getString(R.string.loading_stories)
        loadingView.visibility = View.VISIBLE
        topStoriesView.adapter = adapter
    }

    override fun updateWith(uiModel: UiModel<UiStory>) {
        loadingView.visibility = View.GONE
        adapter.updateWith(uiModel)
    }

    override fun showError(throwable: Throwable) {
        Log.e("HN", "something borkded", throwable)
        loadingView.setText(R.string.bork_bork)
        loadingView.visibility = View.VISIBLE
    }
}
