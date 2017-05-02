package com.novoda.materialised.hackernews.stories.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.novoda.materialised.R;
import com.novoda.materialised.databinding.StoryCardBinding;
import com.novoda.materialised.hackernews.asynclistview.ModelledView;
import com.novoda.materialised.hackernews.asynclistview.ViewModel;

public final class StoryCardView
        extends LinearLayout
        implements ModelledView<StoryViewData> {

    private StoryCardBinding storyCard;

    public StoryCardView(Context context) {
        super(context);
        init(context);
    }

    public StoryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoryCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void updateWith(@NonNull ViewModel<StoryViewData> viewModel) {
        storyCard.setViewData(viewModel.getViewData());
        storyCard.setViewBehaviour(viewModel);
    }

    private void init(Context context) {
        storyCard = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.story_card, this, true);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

}
