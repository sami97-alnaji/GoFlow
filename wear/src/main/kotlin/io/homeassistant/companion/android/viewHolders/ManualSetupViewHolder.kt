package com.goflow.app.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goflow.app.R

class ManualSetupViewHolder(v: View, val onClick: () -> Unit) : RecyclerView.ViewHolder(v) {

    val text: TextView = v.findViewById(R.id.txt_name)

    init {
        // Set onclick listener
        v.setOnClickListener {
            onClick()
        }
    }
}
