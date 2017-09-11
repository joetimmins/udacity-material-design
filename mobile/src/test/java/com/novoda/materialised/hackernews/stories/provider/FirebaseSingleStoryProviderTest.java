package com.novoda.materialised.hackernews.stories.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static org.fest.assertions.api.Assertions.assertThat;

public class FirebaseSingleStoryProviderTest {

    @Test
    public void readItemsCallsBackOncePerValueRetrieved() {
        int firstStoryId = 12;
        int secondStoryId = 34;
        Story firstStory = new Story("author", 890, firstStoryId, Arrays.asList(1, 2), 4, 1232, "test title", "test type", "http://test.url");
        final Story secondStory = new Story("another author", 567, secondStoryId, Arrays.asList(3, 4), 5, 7897, "another title", "another type", "http://another.url");
        List<Story> stories = Arrays.asList(firstStory, secondStory);
        FirebaseSingleStoryProvider provider = new FirebaseSingleStoryProvider(FakeFirebase.getItemsDatabase(stories));

        Single<Story> firstStorySingle = provider.obtainStory(firstStoryId);
        Single<Story> secondStorySingle = provider.obtainStory(secondStoryId);
        final List<Story> actualStories = new ArrayList<>();

        Consumer<Story> consumer = new Consumer<Story>() {
            @Override
            public void accept(@NonNull Story story) throws Exception {
                actualStories.add(story);
            }
        };
        firstStorySingle.subscribe(consumer);
        secondStorySingle.subscribe(consumer);

        assertThat(actualStories).isEqualTo(stories);
    }
}
