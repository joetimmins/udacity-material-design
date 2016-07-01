package com.novoda.materialised.hackernews

interface ClickListener<T : ViewModel> {
    fun onClick(viewModel: T)
}
