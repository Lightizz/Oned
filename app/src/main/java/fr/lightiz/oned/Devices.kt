package fr.lightiz.oned

import android.content.Context
import android.widget.ImageView
import android.widget.TextView

enum class Devices(id: Int, logo:ImageView) {
    ANDROID(0, MainActivity().devicesLogoToIDConverter(0)),
    WINDOWS10(1, MainActivity().devicesLogoToIDConverter(1)),
    LINUX(2, MainActivity().devicesLogoToIDConverter(2)),
    MAC(3, MainActivity().devicesLogoToIDConverter(3)),
    IPHONE(4, MainActivity().devicesLogoToIDConverter(4)),
    ;
}