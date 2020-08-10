package com.omdb.movies.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.omdb.movies.R


class TextViewWithSizeableDrawable : AppCompatTextView {

    var drawableSize:Float = 150f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        applyDrawableSize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        applyDrawableSize(attrs)
    }

    private fun applyDrawableSize(attrs: AttributeSet?){
        if(attrs!=null) {
            try {
                val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithSizeableDrawable)
                drawableSize = a.getDimensionPixelSize( R.styleable.TextViewWithSizeableDrawable_drawableSize, drawableSize.toInt()).toFloat()
                a.recycle()
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
        setCompoundDrawablesWithIntrinsicBounds(resize(compoundDrawables[0]), resize(compoundDrawables[1]), resize(compoundDrawables[2]), resize(compoundDrawables[3]))
    }

    private fun resize(drawable: Drawable?): Drawable? {
        drawable?.setBounds(0,0, drawableSize.toInt(), drawableSize.toInt())
        return drawable
    }

    override fun setCompoundDrawablesWithIntrinsicBounds( left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable? ) {
        listOf(left, top, right, bottom).forEach { resize(it) }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }

    override fun setCompoundDrawablesRelativeWithIntrinsicBounds(start: Drawable?,  top: Drawable?, end: Drawable?, bottom: Drawable? ) {
        listOf(start, top, end, bottom).forEach { resize(it) }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
    }
    override fun setCompoundDrawables( left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?  ) {
        listOf(left, top, right, bottom).forEach { resize(it) }
        super.setCompoundDrawables(left, top, right, bottom)
    }
}