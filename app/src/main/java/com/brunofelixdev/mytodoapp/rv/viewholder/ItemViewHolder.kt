package com.brunofelixdev.mytodoapp.rv.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.R
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding
import com.brunofelixdev.mytodoapp.extension.getDateMarker
import com.brunofelixdev.mytodoapp.extension.getDurationBetweenDates
import com.brunofelixdev.mytodoapp.extension.parseToDate
import com.brunofelixdev.mytodoapp.extension.parseToString
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener

class ItemViewHolder constructor(
    private val binding: RowItemBinding,
    private val listener: ItemClickListener?,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Item) {
        binding.tvName.text = item.name
        binding.tvDueDate.text = "${item.dueDate} ${item.dueTime.getDateMarker()}"
        binding.itemLayout.setOnClickListener {
            listener?.onItemClick(item)
        }
        binding.cbItem.setOnClickListener {
            listener?.onCheckedClick(item, binding.cbItem)
        }
        checkIfDateHasPassed(item)
    }

    private fun checkIfDateHasPassed(item: Item) {
        val dueDateTime = "${item.dueDate.parseToDate("EEE, MMM dd, yyyy")
            ?.parseToString("MM-dd-yyyy")} ${item.dueTime}"
        val duration = getDurationBetweenDates(dueDateTime)

        if (duration < 0) {
            binding.ivTag.setColorFilter(ContextCompat.getColor(context, R.color.red_500))
            binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.red_500))
            binding.tvDueDate.setTextColor(ContextCompat.getColor(context, R.color.red_500))
        }
    }
}