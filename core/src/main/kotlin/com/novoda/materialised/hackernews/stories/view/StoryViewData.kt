package com.novoda.materialised.hackernews.stories.view

import com.novoda.materialised.hackernews.asynclistview.ViewData
import java.net.URI


sealed class StoryViewData : ViewData<Int> {
    data class JustAnId(override val id: Int) : StoryViewData()
    data class FullyPopulated(val by: String = "",
                              val commentIds: List<Int> = emptyList(),
                              override val id: Int = 0,
                              val score: Int = 0,
                              val title: String = "",
                              val url: String = "") : StoryViewData() {
        fun commentCount(): String = commentIds.count().toString()
        fun submittedFrom(): String {
            val domainName = URI.create(url).host ?: ""
            val prefix = "www."
            return when {
                domainName.startsWith(prefix) -> domainName.substring(prefix.length)
                else -> domainName
            }
        }
    }
}
