package com.goflow.app.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.goflow.app.R
import com.goflow.app.databinding.ViewLoadingBinding

class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    init {
        val binding = ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)

        context.withStyledAttributes(attrs, R.styleable.LoadingView) {
            binding.loadingText.text = getString(R.styleable.LoadingView_loading_text)
        }
    }
}
