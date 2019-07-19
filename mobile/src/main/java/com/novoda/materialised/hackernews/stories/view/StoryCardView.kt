package com.novoda.materialised.hackernews.stories.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.novoda.materialised.R
import com.novoda.materialised.databinding.StoryCardBinding
import com.novoda.materialised.hackernews.asynclistview.ModelledView
import com.novoda.materialised.hackernews.asynclistview.UiState

class StoryCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ModelledView<StoryUiData> {

    private var storyCard: StoryCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.story_card, this, true)

    init {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setLayoutParams(layoutParams)
    }

    override fun updateWith(uiState: UiState<StoryUiData>) {
        storyCard.viewData = uiState.data
        storyCard.viewModel = uiState
    }
}
