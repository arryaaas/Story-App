package com.arya.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.databinding.ItemStoryBinding
import com.arya.storyapp.utils.withDateFormat
import com.bumptech.glide.Glide

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((StoryEntity, FragmentNavigator.Extras) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        return StoriesViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val storyItem = getItem(position)
        if (storyItem != null) {
            holder.bind(storyItem)
        }
    }

    inner class StoriesViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyEntity: StoryEntity) {
            with(binding) {
                profileImageView.transitionName = "profile_${storyEntity.id}"

                photoImageView.transitionName = "photo_${storyEntity.id}"
                Glide.with(itemView.context)
                    .load(storyEntity.photoUrl)
                    .into(photoImageView)

                nameTextView.transitionName = "name_${storyEntity.id}"
                nameTextView.text = storyEntity.name

                dateTextView.transitionName = "date_${storyEntity.id}"
                dateTextView.text = storyEntity.createdAt.withDateFormat()

                descriptionTextView.transitionName = "description_${storyEntity.id}"
                descriptionTextView.text = storyEntity.description

                itemView.setOnClickListener {
                    val extras = FragmentNavigatorExtras(
                        profileImageView to "profile",
                        nameTextView to "name",
                        dateTextView to "date",
                        photoImageView to "photo",
                        descriptionTextView to "description"
                    )
                    onItemClick?.invoke(storyEntity, extras)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object :DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}