package com.goflow.app.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.goflow.app.R

class SettingsSuggestionPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : Preference(context, attrs, defStyleAttr) {

    private var onCancelClickListener: View.OnClickListener? = null

    init {
        layoutResource = R.layout.preference_suggestion
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.findViewById<ImageButton>(R.id.cancel)?.setOnClickListener(onCancelClickListener)
    }

    fun setOnPreferenceCancelListener(listener: View.OnClickListener?) {
        onCancelClickListener = listener
    }
}
