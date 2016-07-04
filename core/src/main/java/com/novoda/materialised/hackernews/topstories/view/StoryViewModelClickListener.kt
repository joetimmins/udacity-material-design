package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.ClickListener
import com.novoda.materialised.hackernews.items.StoryViewModel
import com.novoda.materialised.hackernews.navigator.Navigator

class StoryViewModelClickListener(private val navigator: Navigator) : ClickListener<StoryViewModel> {
    override fun onClick(data: StoryViewModel) {
        navigator.navigateTo(data.url)
    }
}

fun something(navigator: Navigator) : ClickListener<StoryViewModel> {
    return object : ClickListener<StoryViewModel> {
        override fun onClick(data: StoryViewModel) {
           navigator.navigateTo(data.url)
        }
    }
}
