package com.tcubedstudios.angularstudio.browsers.toolwindows

import com.tcubedstudios.angularstudio.browsers.components.ControlButton
import com.tcubedstudios.angularstudio.browsers.components.IBrowserComponent
import com.tcubedstudios.angularstudio.shared.simpletons.Constants.BLANK_PAGE_URL
import com.tcubedstudios.angularstudio.shared.simpletons.Constants.DEV_TOOLS_TEXT
import com.tcubedstudios.angularstudio.shared.simpletons.Constants.GO_TEXT
import com.tcubedstudios.angularstudio.shared.libs.shopobot.URL
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.JTextField
import javax.swing.SwingUtilities

class BrowserWindow(
    private val browserComponent: IBrowserComponent
): JPanel(), KeyListener {

    private lateinit var goButton: ControlButton
    private lateinit var devToolsButton: ControlButton
    private lateinit var progressBar: JProgressBar
    private lateinit var urlTextField: JTextField

    init {
        initView()
        initEvent()
    }

    private fun initView() {
        layout = BorderLayout()
        add(topControls(), NORTH)
        add(centerContent(), CENTER)
    }

    private fun topControls(): JPanel {
        return JPanel().apply {
            layout = GridBagLayout()

//            if (browserComponent.type == BrowserComponentType.JAVAFX) {
                devToolsButton = ControlButton(DEV_TOOLS_TEXT)
                devToolsButton.addActionListener { browserComponent.openDevTools() }// addActionListenter{ browserComponent.openDevTools()}
                devToolsButton.toolTipText = DEV_TOOLS_TEXT
                add(devToolsButton)
//            }

            //TODO - CHRIS - this likely can be a TextRow
            urlTextField = JTextField()
            // txtUrl.setText("http://127.0.0.1:8080")
            urlTextField.addKeyListener(this@BrowserWindow)
            add(urlTextField, object : GridBagConstraints() {
                init {
                    weightx = 1.0
                    fill = HORIZONTAL
                }
            })

            goButton = ControlButton(GO_TEXT)
            goButton.addActionListener { load(urlTextField.text) }
            add(goButton)
        }
    }

    private fun centerContent(): JPanel {
        return JPanel().apply {
            layout = BorderLayout()

            progressBar = JProgressBar(0, 100)
            progressBar.isVisible = false
            progressBar.preferredSize = Dimension(0, 5)
            add(progressBar, NORTH)
            
            browserComponent.onProgressChangedCallback = { progress ->
                swingInvokeLater {
                    progressBar.isVisible = progress != 1.0 && progress != 0.0
                    progressBar.value = (progress * 100.0).toInt()
                }
            }

            browserComponent.onUrlChangedCallback = { url ->
                swingInvokeLater {
                    urlTextField.text = if (url.isBlank() || url.trim() == BLANK_PAGE_URL) {
                         ""
                    } else {
                        url
                    }

                    if (urlTextField.text.isNotEmpty() && !urlTextField.isFocusOwner) {
                        urlTextField.caretPosition = 0
                    }
                }
            }
            add(browserComponent.component, CENTER)
        }
    }

    private fun initEvent() {

    }

    private fun load(url: String) {
        try {
            browserComponent.load(URL.get(url)?.toJavaURL().toString())
        } catch(e: Exception) {
            browserComponent.load(BLANK_PAGE_URL)
        }
    }

    private fun swingInvokeLater(runnable: Runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run()
        } else {
            SwingUtilities.invokeLater(runnable)
        }
    }

    override fun keyTyped(keyEvent: KeyEvent) {}

    override fun keyReleased(keyEvent: KeyEvent) {}

    override fun keyPressed(keyEvent: KeyEvent) {
        if (keyEvent.keyCode == KeyEvent.VK_ENTER) {
            load(urlTextField.text)
        }
    }
}