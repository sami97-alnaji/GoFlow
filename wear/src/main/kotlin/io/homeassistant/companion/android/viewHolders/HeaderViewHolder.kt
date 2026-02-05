package com.goflow.app.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goflow.app.R

class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val headerTextView = v.findViewById<TextView>(R.id.headerTextView)
}
