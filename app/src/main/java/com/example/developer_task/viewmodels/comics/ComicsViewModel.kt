package com.example.developer_task.viewmodels.comics

import com.example.developer_task.models.ComicsModel

class ComicsViewModel(private val comics: ComicsModel) {
    val title: String
    get() = if (comics.title != null && comics.title.isNotEmpty()) comics.title else "No description found"

    val description: String
    get() = if (comics.description != null && comics.description.isNotEmpty()) comics.description else "No title found"

    val thumbnailUrl: String = "${comics.thumbnail.path}.${comics.thumbnail.extension}"

}