package com.novoda.materialised.hackernews

data class ViewModelWithClickListener<T : ViewModel<T>>(
        val viewModel: T,
        val clickListener: ClickListener<T>
) {}
