package com.novoda.materialised.hackernews.stories

class SectionPresenter(
        private val contentPresenter: TypedPresenter<String>
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
        contentPresenter.present(storyType)
    }
}

