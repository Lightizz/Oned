package fr.lightiz.oned.models

class Device (
    val deviceKey:String = "000000000000000000000000000000000000000000001", // 35 Integer
    var name:String = "Device_name",
    var os:Int = 0

        /*
      ANDROID (0),
      WINDOWS10 (1),
      LINUX (2),
      MAC (3),
      IPHONE (4),
         */
)