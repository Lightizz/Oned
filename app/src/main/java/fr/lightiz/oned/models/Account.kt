package fr.lightiz.oned.models

class Account(
        var name:String = "",
        var email:String = "",
        var password:String = "",
        var age: String = "",
        var fullHourSystem:Boolean = true
//  True = format 24h | False = format 12h
)