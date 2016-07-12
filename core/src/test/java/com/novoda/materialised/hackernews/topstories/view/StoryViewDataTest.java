package com.novoda.materialised.hackernews.topstories.view;

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
}
