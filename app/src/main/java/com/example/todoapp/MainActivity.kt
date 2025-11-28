package com.example.todoapp

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding

/**
 * メインアクティビティ
 * Todoリストの表示と操作を管理
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding の初期化
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel の初期化
        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        // RecyclerView の設定
        setupRecyclerView()

        // ボタンのイベント設定
        setupButtons()

        // LiveData の監視
        observeData()
    }

    /**
     * RecyclerView の初期化
     */
    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            onToggleDone = { todo ->
                viewModel.toggleDone(todo)
            },
            onDelete = { todo ->
                viewModel.delete(todo)
            }
        )

        binding.recyclerViewTodos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    /**
     * ボタンイベントの設定
     */
    private fun setupButtons() {
        // 追加ボタン
        binding.buttonAdd.setOnClickListener {
            addTodo()
        }

        // Enterキーで追加
        binding.editTextTodo.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                addTodo()
                true
            } else {
                false
            }
        }
    }

    /**
     * Todoを追加
     */
    private fun addTodo() {
        val title = binding.editTextTodo.text.toString().trim()

        if (title.isNotEmpty()) {
            viewModel.insert(title)
            binding.editTextTodo.text.clear()
        }
    }

    /**
     * LiveData の監視
     */
    private fun observeData() {
        viewModel.allTodos.observe(this) { todos ->
            todos?.let {
                // リストを更新
                adapter.submitList(it)

                // 統計を更新
                updateStats(it)

                // 空のメッセージを表示/非表示
                if (it.isEmpty()) {
                    binding.recyclerViewTodos.visibility = View.GONE
                    binding.textEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewTodos.visibility = View.VISIBLE
                    binding.textEmpty.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 統計情報を更新
     */
    private fun updateStats(todos: List<Todo>) {
        val total = todos.size
        val completed = todos.count { it.isDone }
        val uncompleted = total - completed

        binding.textTotal.text = total.toString()
        binding.textCompleted.text = completed.toString()
        binding.textUncompleted.text = uncompleted.toString()
    }
}