package com.novoda.materialised.hackernews.asynclistview;

import java.util.List;

public interface AsyncListView<T extends ViewModel> {
    void updateWith(List<T> initialViewModelList);

    void updateWith(T viewModel);

    void showError();
}
