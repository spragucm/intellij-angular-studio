package com.tcubedstudios.angularstudio.browsers.components

import java.awt.Dimension
import javax.swing.JButton

class ControlButton(text: String): JButton(text) {
    init {
        maximumSize = Dimension(40, 25)
        minimumSize = Dimension(40, 25)
        preferredSize = Dimension(40, 25)
    }
}