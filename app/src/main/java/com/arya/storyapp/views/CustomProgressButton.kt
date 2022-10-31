package com.arya.storyapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ButtonProgressBinding

class CustomProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null
    private var state: State = State.Normal
        set(value) {
            field = value
            refreshState()
        }

    private val binding = ButtonProgressBinding
        .inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        loadAttr(attrs)
        refreshState()
    }

    private fun loadAttr(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomProgressButton
            )

            setBackgroundResource(R.drawable.bg_progress_btn)

            val titleId = attributes.getResourceId(
                R.styleable.CustomProgressButton_progress_button_title,
                0
            )

            val loadingTitleId = attributes.getResourceId(
                R.styleable.CustomProgressButton_progress_button_loading_title,
                0
            )

            if (titleId != 0) {
                title = context.getString(titleId)
            }

            if (loadingTitleId != 0) {
                loadingTitle = context.getString(loadingTitleId)
            }

            attributes.recycle()
        }
    }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        binding.tv.let {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }

        binding.pb.visibility = state.visibility

        when(state) {
            State.Normal -> binding.tv.text = title
            State.Loading -> binding.tv.text = loadingTitle
        }
    }

    fun setLoading(condition: Boolean) {
        state = if (condition) {
            State.Loading
        } else {
            State.Normal
        }
    }

    enum class State(val isEnabled: Boolean, val visibility: Int) {
        Normal(true, View.GONE),
        Loading(false, View.VISIBLE)
    }
}