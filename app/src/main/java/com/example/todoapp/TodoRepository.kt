package com.example.todoapp

import androidx.lifecycle.LiveData

/**
 * Todo リポジトリ
 * ViewModelとデータベース間の仲介役
 */
class TodoRepository(private val todoDao: TodoDao) {

    // LiveData で全てのTodoを監視
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    /**
     * Todoを追加
     */
    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    /**
     * Todoを更新
     */
    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    /**
     * Todoを削除
     */
    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }
}