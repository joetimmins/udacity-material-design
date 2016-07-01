package com.novoda.materialised.hackernews;

import org.jetbrains.annotations.NotNull;

public class NoOpClickListener<T extends ViewModel> implements ClickListener<T> {
    @Override
    public void onClick(@NotNull T viewModel) {
        // No-op
    }
}
