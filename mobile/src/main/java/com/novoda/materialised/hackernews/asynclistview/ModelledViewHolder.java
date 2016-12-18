package com.novoda.materialised.hackernews.asynclistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

final class ModelledViewHolder<V extends View & ModelledView> extends RecyclerView.ViewHolder {

    private final V heldModelledView;

    ModelledViewHolder(V itemView) {
        super(itemView);
        heldModelledView = itemView;
    }

    V obtainHeldView() {
        return heldModelledView;
    }
}
