package com.addenisov00.courseproject.presentation.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem

class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        reactionItem: ReactionItem
    ) : this(context, attrs, defStyleAttr, defStyleRes) {
        reactionToDraw = reactionItem
    }

    private var reactionToDraw: ReactionItem
    fun getReaction() = reactionToDraw

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f.sp(context)
        color = context.getColor(R.color.white)
    }
    private val textBounds = Rect()

    init {
        reactionToDraw = run {
            var reaction = ""
            var count = 0
            context.withStyledAttributes(attrs, R.styleable.ReactionCustomView) {
                reaction = getString(R.styleable.ReactionCustomView_reaction) ?: ""
                count = getString(R.styleable.ReactionCustomView_count)?.toInt() ?: 0
            }
            ReactionItem(reaction, count, false, reaction, ReactionType.REACTION)
        }
        isSelected = reactionToDraw.isSelected
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        paint.getTextBounds(
            reactionToDraw.toString(),
            0,
            reactionToDraw.toString().length,
            textBounds
        )
        val textWidth = textBounds.width()
        val textHeight = textBounds.height()
        val measureWidth = resolveSize(textWidth + paddingLeft + paddingRight, widthMeasureSpec)
        val measureHeight = resolveSize(textHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        val centerY = height / 2 - textBounds.exactCenterY()
        canvas?.drawText(reactionToDraw.toString(), paddingLeft.toFloat(), centerY, paint)
    }
}
