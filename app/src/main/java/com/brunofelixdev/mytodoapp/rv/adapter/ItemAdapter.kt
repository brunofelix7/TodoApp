package com.brunofelixdev.mytodoapp.rv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.brunofelixdev.mytodoapp.rv.viewholder.ItemViewHolder
import com.brunofelixdev.mytodoapp.data.db.entity.Item
import com.brunofelixdev.mytodoapp.databinding.RowItemBinding
import com.brunofelixdev.mytodoapp.rv.listener.ItemClickListener

class ItemAdapter : PagingDataAdapter<Item, ItemViewHolder>(DIFF_CALLBACK) {

    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(root, listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
        }
    }
}