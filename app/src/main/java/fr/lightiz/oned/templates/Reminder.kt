package fr.lightiz.oned.templates

import java.util.*
import kotlin.collections.ArrayList

class Reminder(
        var title:String = "Title",
        var reminderDate:Date = Date(),
        var deviceList: ArrayList<Device> = arrayListOf(),
        var isEnabled:Boolean = true
)
