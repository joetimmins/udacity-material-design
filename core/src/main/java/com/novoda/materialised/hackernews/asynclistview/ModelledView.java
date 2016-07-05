package com.novoda.materialised.hackernews.asynclistview;

public interface ModelledView<T extends ViewModel> {
    void updateWith(T viewModel);
}
