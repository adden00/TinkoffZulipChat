package com.addenisov00.courseproject.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.addenisov00.courseproject.R
import com.addenisov00.courseproject.common.Constants
import com.addenisov00.courseproject.presentation.messenger.models.MessageItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val imAvatar: CardView
    private val tvName: TextView
    private val tvMessage: TextView
    private val cardMessage: LinearLayout
    private val reactionBox: ReactionFlexBox
    private val imPicture: ImageView

    fun getReactionBox() = reactionBox


    init {
        inflate(context, R.layout.message_view_group_content, this)
        imAvatar = findViewById(R.id.imAvatar)
        tvName = findViewById(R.id.tvMessageUserName)
        tvMessage = findViewById(R.id.tvMessageContent)
        reactionBox = findViewById(R.id.reactionBox)
        cardMessage = findViewById(R.id.cardMessage)
        imPicture = findViewById(R.id.imPicture)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var xSum = 0
        var ySum = 0

        measureChildWithMargins(imAvatar, widthMeasureSpec, xSum, heightMeasureSpec, ySum)
        xSum += imAvatar.measuredWidth + imAvatar.marginLeft + imAvatar.marginRight
        ySum += imAvatar.measuredHeight + imAvatar.marginTop + imAvatar.marginBottom

        measureChildWithMargins(cardMessage, widthMeasureSpec, xSum, heightMeasureSpec, ySum)
        ySum += cardMessage.measuredHeight + cardMessage.marginTop + cardMessage.marginBottom

        measureChildWithMargins(reactionBox, widthMeasureSpec, xSum, heightMeasureSpec, ySum)

        val totalWidth = paddingLeft + paddingRight + imAvatar.measuredWidth + maxOf(
            cardMessage.measuredWidth + cardMessage.marginLeft + cardMessage.marginRight,
            reactionBox.measuredWidth + cardMessage.marginLeft + reactionBox.marginLeft + reactionBox.marginRight
        ) + imAvatar.marginLeft + imAvatar.marginRight

        val totalHeight = paddingTop + paddingBottom + maxOf(
            imAvatar.measuredHeight + imAvatar.marginTop + imAvatar.marginBottom,
            cardMessage.measuredHeight + reactionBox.measuredHeight
                    + cardMessage.marginTop + cardMessage.marginBottom + reactionBox.marginTop + reactionBox.marginBottom
        )
        setMeasuredDimension(totalWidth, totalHeight)
    }


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var offsetX = paddingLeft + imAvatar.marginLeft
        var offsetY = paddingTop + imAvatar.marginTop

        imAvatar.layout(
            offsetX,
            offsetY,
            offsetX + imAvatar.measuredWidth,
            offsetY + imAvatar.measuredHeight
        )

        offsetX += imAvatar.measuredWidth + imAvatar.marginRight + cardMessage.marginLeft
        offsetY = paddingTop + cardMessage.marginTop

        cardMessage.layout(
            offsetX,
            offsetY,
            offsetX + cardMessage.measuredWidth,
            offsetY + cardMessage.measuredHeight
        )

        offsetY += cardMessage.measuredHeight + cardMessage.marginBottom + reactionBox.marginTop

        reactionBox.layout(
            offsetX,
            offsetY,
            offsetX + reactionBox.measuredWidth,
            offsetY + reactionBox.measuredHeight
        )
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

    fun setMessage(item: MessageItem) {
        tvMessage.text = item.content.text

        if (item.content.imageUrl != "") {
            loadPicture(item.content.imageUrl)
        } else
            hidePicture()
        tvName.text = item.userName
        reactionBox.setReactions(item.reactions)
        cardMessage.background =
            if (item.isMy) ContextCompat.getDrawable(
                context,
                R.drawable.bg_my_message
            ) else ContextCompat.getDrawable(
                context, R.drawable.bg_other_message
            )
        tvName.visibility = if (item.isMy) View.GONE else View.VISIBLE
        Glide.with(context).load(item.imageUrl).centerCrop()
            .into(findViewById(R.id.message_avatar_image))
        imAvatar.visibility = if (item.isMy) View.GONE else View.VISIBLE
        (this.layoutParams as FrameLayout.LayoutParams).gravity =
            if (item.isMy) Gravity.END else Gravity.START
        (this.layoutParams as FrameLayout.LayoutParams).marginEnd =
            if (item.isMy) 10f.dp(context) else 50f.dp(context)
    }

    private fun hidePicture() {
        imPicture.visibility = View.GONE

    }

    private fun loadPicture(url: String) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 60f
        circularProgressDrawable.setTint(context.getColor(R.color.color_primary))
        circularProgressDrawable.start()
        imPicture.visibility = View.VISIBLE
        val authorizedUrl = GlideUrl(
            url,
            LazyHeaders.Builder().addHeader(
                "Authorization",
                Constants.authorization
            ).build()
        )
        Glide.with(context).load(authorizedUrl).centerCrop()
            .placeholder(circularProgressDrawable)
            .into(imPicture)
    }
}