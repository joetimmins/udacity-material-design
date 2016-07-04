package com.novoda.materialised.hackernews.stories;

import com.novoda.materialised.hackernews.generics.ViewModel;

public interface UpdatableView<T extends ViewModel> {
    void updateWith(T viewModel);
}
