package com.addenisov00.courseproject.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.presentation.messenger.models.ReactionItem


class ReactionFlexBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val reactions = mutableListOf<ReactionView>()

    init {
        inflate(context, R.layout.reaction_viewgroup_content, this)
    }

    private fun addReaction(reaction: ReactionItem) {
        val reactionView = ReactionView(context, reactionItem = reaction).apply {
            layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(
                5f.dp(context),
                5f.dp(context),
                8f.dp(context),
                7f.dp(context)
            )
            setBackgroundResource(R.drawable.bg_reaction_view)

            (layoutParams as MarginLayoutParams).setMargins(
                5f.dp(context),
                3f.dp(context),
                5f.dp(context),
                3f.dp(context)
            )

            if (reaction.reactionType == ReactionType.ADD_NEW_REACTION)
                updatePadding(
                    left = paddingLeft + 5F.dp(context),
                    right = paddingRight + 3F.dp(context)
                )
        }

        reactionView.isSelected = reaction.isSelected
        reactions.add(reactionView)
        addView(reactionView)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        var totalWidth = paddingLeft + paddingRight
        var totalHeight =
            paddingTop + paddingBottom

        var maxWidth = 0
        var maxLineHeight = 0
        reactions.forEach { reactionView ->
            measureChildWithMargins(reactionView, widthMeasureSpec, 0, heightMeasureSpec, 0)

            if (maxLineHeight < reactionView.measuredHeight + reactionView.marginTop + reactionView.marginBottom)
                maxLineHeight =
                    reactionView.measuredHeight + reactionView.marginTop + reactionView.marginBottom

            if (totalHeight == paddingTop + paddingBottom)
                totalHeight += maxLineHeight

            if (totalWidth > Constants.MESSAGE_WIDTH.dp(context)) {
                totalWidth = paddingLeft + paddingRight
                totalHeight += maxLineHeight
            }
            totalWidth += reactionView.measuredWidth + reactionView.marginLeft + reactionView.marginRight


            if (maxWidth < totalWidth) {
                maxWidth = totalWidth
            }

        }
        setMeasuredDimension(maxWidth, totalHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var offsetX = paddingLeft
        if (reactions.isNotEmpty())
            offsetX += reactions[0].marginLeft
        var offsetY = paddingTop
        var maxHeight = 0

        reactions.forEach { reactionView ->
            if (maxHeight < reactionView.measuredHeight + reactionView.marginTop)
                maxHeight = reactionView.measuredHeight + reactionView.marginTop
            if (offsetX >= Constants.MESSAGE_WIDTH.dp(context)) {
                offsetX = paddingLeft + reactionView.marginLeft
                offsetY += maxHeight
            }
            reactionView.layout(
                offsetX,
                offsetY + reactionView.marginTop,
                offsetX + reactionView.measuredWidth,
                offsetY + maxHeight
            )
            offsetX += reactionView.measuredWidth + reactionView.marginLeft + reactionView.marginRight
            if (maxHeight < reactionView.measuredHeight + reactionView.marginTop)
                maxHeight = reactionView.measuredHeight + reactionView.marginTop
        }
    }


    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }


    fun setReactions(reactionsList: List<ReactionItem>) {
        reactions.clear()
        removeAllViews()
        if (reactionsList.isNotEmpty()) {
            reactionsList.forEach {
                addReaction(it)
            }
            addPlus()
        }
    }

    private fun addPlus() {
        addReaction(
            ReactionItem(
                "+",
                0,
                false,
                "",
                ReactionType.ADD_NEW_REACTION
            )
        )
    }
}