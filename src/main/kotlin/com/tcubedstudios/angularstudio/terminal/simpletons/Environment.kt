package com.tcubedstudios.angularstudio.terminal.simpletons

import com.tcubedstudios.angularstudio.terminal.utils.OperatingSystemUtils

object Environment {
    val osName: String = System.getProperty("os.name")
    val osVersion: String = System.getProperty("os.version")
    val gui: String = System.getProperty("sun.desktop")
    val operatingSystem: OperatingSystem = OperatingSystemUtils.toOperatingSystem(osName)
}