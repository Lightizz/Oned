package fr.lightiz.oned.home_page

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ReminderItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = 20
    }
}