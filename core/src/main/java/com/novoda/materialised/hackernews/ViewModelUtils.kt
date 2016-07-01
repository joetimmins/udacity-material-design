package com.novoda.materialised.hackernews

fun <T : ViewModel> addNoOpClickListeners(viewModels: List<T>): List<ViewModelWithClickListener<T>> {
    return viewModels.map { viewModel -> addClickListener(viewModel, NoOpClickListener<T>()) }
}

fun <T : ViewModel> addClickListener(viewModel: T, clickListener: ClickListener<T>): ViewModelWithClickListener<T> {
    return ViewModelWithClickListener(viewModel, clickListener)
}
