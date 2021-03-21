package com.brunofelixdev.mytodoapp.rv.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.brunofelixdev.mytodoapp.rv.viewholder.ItemLoadStateViewHolder

class ItemLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ItemLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: ItemLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemLoadStateViewHolder {
        return ItemLoadStateViewHolder.create(parent, retry)
    }
}