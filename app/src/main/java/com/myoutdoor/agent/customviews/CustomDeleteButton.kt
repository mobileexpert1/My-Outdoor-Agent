package com.myoutdoor.agent.customviews

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.myoutdoor.agent.R // Make sure you reference the correct package for your resources

class CustomDeleteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    // Colors for different states
    private val disabledColor = ContextCompat.getColor(context, R.color.light_red) // Use a custom color in res/values/colors.xml
    private val enabledColor = ContextCompat.getColor(context, R.color.royal_red)  // Solid red color defined in res/values/colors.xml

    init {
        // Apply the initial state
        updateButtonState(isEnabled)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateButtonState(enabled)
    }

    // Function to update the background color and shape based on the state
    private fun updateButtonState(isEnabled: Boolean) {
        val color = if (isEnabled) enabledColor else disabledColor
        setButtonBackground(color)
    }

    // Helper function to create a drawable with a rounded rectangle shape
    private fun setButtonBackground(color: Int) {
        val shapeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f  // Set corner radius for rounded corners
            setColor(color)
        }
        background = shapeDrawable
        setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Ensure text color is always white
    }
}
