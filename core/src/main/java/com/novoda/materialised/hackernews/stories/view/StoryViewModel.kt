package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.ClickListener
import com.novoda.materialised.hackernews.asynclistview.ViewModel

data class StoryViewModel(
        private val viewData: StoryViewData,
        private val viewBehaviour: ClickListener<StoryViewData>
) : ViewModel<StoryViewData> {
    override fun viewData(): StoryViewData {
        return viewData
    }

    override fun viewBehaviour(): ClickListener<StoryViewData> {
        return viewBehaviour
    }
}
