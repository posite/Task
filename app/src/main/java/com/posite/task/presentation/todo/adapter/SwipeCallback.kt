package com.posite.task.presentation.todo.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.posite.task.R

class SwipeCallback(
    val context: Context,
    private val adapter: TaskListAdapter
) : ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val targetIndex = viewHolder.adapterPosition
        val targetItem = adapter.onItemMoved(targetIndex)

        if (direction == ItemTouchHelper.LEFT) {
            adapter.removeTask(targetItem)
            adapter.editTask(targetItem)
        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.removeTask(targetItem)

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
}