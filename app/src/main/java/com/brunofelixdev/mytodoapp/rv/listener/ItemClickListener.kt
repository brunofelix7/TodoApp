package com.brunofelixdev.mytodoapp.rv.listener

import android.widget.CheckBox
import com.brunofelixdev.mytodoapp.data.db.Item

interface ItemClickListener {
    fun onCheckedClick(item: Item, cbItem: CheckBox)
    fun onItemClick(item: Item)
}