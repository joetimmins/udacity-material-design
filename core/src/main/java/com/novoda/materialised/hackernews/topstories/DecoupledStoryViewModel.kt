package com.novoda.materialised.hackernews.topstories

import com.novoda.materialised.hackernews.generics.ClickListener
import com.novoda.materialised.hackernews.generics.DecoupledViewModel

data class DecoupledStoryViewModel(
        override val viewData: StoryViewData,
        override val viewBehaviour: ClickListener<StoryViewData>
) : DecoupledViewModel<StoryViewData> {}
