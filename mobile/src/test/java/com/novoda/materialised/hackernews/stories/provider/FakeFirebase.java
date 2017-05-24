package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novoda.materialised.hackernews.section.Section;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

final class FakeFirebase {

    static FirebaseDatabase getDatabaseForStoryType(Section section, final List<Long> expectedTopStories) {
        FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);

        when(mockFirebaseDatabase.getReference("v0")).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.child(section.getId())).thenReturn(mockDatabaseReference);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);

                when(mockDataSnapshot.getValue()).thenReturn(expectedTopStories);

                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                valueEventListener.onDataChange(mockDataSnapshot);
                return null;
            }
        }).when(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        return mockFirebaseDatabase;
    }

    static FirebaseDatabase getItemsDatabase(final List<Story> stories) {
        FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);

        when(mockFirebaseDatabase.getReference("v0")).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.child("item")).thenReturn(mockDatabaseReference);

        for (final Story story : stories) {
            DatabaseReference storyNode = mock(DatabaseReference.class);

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
                    when(mockDataSnapshot.getValue(Story.class)).thenReturn(story);
                    ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                    valueEventListener.onDataChange(mockDataSnapshot);
                    return null;
                }
            }).when(storyNode).addListenerForSingleValueEvent(any(ValueEventListener.class));

            when(mockDatabaseReference.child(Integer.toString(story.getId()))).thenReturn(storyNode);
        }

        return mockFirebaseDatabase;
    }

    static <T> FirebaseDatabase databaseFor(final T payload) {
        FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);

        when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseReference);
        when(mockFirebaseDatabase.getReference(anyString())).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
                when(mockDataSnapshot.getValue()).thenReturn(payload);

                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                valueEventListener.onDataChange(mockDataSnapshot);
                return null;
            }
        }).when(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        return mockFirebaseDatabase;
    }
}
