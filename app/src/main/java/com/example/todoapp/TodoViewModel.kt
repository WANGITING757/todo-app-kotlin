package com.example.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Todo ViewModel
 * UIとデータ層の間のビジネスロジック
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository
    val allTodos: LiveData<List<Todo>>

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(todoDao)
        allTodos = repository.allTodos
    }

    /**
     * 新しいTodoを追加
     */
    fun insert(title: String) = viewModelScope.launch {
        val todo = Todo(title = title)
        repository.insert(todo)
    }

    /**
     * Todoの完了状態を切り替え
     */
    fun toggleDone(todo: Todo) = viewModelScope.launch {
        val updatedTodo = todo.copy(isDone = !todo.isDone)
        repository.update(updatedTodo)
    }

    /**
     * Todoを削除
     */
    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }
}