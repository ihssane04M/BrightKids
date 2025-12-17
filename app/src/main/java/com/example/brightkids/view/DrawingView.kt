package com.example.brightkids.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#FF6B9D")
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val guidePaint = Paint().apply {
        color = Color.parseColor("#CCCCCC") // Light gray for guide
        strokeWidth = 3f
        style = Paint.Style.STROKE
        isAntiAlias = true
        alpha = 150 // Semi-transparent
    }

    private val textPaint = Paint().apply {
        color = Color.parseColor("#BBBBBB") // Light gray
        textSize = 220f // Initial size, will be scaled dynamically
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        strokeWidth = 12f // Will also be scaled with view size
        alpha = 150 // More visible for better tracing
    }

    private var letter: String = ""
    private val path = Path()
    private lateinit var bitmap: Bitmap
    private lateinit var drawCanvas: Canvas
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    
    // Score tracking
    private var touchPointCount: Int = 0
    private var startTime: Long = 0
    private var isDrawing: Boolean = false
    var onDrawingComplete: ((Int, Long) -> Unit)? = null // (score, duration)
    

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(bitmap)
        // Draw guide letter on bitmap
        drawGuideLetter()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isDrawing) {
                    isDrawing = true
                    startTime = System.currentTimeMillis()
                    touchPointCount = 0
                }
                path.moveTo(x, y)
                touchPointCount++
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                touchPointCount++
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas.drawPath(path, paint)
                path.reset()
                touchPointCount++
                
                // Calculate score after a short delay to check if drawing is complete
                val currentPointCount = touchPointCount
                postDelayed({
                    // Check if drawing is still active (user might continue)
                    if (!isDrawing) return@postDelayed
                    
                    val duration = System.currentTimeMillis() - startTime
                    val score = calculateScore(currentPointCount, duration)
                    onDrawingComplete?.invoke(score, duration)
                    isDrawing = false
                    touchPointCount = 0
                }, 1500) // Wait 1.5 seconds to see if user continues drawing
            }
        }
        invalidate()
        return true
    }
    
    private fun calculateScore(points: Int, duration: Long): Int {
        // Simple scoring: more points = better, but not too fast or too slow
        val durationSeconds = duration / 1000f
        if (durationSeconds < 1) return 0 // Too fast, probably accidental
        
        // Score based on points and reasonable duration (2-10 seconds is ideal)
        var score = (points / 10).coerceIn(0, 100)
        
        // Bonus for good duration (2-8 seconds)
        if (durationSeconds in 2f..8f) {
            score = (score * 1.2f).toInt().coerceIn(0, 100)
        } else if (durationSeconds > 15f) {
            score = (score * 0.8f).toInt() // Penalty for too slow
        }
        
        return score.coerceIn(0, 100)
    }
    
    fun getStarsFromScore(score: Int): Int {
        return when {
            score >= 70 -> 3
            score >= 40 -> 2
            score >= 20 -> 1
            else -> 0
        }
    }

    fun setLetter(letter: String) {
        this.letter = letter
        if (::bitmap.isInitialized) {
            drawGuideLetter()
            invalidate()
        }
    }

    private fun drawGuideLetter() {
        if (!::bitmap.isInitialized || letter.isEmpty()) return
        
        // Clear the bitmap first
        bitmap.eraseColor(Color.TRANSPARENT)
        drawCanvas = Canvas(bitmap)

        // Dynamically scale letter size based on view dimensions
        val minDim = min(viewWidth, viewHeight).toFloat()
        // Letter will take about 65% of the smallest dimension
        textPaint.textSize = minDim * 0.65f
        textPaint.strokeWidth = minDim * 0.03f
        
        // Calculate position to center the letter
        val textBounds = Rect()
        textPaint.getTextBounds(letter, 0, letter.length, textBounds)
        
        val x = viewWidth / 2f
        val y = (viewHeight / 2f) + (textBounds.height() / 2f)
        
        // Draw the guide letter with bold stroke for better visibility
        // Draw stroke first (outline) for bold effect
        textPaint.style = Paint.Style.STROKE
        drawCanvas.drawText(letter, x, y, textPaint)
        
        // Draw fill on top
        textPaint.style = Paint.Style.FILL
        drawCanvas.drawText(letter, x, y, textPaint)
    }

    fun clear() {
        if (::bitmap.isInitialized) {
            bitmap.eraseColor(Color.TRANSPARENT)
            drawGuideLetter() // Redraw guide after clearing
            path.reset()
            isDrawing = false
            touchPointCount = 0
            invalidate()
        }
    }

    fun setDrawColor(color: Int) {
        paint.color = color
    }
    
    fun startTraceAnimation(letter: String) {
        // Clear the drawing to start fresh
        clear()
    }
}
