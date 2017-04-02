package com.novoda.materialised.hackernews.asynclistview;

import java.util.List;

public interface AsyncListView<T extends ViewData<Integer>> {
    void updateWith(List<ViewModel<T>> initialViewModelList);

    void updateWith(ViewModel<T> viewModel);

    void showError();
}
