package com.goflow.app.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goflow.app.R
import com.goflow.app.onboarding.HomeAssistantInstance
import timber.log.Timber

class InstanceViewHolder(v: View, val onClick: (HomeAssistantInstance) -> Unit) :
    RecyclerView.ViewHolder(v),
    View.OnClickListener {

    private val name: TextView = v.findViewById(R.id.txt_name)
    var server: HomeAssistantInstance? = null
        set(value) {
            name.text = value?.name
            field = value
        }

    init {
        v.setOnClickListener {
            server?.let { onClick(it) }
        }
    }

    override fun onClick(v: View) {
        Timber.tag("ServerListAdapter").d("Clicked")
    }
}
