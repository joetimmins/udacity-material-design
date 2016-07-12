package com.novoda.materialised.hackernews.topstories;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.novoda.materialised.R;
import com.novoda.materialised.databinding.StoryCardBinding;
import com.novoda.materialised.hackernews.asynclistview.ModelledView;
import com.novoda.materialised.hackernews.topstories.view.StoryViewData;
import com.novoda.materialised.hackernews.topstories.view.StoryViewModel;

public final class StoryCardView
        extends LinearLayout
        implements ModelledView<StoryViewModel> {

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
    public void updateWith(@NonNull final StoryViewModel viewModel) {
        final StoryViewData viewData = viewModel.getViewData();

        storyCard.titleView.setText(viewData.getTitle());

        storyCard.storyScore.setText(String.valueOf(viewData.getScore()));
        storyCard.storyComments.setText(viewData.commentCount());
        storyCard.storySubmittedFrom.setText(viewData.submittedFrom());

        storyCard.fullWidthCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getViewBehaviour().onClick(viewData);
            }
        });
    }

    private void init(Context context) {
        storyCard = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.story_card, this, true);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }
}
