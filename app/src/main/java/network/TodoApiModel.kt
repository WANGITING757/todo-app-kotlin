package com.example.todoapp.network

import com.google.gson.annotations.SerializedName

/**
 * API レスポンスモデル
 * JSONPlaceholder の Todo 形式
 */
data class TodoApiModel(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("userId")
    val userId: Int = 1
)

/**
 * API リクエストモデル
 */
data class TodoCreateRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("completed")
    val completed: Boolean = false,

    @SerializedName("userId")
    val userId: Int = 1
)