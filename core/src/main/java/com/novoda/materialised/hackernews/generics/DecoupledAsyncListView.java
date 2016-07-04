package com.novoda.materialised.hackernews.generics;

import java.util.List;

public interface DecoupledAsyncListView<T extends DecoupledViewModel> {
    void updateWith(List<T> initialViewModelList);
    void updateWith(T viewModel);
}
