package fr.lightiz.oned

class Account (
    val id:String = "",
    var email:String = "example@email.com",
    var password:String = "password",
    var remindersList: List<Reminder>,
    var fullHourSystem:Boolean = true
//  True = format 24h | False = format 12h
)