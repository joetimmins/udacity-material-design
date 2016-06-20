package com.novoda.materialised;

import com.novoda.materialised.hackernews.database.TopStories;
import com.novoda.materialised.hackernews.database.ValueCallback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

class FirebaseTopStories implements TopStories {
    private final List<Integer> topStories;

    public FirebaseTopStories(List<Integer> topStories) {
        this.topStories = topStories;
    }

    @Override
    public void readAll(@NotNull ValueCallback<List<Integer>> callback) {
        callback.onValueRetrieved(topStories);
    }
}
