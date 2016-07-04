package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.navigator.Navigator
import com.novoda.materialised.hackernews.topstories.view.StoryViewData

class StoryClickListener(private val navigator: Navigator) : ClickListener<com.novoda.materialised.hackernews.topstories.view.StoryViewData> {
    override fun onClick(data: com.novoda.materialised.hackernews.topstories.view.StoryViewData) {
        navigator.navigateTo(data.url)
    }
}
