package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.StoryViewModel

class StoryViewModelClickListener(private val navigator: Navigator) : ClickListener<StoryViewModel> {
    override fun onClick(data: StoryViewModel) {
        navigator.navigateTo(data.url)
    }
}
