package com.alex.carlauncher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CustomSpeedometerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var speed = 0
    private val maxSpeed = 240
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun updateSpeed(newSpeed: Int) {
        speed = newSpeed.coerceIn(0, maxSpeed)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height * 0.75f
        val radius = width * 0.45f

        // Fondo del velocímetro
        paint.color = Color.parseColor("#1E1E1E")
        canvas.drawArc(RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius), 120f, 300f, true, paint)

        // Arco de velocidad
        paint.color = Color.parseColor("#00FF88")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 30f
        canvas.drawArc(RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius), 120f, (speed.toFloat() / maxSpeed) * 300f, false, paint)

        // Aguja
        needlePaint.color = Color.RED
        needlePaint.strokeWidth = 12f
        val angle = 120f + (speed.toFloat() / maxSpeed) * 300f
        val rad = Math.toRadians(angle.toDouble())
        val needleLength = radius * 0.85f
        val x = centerX + (needleLength * cos(rad)).toFloat()
        val y = centerY + (needleLength * sin(rad)).toFloat()
        canvas.drawLine(centerX, centerY, x, y, needlePaint)

        // Centro
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, 20f, paint)

        // Texto velocidad
        paint.textSize = 60f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("$speed", centerX, centerY + 140f, paint)
        paint.textSize = 24f
        canvas.drawText("km/h", centerX, centerY + 180f, paint)
    }
}