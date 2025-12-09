package com.shapes.app.drawing

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Safe placeholder so the project can compile
class DrawingViewAndroid {

    @Composable
    fun Render(modifier: Modifier = Modifier) {
        // Empty on purpose (placeholder)
    }

    fun getBitmap(): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    fun clear() {
        // Do nothing for now
    }
}