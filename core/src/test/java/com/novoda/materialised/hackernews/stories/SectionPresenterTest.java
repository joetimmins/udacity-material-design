package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.section.Section;
import com.novoda.materialised.hackernews.section.SectionPresenter;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SectionPresenterTest {

    @Test
    public void tabPresenterShowsTopStories_ByDefault() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.resume();

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0).getId();
        assertThat(actual).isEqualTo("topstories");
    }

    @Test
    public void tabPresenterMapsTopStoriesTypeCorrectly() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Top Stories");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(Section.TOP_STORIES);
    }

    @Test
    public void tabPresenterMapsNewTypeCorrectly() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("New");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(Section.NEW);
    }

    @Test
    public void tabPresenterMapsBestTypeCorrectly() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Best");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(Section.BEST);
    }

    @Test
    public void tabPresenterRefreshesContentPresenter_WhenResuming() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Top Stories");
        sectionPresenter.tabSelected("Best");
        sectionPresenter.resume();

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(Section.TOP_STORIES);
        assertThat(storiesPresenter.presentedTypes.get(1)).isEqualTo(Section.BEST);
        assertThat(storiesPresenter.presentedTypes.get(2)).isEqualTo(Section.BEST);
    }

    @Test
    public void tabPresenterShowsTopStories_WhenTabNameIsNull() {
        // Arrange
        SpyingSectionPresenter storiesPresenter = new SpyingSectionPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected(null);

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(Section.TOP_STORIES);
    }

}
