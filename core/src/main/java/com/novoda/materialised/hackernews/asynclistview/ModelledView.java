package com.novoda.materialised.hackernews.asynclistview;

public interface ModelledView<T> {
    void updateWith(ViewModel<T> viewModel);
}
