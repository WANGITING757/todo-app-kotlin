package com.example.todoapp.network

import retrofit2.Response
import retrofit2.http.*

/**
 * Todo API インターフェース
 * JSONPlaceholder API のエンドポイント定義
 */
interface TodoApiService {

    /**
     * Todoリストを取得
     */
    @GET("todos")
    suspend fun getTodos(
        @Query("userId") userId: Int = 1
    ): Response<List<TodoApiModel>>

    /**
     * 単一のTodoを取得
     */
    @GET("todos/{id}")
    suspend fun getTodo(@Path("id") id: Long): Response<TodoApiModel>

    /**
     * Todoを作成
     */
    @POST("todos")
    suspend fun createTodo(
        @Body todo: TodoCreateRequest
    ): Response<TodoApiModel>

    /**
     * Todoを更新
     */
    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: Long,
        @Body todo: TodoApiModel
    ): Response<TodoApiModel>

    /**
     * Todoを削除
     */
    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Long): Response<Unit>
}