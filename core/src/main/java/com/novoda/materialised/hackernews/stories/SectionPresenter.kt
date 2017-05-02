package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.stories.database.StoryType

class SectionPresenter(
        private val contentPresenter: TypedPresenter<StoryType>
) {
    var storyType: StoryType = StoryType.TOP_STORIES

    fun resume() {
        refreshContent()
    }

    fun tabSelected(tabName: CharSequence?) {
        val selectedTabName = tabName?.toString() ?: ""
        storyType = StoryType.values()
                .filter { storyType -> storyType.userFacingName == selectedTabName }
                .elementAtOrElse(index = 0, defaultValue = { StoryType.TOP_STORIES })
        refreshContent()
    }

    private fun refreshContent() {
        contentPresenter.present(storyType)
    }
}

