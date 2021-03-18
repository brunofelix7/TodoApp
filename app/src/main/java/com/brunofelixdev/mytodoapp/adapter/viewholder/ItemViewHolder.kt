package com.brunofelixdev.mytodoapp.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding

class ItemViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.tvName.text = item.name
        binding.tvDueDate.text = item.dueDate
    }
}