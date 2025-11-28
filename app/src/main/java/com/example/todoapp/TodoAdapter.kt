package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Todo RecyclerView Adapter
 * Todoリストの表示を管理
 */
class TodoAdapter(
    private val onToggleDone: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view, onToggleDone, onDelete)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder: 各Todoアイテムのビューを保持
     */
    class TodoViewHolder(
        itemView: View,
        private val onToggleDone: (Todo) -> Unit,
        private val onDelete: (Todo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val checkboxDone: CheckBox = itemView.findViewById(R.id.checkboxDone)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(todo: Todo) {
            textTitle.text = todo.title
            checkboxDone.isChecked = todo.isDone

            // 完了時は取り消し線を表示
            if (todo.isDone) {
                textTitle.paintFlags = textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textTitle.setTextColor(0xFF9E9E9E.toInt())
            } else {
                textTitle.paintFlags = textTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textTitle.setTextColor(0xFF000000.toInt())
            }

            // チェックボックスのイベント
            checkboxDone.setOnClickListener {
                onToggleDone(todo)
            }

            // 削除ボタンのイベント
            buttonDelete.setOnClickListener {
                onDelete(todo)
            }
        }
    }

    /**
     * DiffUtil: リストの変更を効率的に検出
     */
    class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
}