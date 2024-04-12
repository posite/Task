package com.posite.task.presentation.todo.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.posite.task.R
import com.posite.task.presentation.todo.model.UserTask

class SwipeCallback(
    val context: Context,
    private val recyclerView: RecyclerView,
    private val adapter: TaskListAdapter
) : ItemTouchHelper.Callback() {

    private var recentlySwipedItem: Pair<Int, UserTask>? = null
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val targetIndex = viewHolder.adapterPosition
        val targetItem = adapter.onItemMoved(targetIndex)
        recentlySwipedItem = Pair(targetIndex, targetItem)
        if (direction == ItemTouchHelper.LEFT) {
            adapter.removeTask(targetItem)
            adapter.editTask(targetItem)

        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.removeTask(targetItem)
            showUndoAddSnackbar()
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val viewItem = viewHolder.itemView
            if (dX < 0) {
                SwipeBackgroundHelper.paintDrawCommandToStart(
                    c,
                    viewItem,
                    R.drawable.edit_white_24dp,
                    R.color.white,
                    dX
                )
            } else if (dX > 0) {
                SwipeBackgroundHelper.paintDrawCommandToStart(
                    c,
                    viewItem,
                    R.drawable.task_alt_white_24dp,
                    R.color.white,
                    dX
                )
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun showUndoAddSnackbar() {
        val snackbar = Snackbar.make(context, recyclerView, "Task 완료됨", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            // Undo 버튼을 누르면 실행되는 작업
            recentlySwipedItem?.let { (index, item) ->
                adapter.addTask(item) // 이전에 swipe한 아이템을 다시 추가
                recyclerView.scrollToPosition(index) // 아이템이 추가된 위치로 스크롤
                recentlySwipedItem = null // 임시 저장된 아이템 초기화
            }
        }
        snackbar.setBackgroundTint(context.getColor(R.color.white))
        snackbar.setTextColor(context.getColor(R.color.highlight))
        snackbar.setActionTextColor(context.getColor(R.color.highlight))
        snackbar.show()
    }

    fun undoSwipe() {
        recentlySwipedItem?.let { (index, item) ->
            adapter.addTask(item)
            recyclerView.scrollToPosition(index)
            recentlySwipedItem = null
        }
    }
}