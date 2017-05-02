package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.stories.database.StoryType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SectionPresenterTest {

    @Test
    public void tabPresenterShowsTopStories_ByDefault() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
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
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Top Stories");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(StoryType.TOP_STORIES);
    }

    @Test
    public void tabPresenterMapsNewTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("New");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(StoryType.NEW);
    }

    @Test
    public void tabPresenterMapsBestTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Best");

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(StoryType.BEST);
    }

    @Test
    public void tabPresenterRefreshesContentPresenter_WhenResuming() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Top Stories");
        sectionPresenter.tabSelected("Best");
        sectionPresenter.resume();

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(StoryType.TOP_STORIES);
        assertThat(storiesPresenter.presentedTypes.get(1)).isEqualTo(StoryType.BEST);
        assertThat(storiesPresenter.presentedTypes.get(2)).isEqualTo(StoryType.BEST);
    }

    @Test
    public void tabPresenterShowsTopStories_WhenTabNameIsNull() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected(null);

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo(StoryType.TOP_STORIES);
    }

    private class CapturingTypedPresenter implements TypedPresenter<StoryType> {
        List<StoryType> presentedTypes = new ArrayList<>();

        @Override
        public void present(StoryType type) {
            presentedTypes.add(type);
        }
    }
}
