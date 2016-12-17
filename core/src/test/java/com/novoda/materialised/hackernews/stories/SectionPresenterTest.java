package com.novoda.materialised.hackernews.stories;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
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
        String actual = storiesPresenter.presentedTypes.get(0);
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
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("topstories");
    }

    @Test
    public void tabPresenterMapsNewTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("New");

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("newstories");
    }

    @Test
    public void tabPresenterMapsBestTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected("Best");

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("beststories");
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
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo("topstories");
        assertThat(storiesPresenter.presentedTypes.get(1)).isEqualTo("beststories");
        assertThat(storiesPresenter.presentedTypes.get(2)).isEqualTo("beststories");
    }

    @Test
    public void tabPresenterShowsTopStories_WhenTabNameIsNull() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        SectionPresenter sectionPresenter = new SectionPresenter(storiesPresenter);

        // Act
        sectionPresenter.tabSelected(null);

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo("topstories");
    }

    private class CapturingTypedPresenter implements TypedPresenter<String> {
        List<String> presentedTypes = new ArrayList<>();

        @Override
        public void present(@NotNull String type) {
            presentedTypes.add(type);
        }
    }
}
