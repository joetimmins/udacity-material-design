package com.novoda.materialised.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FakeFirebase {

    public static FirebaseDatabase getTopStoriesFirebaseDatabase(final List<Long> expectedTopStories) {
        FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);

        when(mockFirebaseDatabase.getReference("v0")).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.child("topstories")).thenReturn(mockDatabaseReference);

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
}
