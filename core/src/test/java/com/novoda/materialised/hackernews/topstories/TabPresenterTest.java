package com.novoda.materialised.hackernews.topstories;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TabPresenterTest {

    @Test
    public void tabPresenterShowsTopStories_ByDefault() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.resume();

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("topstories");
    }

    @Test
    public void tabPresenterMapsTopStoriesTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("Top Stories");

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("topstories");
    }

    @Test
    public void tabPresenterMapsNewTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("New");

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("newstories");
    }

    @Test
    public void tabPresenterMapsBestTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("Best");

        // Assert
        String actual = storiesPresenter.presentedTypes.get(0);
        assertThat(actual).isEqualTo("beststories");
    }

    @Test
    public void tabPresenterRefreshesContentPresenter_WhenResuming() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("Top Stories");
        tabPresenter.tabSelected("Best");
        tabPresenter.resume();

        // Assert
        assertThat(storiesPresenter.presentedTypes.get(0)).isEqualTo("topstories");
        assertThat(storiesPresenter.presentedTypes.get(1)).isEqualTo("beststories");
        assertThat(storiesPresenter.presentedTypes.get(2)).isEqualTo("beststories");
    }

    @Test
    public void tabPresenterShowsTopStories_WhenTabNameIsNull() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected(null);

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
