package com.novoda.materialised.hackernews.asynclistview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

internal class SingleTypeAdapter<T, V>(private val viewInflater: ModelledViewInflater<V>) :
    RecyclerView.Adapter<ModelledViewHolder<V>>() where T : ViewData<Int>, V : View, V : ModelledView<T> {
    private val viewModels: MutableList<ViewModel<T>>

    init {
        this.viewModels = ArrayList()
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelledViewHolder<V> {
        val view = viewInflater.inflateUsing(parent)
        return ModelledViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelledViewHolder<V>, position: Int) {
        val view = holder.obtainHeldView()
        val viewModel = viewModels[position]
        view.updateWith(viewModel)
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    override fun getItemId(position: Int): Long {
        return idFor(position)!!.toLong()
    }

    fun updateWith(viewModel: ViewModel<T>) {
        for (i in viewModels.indices) {
            if (shouldUpdate(i, viewModel)) {
                viewModels[i] = viewModel
                notifyItemChanged(i)
                return
            }
        }
        viewModels.add(viewModel)
        notifyItemChanged(viewModels.size - 1)
    }

    private fun shouldUpdate(position: Int, fullyPopulatedViewModel: ViewModel<T>): Boolean {
        val id = idFor(position)
        val fullyPopulatedViewModelId = fullyPopulatedViewModel.viewData.id
        return id == fullyPopulatedViewModelId
    }

    private fun idFor(position: Int): Int? {
        return viewModels[position].viewData.id
    }
}
