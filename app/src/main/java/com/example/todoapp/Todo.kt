package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Todo データエンティティ
 * Room データベースのテーブル定義
 */
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,        // タスクの内容

    val isDone: Boolean = false,  // 完了状態

    val createdAt: Long = System.currentTimeMillis()  // 作成時刻
)