package com.example.developer_task.adapters.viewholders

import android.view.View
import kotlinx.android.synthetic.main.item_comics.view.*

class ComicsViewHolder(view: View) : BaseViewHolder(view) {
    val comicsImage = view.comicsImage
    val comicsTitle = view.comicsTitle
    val comicsDescription = view.comicsDescription
}