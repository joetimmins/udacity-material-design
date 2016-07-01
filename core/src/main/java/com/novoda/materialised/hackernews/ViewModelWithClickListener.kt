package com.novoda.materialised.hackernews

data class ViewModelWithClickListener<V : ViewModel>(
        val viewModel: V,
        val clickListener: ClickListener<V>
) {}
