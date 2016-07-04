package com.novoda.materialised.hackernews.generics;

import java.util.List;

public interface AsyncListView<T extends ViewModel<T>> {
    void updateWith(List<T> viewModels);
    void updateWith(T viewModel);
}
