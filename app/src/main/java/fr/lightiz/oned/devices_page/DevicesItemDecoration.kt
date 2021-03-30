package fr.lightiz.oned.devices_page

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DevicesItemDecoration  : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = 20
    }
}