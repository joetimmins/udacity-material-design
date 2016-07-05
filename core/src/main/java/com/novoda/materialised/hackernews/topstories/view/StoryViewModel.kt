package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.asynclistview.ClickListener
import com.novoda.materialised.hackernews.asynclistview.ViewModel

data class StoryViewModel(
        override val viewData: StoryViewData,
        override val viewBehaviour: ClickListener<StoryViewData>
) : ViewModel<StoryViewData> {}
