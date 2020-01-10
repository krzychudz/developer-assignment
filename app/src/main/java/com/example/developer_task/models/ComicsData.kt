package com.example.developer_task.models

class ComicsData (
    val limit: Int,
    val total: Int,
    val count: Int,
    val offset: Int,
    val results: List<ComicsModel>
)