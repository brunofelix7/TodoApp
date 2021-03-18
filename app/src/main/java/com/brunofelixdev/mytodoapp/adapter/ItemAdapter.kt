package com.brunofelixdev.mytodoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding

class ItemAdapter : PagingDataAdapter<Item, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root =  RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: Item? = getItem(position)
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item?) {
            binding.tvName.text = item?.name
            binding.tvDueDate.text = item?.dueDate
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
        }
    }
}