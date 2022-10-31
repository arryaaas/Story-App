package com.arya.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.arya.storyapp.data.local.database.StoryDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StackWidgetService : RemoteViewsService() {

    @Inject
    lateinit var storyDatabase: StoryDatabase

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(storyDatabase, this.applicationContext)
}