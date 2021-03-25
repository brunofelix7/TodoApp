package com.brunofelixdev.mytodoapp.rv.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener

class ItemViewHolder constructor(
    private val binding: RowItemBinding,
    private val listener: ItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Item) {
        binding.tvName.text = item.name
        binding.tvDueDate.text = "${item.dueDate} - ${item.dueTime}"
        binding.itemLayout.setOnClickListener {
            listener?.onItemClick(item)
        }
        binding.cbItem.setOnClickListener {
            listener?.onCheckedClick(item, binding.cbItem)
        }
    }
}