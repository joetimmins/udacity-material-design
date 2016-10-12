package com.novoda.materialised.hackernews.topstories;

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
        assertThat(storiesPresenter.presentedType).isEqualTo("topstories");
    }

    @Test
    public void tabListenerMapsTopStoriesTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("Top Stories");

        // Assert
        assertThat(storiesPresenter.presentedType).isEqualTo("topstories");
    }

    @Test
    public void tabListenerMapsNewTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("New");

        // Assert
        assertThat(storiesPresenter.presentedType).isEqualTo("newstories");
    }

    @Test
    public void tabListenerMapsBestTypeCorrectly() {
        // Arrange
        CapturingTypedPresenter storiesPresenter = new CapturingTypedPresenter();
        TabPresenter tabPresenter = new TabPresenter(storiesPresenter);

        // Act
        tabPresenter.tabSelected("Best");

        // Assert
        assertThat(storiesPresenter.presentedType).isEqualTo("beststories");
    }

    private class CapturingTypedPresenter implements TypedPresenter<String> {
        String presentedType;

        @Override
        public void present(@NotNull String type) {
            presentedType = type;
        }
    }
}
