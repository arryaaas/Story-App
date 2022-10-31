package com.arya.storyapp.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.room.Room
import com.arya.storyapp.R
import com.arya.storyapp.data.local.database.StoryDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class StackRemoteViewsFactory(
    private val storyDatabase: StoryDatabase,
    private val mContext: Context
) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<String>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
//        val database = StoryDatabase.getDatabase(mContext)
//        database.storyDao().getAllStoryForWidget().forEach {
//            mWidgetItems.add(it.photoUrl)
//        }
        storyDatabase.storyDao().getAllStoryForWidget().forEach {
            mWidgetItems.add(it.photoUrl)
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position])
            .apply(RequestOptions())
            .submit()
            .get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}