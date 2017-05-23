package com.novoda.materialised.hackernews.stories.provider;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

final class FirebaseStoryProvider implements StoryProvider {
    private final StoryObservableProvider storyObservableProvider;

    FirebaseStoryProvider(StoryObservableProvider storyObservableProvider) {
        this.storyObservableProvider = storyObservableProvider;
    }

    @NotNull
    @Override
    public Observable<Story> readItems(@NotNull List<Integer> ids) {
        return storyObservableProvider.createStoryObservables(ids)
                .reduce(new BiFunction<Observable<Story>, Observable<Story>, Observable<Story>>() {
                    @Override
                    public Observable<Story> apply(@NonNull Observable<Story> storyObservable, @NonNull Observable<Story> nextStoryObservable) throws Exception {
                        return storyObservable.mergeWith(nextStoryObservable);
                    }
                })
                .flatMapObservable(new Function<Observable<Story>, ObservableSource<? extends Story>>() {
                    @Override
                    public ObservableSource<? extends Story> apply(@NonNull Observable<Story> storyObservable) throws Exception {
                        return storyObservable;
                    }
                });
    }
}
