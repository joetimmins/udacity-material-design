package com.novoda.materialised.hackernews.stories.view

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.Arrays

class FullyPopulatedTest {

    @Test
    fun submittedFromStripsWWWPrefixFromDomainName() {
        val expectedDomainName = "google.com"
        val fullDomainName = "http://www.$expectedDomainName/"
        val data = StoryUiData.FullyPopulated(url = fullDomainName)

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName)
    }

    @Test
    fun submittedFromDoesNotStripPrefixFromDomainName_WhenItIsNotWWW() {
        val expectedDomainName = "news.ycombinator.com"
        val fullDomainName = "http://$expectedDomainName/"
        val data = StoryUiData.FullyPopulated(url = fullDomainName)

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName)
    }

    @Test
    fun submittedFromReturnsEmptyString_WhenUrlIsEmptyString() {
        val empty = StoryUiData.FullyPopulated()
        assertThat(empty.submittedFrom()).isEqualTo("")
    }

    @Test
    fun commentCountIsZeroWhenThereAreNoCommentIds() {
        val empty = StoryUiData.FullyPopulated()
        assertThat(empty.commentCount()).isEqualTo(0.toString())
    }

    @Test
    fun commentCountIsSameAsSizeOfListOfCommentIds() {
        val givenCommentIds = Arrays.asList(1, 2, 3, 4)
        val data = StoryUiData.FullyPopulated(commentIds = givenCommentIds)
        assertThat(data.commentCount()).isEqualTo(givenCommentIds.size.toString())
    }
}
