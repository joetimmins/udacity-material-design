package com.novoda.materialised.hackernews.topstories.view

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.generics.ViewModel

data class StoryViewModel(
        override val viewData: StoryViewData,
        override val viewBehaviour: ClickListener<StoryViewData>
) : ViewModel<StoryViewData> {}
