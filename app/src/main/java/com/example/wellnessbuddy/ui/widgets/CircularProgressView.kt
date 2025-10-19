package com.example.wellnessbuddy.ui.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16f
        color = Color.parseColor("#DBEAFE")
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16f
        color = Color.parseColor("#3B82F6")
        strokeCap = Paint.Cap.ROUND
    }


    private var progress: Float = 0f
    private var animatedProgress: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private var rectF: RectF = RectF()

    private var animator: ValueAnimator? = null

    init {
        // Set default size if not specified
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(160.dpToPx(), 160.dpToPx())
        }
    }

    fun setProgress(progress: Float, animate: Boolean = true) {
        val newProgress = progress.coerceIn(0f, 100f)
        if (this.progress == newProgress) return

        this.progress = newProgress

        if (animate) {
            animateToProgress(newProgress)
        } else {
            animatedProgress = newProgress
            invalidate()
        }
    }

    private fun animateToProgress(targetProgress: Float) {
        animator?.cancel()
        
        animator = ValueAnimator.ofFloat(animatedProgress, targetProgress).apply {
            duration = 500
            addUpdateListener { animation ->
                animatedProgress = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        centerX = w / 2f
        centerY = h / 2f
        radius = minOf(w, h) / 2f - backgroundPaint.strokeWidth / 2f
        
        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        
        // Draw progress arc (circular progress stroke only)
        val sweepAngle = (animatedProgress / 100f) * 360f
        canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint)
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}
