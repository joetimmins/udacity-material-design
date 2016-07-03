package com.novoda.materialised.hackernews

fun <T : ViewModel<T>> addNoOpClickListeners(viewModels: List<T>): List<ViewModelWithClickListener<T>> {
    return viewModels.map { viewModel -> ViewModelWithClickListener(viewModel, NoOpClickListener()) }
}
