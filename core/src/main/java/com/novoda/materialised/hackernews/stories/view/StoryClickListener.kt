package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.ClickListener
import com.novoda.materialised.hackernews.navigator.Navigator

class StoryClickListener(private val navigator: Navigator) : ClickListener<StoryViewData> {
    override fun onClick(data: StoryViewData) {
        navigator.navigateTo(data.url)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as StoryClickListener

        if (navigator != other.navigator) return false

        return true
    }

    override fun hashCode(): Int {
        return navigator.hashCode()
    }

}
