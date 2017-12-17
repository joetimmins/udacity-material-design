package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DatabaseReference;
import com.novoda.materialised.hackernews.stories.provider.DatabaseReferenceWrapper.WorkaroundForTypeErasure;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DatabaseReferenceWrapperTest {
    @Test
    public void testSomething() {
        WorkaroundForTypeErasure<List<Long>> listOfLongsType = new WorkaroundForTypeErasure<List<Long>>() {
        };
        DatabaseReferenceWrapper wrapper = new DatabaseReferenceWrapper(mock(DatabaseReference.class));
        Type[] something = wrapper.something(listOfLongsType);

        assertThat(something).isEmpty();
    }

}
