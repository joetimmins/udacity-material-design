package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.AdapterViewModel
import com.novoda.materialised.hackernews.asynclistview.ClickListener

data class StoryViewModel(
        override val viewData: StoryViewData,
        override val viewBehaviour: ClickListener<StoryViewData>
) : AdapterViewModel<StoryViewData>
