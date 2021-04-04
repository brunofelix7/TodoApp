package com.brunofelixdev.mytodoapp.rv.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener

class ItemViewHolder constructor(
    private val binding: RowItemBinding,
    private val listener: ItemClickListener?,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Item) {
        binding.tvName.text = item.name
        item.apply {
            binding.tvDueDate.text = "${dueDateTime.getDueDateView()} ${dueDateTime.getDueTime()}"
        }
        binding.itemLayout.setOnClickListener {
            listener?.onItemClick(item)
        }
        binding.cbItem.setOnClickListener {
            listener?.onCheckedClick(item, binding.cbItem)
        }
        checkIfDateHasPassed(item)
    }

    private fun checkIfDateHasPassed(item: Item) {
        item.apply {
            val duration = dueDateTime.getDurationBetweenDates()

            if (duration < 0) {
                binding.ivTag.setColorFilter(ContextCompat.getColor(context, R.color.red_500))
                binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.red_500))
                binding.tvDueDate.setTextColor(ContextCompat.getColor(context, R.color.red_500))
            }
        }
    }
}