package com.novoda.materialised.hackernews.stories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.novoda.materialised.hackernews.navigator.Navigator;

import org.jetbrains.annotations.NotNull;

final class IntentNavigator implements Navigator {
    private final Context context;

    IntentNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void navigateTo(@NotNull String uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }
}
