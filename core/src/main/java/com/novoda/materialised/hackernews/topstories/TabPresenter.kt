package com.novoda.materialised.hackernews.topstories

class TabPresenter(
        private val contentPresenter: TypedPresenter<String>
) {
    fun resume() {
        contentPresenter.present("topstories")
    }

    fun tabSelected(tabName: String) {
        val storyTypeMap = hashMapOf(
                "Top Stories" to "topstories",
                "Best" to "beststories",
                "New" to "newstories"
        )
        val storyType = storyTypeMap.getOrDefault(tabName, "topstories")
        contentPresenter.present(storyType)
    }
}

