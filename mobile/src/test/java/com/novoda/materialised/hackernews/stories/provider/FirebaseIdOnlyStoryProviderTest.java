package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.materialised.hackernews.section.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseIdOnlyStoryProviderTest {

    @Test
    public void testThatTopStoriesCallsBackWithIdList() {
        // Arrange
        Story blankStory = new Story();
        List<Long> expectedStoryIds = Arrays.asList(8863L, 9001L, 9004L);
        List<Story> expectedIdOnlyStories = new ArrayList<>(expectedStoryIds.size());
        for (Long id : expectedStoryIds) {
            Story onlyId = blankStory.copy(
                    blankStory.getBy(), blankStory.getDescendants(), id.intValue(), blankStory.getKids(), blankStory.getScore(), blankStory.getTime(), blankStory.getTitle(), blankStory.getType(), blankStory.getUrl()
            );
            expectedIdOnlyStories.add(onlyId);
        }

        IdOnlyStoryValueCallback callback = new IdOnlyStoryValueCallback();
        FirebaseDatabase storyTypeFirebaseDatabase = FakeFirebase.getDatabaseForStoryType(Section.BEST, expectedStoryIds);

        // Act
        new FirebaseIdOnlyStoryProvider(storyTypeFirebaseDatabase).readStoryIds(Section.BEST, callback);

        // Assert
        assertThat(callback.stories).isEqualTo(expectedIdOnlyStories);
    }

    private static class IdOnlyStoryValueCallback implements ValueCallback<List<Story>> {

        List<Story> stories;

        @Override
        public void onValueRetrieved(List<Story> value) {
            stories = value;
        }
    }
}
