package com.novoda.materialised.hackernews.asynclistview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.novoda.materialised.R
import com.novoda.materialised.databinding.StoryCardBinding
import com.novoda.materialised.hackernews.stories.view.UiStory

internal class StoryCardAdapter : RecyclerView.Adapter<DataBindingHolder<StoryCardBinding>>(), ModelledView<UiStory> {
    private val uiModels: MutableList<UiModel<UiStory>> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingHolder<StoryCardBinding> {
        val binding = DataBindingUtil.inflate<StoryCardBinding>(
            LayoutInflater.from(parent.context),
            R.layout.story_card,
            parent,
            false
        )
        return DataBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingHolder<StoryCardBinding>, position: Int) {
        holder.binding.uiState = uiModels[position]
    }

    override fun getItemCount(): Int = uiModels.size

    override fun getItemId(position: Int): Long = uiModels[position].data.id.toLong()

    override fun updateWith(uiModel: UiModel<UiStory>) =
        when (val originalUiStatePosition = uiModels.indexOfFirst { it.data.id == uiModel.data.id }) {
            -1 -> {
                uiModels.add(uiModel)
                notifyItemChanged(uiModels.size - 1)
            }
            else -> {
                uiModels[originalUiStatePosition] = uiModel
                notifyItemChanged(originalUiStatePosition)
            }
        }
}

internal class DataBindingHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
