package com.novoda.materialised.hackernews.topstories.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StoryViewDataTest {

    @Test
    public void submittedFromStripsWWWPrefixFromDomainName() {
        String expectedDomainName = "google.com";
        String fullDomainName = "http://www." + expectedDomainName + "/";
        StoryViewData empty = new StoryViewData();
        StoryViewData data = new StoryViewData(
                empty.getBy(), empty.getCommentIds(), empty.getId(), empty.getScore(), empty.getTitle(), fullDomainName
        );

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName);
    }

    @Test
    public void submittedFromDoesNotStripPrefixFromDomainName_WhenItIsNotWWW() {
        String expectedDomainName = "news.ycombinator.com";
        String fullDomainName = "http://" + expectedDomainName + "/";
        StoryViewData empty = new StoryViewData();
        StoryViewData data = new StoryViewData(
                empty.getBy(), empty.getCommentIds(), empty.getId(), empty.getScore(), empty.getTitle(), fullDomainName
        );

        assertThat(data.submittedFrom()).isEqualTo(expectedDomainName);
    }

    @Test
    public void submittedFromReturnsEmptyString_WhenUrlIsEmptyString() {
        StoryViewData empty = new StoryViewData();
        assertThat(empty.submittedFrom()).isEqualTo("");
    }

    @Test
    public void commentCountIsZeroWhenThereAreNoCommentIds() {
        StoryViewData empty = new StoryViewData();
        assertThat(empty.commentCount()).isEqualTo(0);
    }

    @Test
    public void commentCountIsSameAsSizeOfListOfCommentIds() {
        List<Integer> commentIds = Arrays.asList(1, 2, 3, 4);
        StoryViewData empty = new StoryViewData();
        StoryViewData data = new StoryViewData(
                empty.getBy(), commentIds, empty.getId(), empty.getScore(), empty.getTitle(), empty.getUrl()
        );
        assertThat(data.commentCount()).isEqualTo(commentIds.size());
    }
}
