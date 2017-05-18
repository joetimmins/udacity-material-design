package com.novoda.materialised.hackernews.stories.provider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import kotlin.jvm.functions.Function1;

public class FirebaseSingleEventListenerTest {

    @Test
    public void invokeValueCallbackWithCorrectValue_whenDataSnapshotReturns() {
        TestObserver<String> testObserver = new TestObserver<>();
        String payload = "a string";
        DatabaseReference reference = FakeFirebase.databaseFor(payload).getReference();

        Function1<DataSnapshot, String> converter = new Function1<DataSnapshot, String>() {
            @Override
            public String invoke(DataSnapshot dataSnapshot) {
                return (String) dataSnapshot.getValue();
            }
        };

        Single<String> stringSingle = FirebaseSingleEventListener.listen(reference, converter);
        stringSingle.subscribe(testObserver);

        testObserver.assertValue(payload);
    }
}
