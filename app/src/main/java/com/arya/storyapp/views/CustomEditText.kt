package com.arya.storyapp.views

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.widget.addTextChangedListener
import com.arya.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditText : TextInputEditText {

    constructor(
        context: Context
    ) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { text: CharSequence?, _, _, _ ->
            val textInputLayout = parent.parent as TextInputLayout

            when (inputType) {
                EMAIL -> {
                    if (isEmailValid(text)) {
                        textInputLayout.error = context.getString(R.string.email_validation)
                    } else {
                        textInputLayout.error = null
                    }
                }
                PASSWORD -> {
                    if (isPasswordValid(text)) {
                        textInputLayout.error = context.getString(R.string.password_validation)
                    } else {
                        textInputLayout.error = null
                    }
                }
            }
        })
    }

    private fun isEmailValid(text: CharSequence?): Boolean =
        !text.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(text).matches()

    private fun isPasswordValid(text: CharSequence?): Boolean =
        !text.isNullOrEmpty() && text.length < 6

    companion object {
        const val EMAIL = 0x00000021
        const val PASSWORD = 0x00000081
    }
}