package com.novoda.materialised.hackernews

fun <T : ViewModel> addNoOpClickListeners(viewModels: List<T>): List<ViewModelWithClickListener<T>> {
//    val result = ArrayList<ViewModelWithClickListener<T>>()
//    viewModels.forEach { viewModel -> result.add(addClickListener(viewModel, NoOpClickListener<T>())) }
//    return result

    return viewModels.map { viewModel -> addClickListener(viewModel, NoOpClickListener<T>()) }
}

fun <T : ViewModel> addClickListener(viewModel: T, clickListener: ClickListener<T>): ViewModelWithClickListener<T> {
    return ViewModelWithClickListener(viewModel, clickListener)
}
