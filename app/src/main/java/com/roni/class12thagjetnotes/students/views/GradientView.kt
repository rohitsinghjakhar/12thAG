package com.roni.class12thagjetnotes.students.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class GradientView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientColors: IntArray = intArrayOf(0xFF667eea.toInt(), 0xFF764ba2.toInt())

    fun setGradientColors(colors: IntArray) {
        gradientColors = colors
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGradient()
    }

    private fun updateGradient() {
        if (width > 0 && height > 0) {
            val gradient = LinearGradient(
                0f, 0f,
                width.toFloat(), height.toFloat(),
                gradientColors[0], gradientColors[1],
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGradient()
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}