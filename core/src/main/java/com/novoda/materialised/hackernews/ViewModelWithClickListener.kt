package com.novoda.materialised.hackernews

data class ViewModelWithClickListener<T : ViewModel>(
        val viewModel: T,
        val clickListener: ClickListener<T>
) {}
