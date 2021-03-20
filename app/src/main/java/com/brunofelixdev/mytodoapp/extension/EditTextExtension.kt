package com.brunofelixdev.mytodoapp.extension

import android.widget.EditText
import com.brunofelixdev.mytodoapp.util.MaskUtil

fun EditText.myCustomMask(mask: String) {
    addTextChangedListener(MaskUtil(this, mask))
}