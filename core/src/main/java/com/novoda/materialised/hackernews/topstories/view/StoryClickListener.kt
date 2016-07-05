package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.asynclistview.ClickListener
import com.novoda.materialised.hackernews.navigator.Navigator

class StoryClickListener(private val navigator: Navigator) : ClickListener<StoryViewData> {
    override fun onClick(data: StoryViewData) {
        navigator.navigateTo(data.url)
    }
}
