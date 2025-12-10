package com.example.todoapp

import androidx.lifecycle.LiveData
import com.example.todoapp.network.RetrofitClient
import com.example.todoapp.network.TodoApiModel
import com.example.todoapp.network.TodoCreateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Todo リポジトリ（オフライン優先 + API 同期）
 */
class TodoRepository(private val todoDao: TodoDao) {

    private val apiService = RetrofitClient.apiService

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    /**
     * サーバーからデータを同期
     */
    suspend fun syncWithServer() = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTodos()
            if (response.isSuccessful) {
                response.body()?.let { remoteTodos ->
                    // サーバーのデータをローカルに保存
                    remoteTodos.forEach { apiTodo ->
                        val localTodo = Todo(
                            id = apiTodo.id,
                            title = apiTodo.title,
                            isDone = apiTodo.completed,
                            createdAt = System.currentTimeMillis()
                        )
                        todoDao.insert(localTodo)
                    }
                }
            }
        } catch (e: Exception) {
            // ネットワークエラー時はローカルデータを使用
            android.util.Log.e("TodoRepository", "Sync error", e)
        }
    }

    /**
     * Todoを追加（ローカル + サーバー）
     */
    suspend fun insert(todo: Todo) = withContext(Dispatchers.IO) {
        // まずローカルに保存（オフライン対応）
        todoDao.insert(todo)

        // サーバーに同期
        try {
            val request = TodoCreateRequest(
                title = todo.title,
                completed = todo.isDone
            )
            apiService.createTodo(request)
        } catch (e: Exception) {
            android.util.Log.e("TodoRepository", "API error", e)
            // サーバー同期失敗時はローカルに保存済みなので継続
        }
    }

    /**
     * Todoを更新
     */
    suspend fun update(todo: Todo) = withContext(Dispatchers.IO) {
        todoDao.update(todo)

        // サーバーに同期
        try {
            val apiTodo = TodoApiModel(
                id = todo.id,
                title = todo.title,
                completed = todo.isDone
            )
            apiService.updateTodo(todo.id, apiTodo)
        } catch (e: Exception) {
            android.util.Log.e("TodoRepository", "API error", e)
        }
    }

    /**
     * Todoを削除
     */
    suspend fun delete(todo: Todo) = withContext(Dispatchers.IO) {
        todoDao.delete(todo)

        // サーバーから削除
        try {
            apiService.deleteTodo(todo.id)
        } catch (e: Exception) {
            android.util.Log.e("TodoRepository", "API error", e)
        }
    }
}