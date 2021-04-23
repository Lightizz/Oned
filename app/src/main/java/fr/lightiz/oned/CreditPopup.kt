package fr.lightiz.oned

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ImageView

class CreditPopup(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_credit)

        val close = findViewById<ImageView>(R.id.popup_credit_close)
        close.setOnClickListener {
            hide()
            dismiss()
        }
    }
}
