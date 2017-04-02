package com.novoda.materialised.hackernews.asynclistview;

import java.util.List;

public interface AsyncListView<T extends ViewData<Integer>> {
    void updateWith(List<DefaultViewModel<T>> initialViewModelList);

    void updateWith(DefaultViewModel<T> viewModel);

    void showError();
}
