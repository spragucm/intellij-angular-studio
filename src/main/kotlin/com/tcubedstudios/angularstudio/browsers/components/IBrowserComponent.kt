package com.tcubedstudios.angularstudio.browsers.components

import com.tcubedstudios.angularstudio.browsers.simpletons.BrowserComponentType
import javax.swing.JComponent

interface IBrowserComponent {
    val type: BrowserComponentType
    val component: JComponent

    var onUrlChangedCallback: ((String) -> Unit)?
    var onProgressChangedCallback: ((Double) -> Unit)?

    val canGoBack: Boolean
    val canGoForward: Boolean

    fun load(url: String)
    fun back()
    fun forward()
    fun openDevTools()
    fun executeScript(script: String)
}