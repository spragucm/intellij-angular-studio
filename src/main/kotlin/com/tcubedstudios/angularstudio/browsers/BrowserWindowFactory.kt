package com.tcubedstudios.angularstudio.browsers


import java.lang.reflect.Method
import java.util.Objects
import javax.swing.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ReflectionUtil.getDeclaredMethod
import com.intellij.util.ui.JBUI
import javax.swing.SwingConstants.CENTER
import javax.swing.SwingConstants.TOP

class BrowserWindowFactory: ToolWindowFactory {

    companion object {
        private const val JBCEF_APP = "com.intellij.ui.jcef.JBCefApp"
        private const val IS_SUPPORTED = "isSupported"
        private const val JCEF_NOT_SUPPORTED = "JCEF is not supported in running IDE"
    }

    private val isJcefSupported: Boolean
        get() {
            try {
                val method = getDeclaredMethod(Class.forName(JBCEF_APP), IS_SUPPORTED) ?: return false
                return method.invoke(null) as? Boolean ?: return false
            } catch(e: Exception) {
                return false
            }
        }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(getBrowser(), "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun getBrowser(): JComponent {
        try {
            if (isJcefSupported) {
                //Class.forName("com.tcubedstudios.angularstudio.browsers.JcefBrowserComponent")//TODO - CHRIS - why is this using reflection?
                return BrowserWindow(JcefBrowserComponent())
            }
        } catch (e: Exception) {}
        
        return JLabel(JCEF_NOT_SUPPORTED).apply {
            horizontalAlignment = CENTER
            verticalAlignment = TOP
            border = JBUI.Borders.emptyTop(10)  
        }
    }
}