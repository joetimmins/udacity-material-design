package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.StoryViewData

class StoryClickListener(private val navigator: Navigator) : ClickListener<StoryViewData> {
    override fun onClick(data: StoryViewData) {
        navigator.navigateTo(data.url)
    }
}
