package com.novoda.materialised.hackernews.stories

import android.content.Context
import android.content.Intent
import android.net.Uri

import com.novoda.materialised.hackernews.navigator.Navigator

internal class IntentNavigator(private val context: Context) : Navigator {

    override fun navigateTo(uri: String) = context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}
