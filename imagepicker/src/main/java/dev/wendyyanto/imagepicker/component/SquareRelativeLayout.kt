package dev.wendyyanto.imagepicker.component

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class SquareRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}