package com.novoda.materialised.hackernews.asynclistview;

public interface UpdatableView<T extends ViewModel> {
    void updateWith(T viewModel);
}
