package com.novoda.materialised.hackernews

interface ClickListener<T : ViewModel> {
    fun onClick(viewModel: T)
}

class NoOpClickListener<T : ViewModel> : ClickListener<T> {
    override fun onClick(viewModel: T) {
        // No-op
    }
}
