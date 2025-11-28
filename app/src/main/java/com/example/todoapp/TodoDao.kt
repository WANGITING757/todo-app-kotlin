package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Todo データアクセスオブジェクト (DAO)
 * データベース操作のインターフェース
 */
@Dao
interface TodoDao {

    /**
     * 全てのTodoを取得（作成日時の新しい順）
     */
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): LiveData<List<Todo>>

    /**
     * Todoを挿入
     */
    @Insert
    suspend fun insert(todo: Todo)

    /**
     * Todoを更新
     */
    @Update
    suspend fun update(todo: Todo)

    /**
     * Todoを削除
     */
    @Delete
    suspend fun delete(todo: Todo)

    /**
     * 全てのTodoを削除
     */
    @Query("DELETE FROM todos")
    suspend fun deleteAll()
}