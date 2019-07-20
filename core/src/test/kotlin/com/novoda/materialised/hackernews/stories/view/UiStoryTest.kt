package com.novoda.materialised.hackernews.stories.view

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class UiStoryTest {

    @Test
    fun submittedFromStripsWWWPrefixFromDomainName() {
        val expectedDomainName = "google.com"
        val fullDomainName = "http://www.$expectedDomainName/"
        val data = UiStory(url = fullDomainName)

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName)
    }

    @Test
    fun submittedFromDoesNotStripPrefixFromDomainName_WhenItIsNotWWW() {
        val expectedDomainName = "news.ycombinator.com"
        val fullDomainName = "http://$expectedDomainName/"
        val data = UiStory(url = fullDomainName)

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName)
    }

    @Test
    fun submittedFromReturnsEmptyString_WhenUrlIsEmptyString() {
        val empty = UiStory()
        assertThat(empty.submittedFrom()).isEqualTo("")
    }

    @Test
    fun commentCountIsZeroWhenThereAreNoCommentIds() {
        val empty = UiStory()
        assertThat(empty.commentCount()).isEqualTo(0.toString())
    }

    @Test
    fun commentCountIsSameAsSizeOfListOfCommentIds() {
        val givenCommentIds = listOf(1, 2, 3, 4)
        val data = UiStory(commentIds = givenCommentIds)
        assertThat(data.commentCount()).isEqualTo(givenCommentIds.size.toString())
    }
}
