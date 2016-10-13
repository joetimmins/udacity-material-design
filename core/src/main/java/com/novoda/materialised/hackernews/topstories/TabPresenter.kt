package com.novoda.materialised.hackernews.topstories

class TabPresenter(
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

    fun tabSelected(tabName: String) {
        val selectedStoryType = storyTypeMap.getOrElse(tabName, {"topstories"})
        storyType = selectedStoryType
        refreshContent()
    }

    private fun refreshContent() {
        contentPresenter.present(storyType)
    }
}

