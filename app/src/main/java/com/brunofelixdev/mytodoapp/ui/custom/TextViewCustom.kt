package com.brunofelixdev.mytodoapp.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.brunofelixdev.mytodoapp.R

@SuppressLint("AppCompatCustomView")
class TextViewCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    companion object {
        private val TAG: String = TextViewCustom::class.java.simpleName
    }

    init {
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}