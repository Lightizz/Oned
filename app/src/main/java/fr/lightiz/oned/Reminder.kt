package fr.lightiz.oned

import java.util.*

class Reminder(
    var title:String = "Title",
    var reminderDate:Date = Date(),
    var devicesListIDs: List<Devices> = Devices.values().toList(),
    var isEnabled:Boolean = true
)
