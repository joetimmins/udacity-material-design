package com.novoda.materialised.hackernews.section

import com.novoda.materialised.hackernews.Presenter

@Deprecated("Let's use the SectionListPresenter instead!")
class SectionPresenter(
        private val contentPresenter: Presenter<Section>
) {
    var section: Section = Section.TOP_STORIES

    fun resume() {
        refreshContent()
    }

    fun tabSelected(tabName: CharSequence?) {
        val selectedTabName = tabName?.toString() ?: ""
        section = Section.values()
                .filter { storyType -> storyType.userFacingName == selectedTabName }
                .elementAtOrElse(index = 0, defaultValue = { Section.TOP_STORIES })
        refreshContent()
    }

    private fun refreshContent() {
        contentPresenter.present(section)
    }
}

