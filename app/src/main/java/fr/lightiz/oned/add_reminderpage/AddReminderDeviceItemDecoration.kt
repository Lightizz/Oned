package fr.lightiz.oned.add_reminderpage

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AddReminderDeviceItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        outRect.bottom = 15
    }
}