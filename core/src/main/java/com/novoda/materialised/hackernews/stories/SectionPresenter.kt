package com.novoda.materialised.hackernews.stories

import com.novoda.materialised.hackernews.stories.database.StoryType

class SectionPresenter(
        private val contentPresenter: TypedPresenter<StoryType>
) {
    var storyType: String = "topstories"

    val storyTypeMap = hashMapOf(
            "Top Stories" to "topstories",
            "Best" to "beststories",
            "New" to "newstories"
    )

    fun resume() {
        refreshContent()
    }

    fun tabSelected(tabName: CharSequence?) {
        val selectedTabName = tabName?.toString() ?: ""
        val selectedStoryType = storyTypeMap.getOrElse(selectedTabName, { "topstories" })
        storyType = selectedStoryType
        refreshContent()
    }

    private fun refreshContent() {
        contentPresenter.present(StoryType.valueOf(storyType))
    }
}

