package com.novoda.materialised.hackernews.generics;

import java.util.List;

public interface AsyncListView<T extends ViewModel> {
    void updateWith(List<T> initialViewModelList);
    void updateWith(T viewModel);
}
