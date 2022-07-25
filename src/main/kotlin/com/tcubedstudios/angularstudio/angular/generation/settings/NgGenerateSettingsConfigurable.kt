package com.tcubedstudios.angularstudio.angular.generation.settings

import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.options.Configurable
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.panel
import com.tcubedstudios.angularstudio.shared.utils.textRow
import javax.swing.JComponent
import kotlin.reflect.KMutableProperty0

class NgGenerateSettingsConfigurable: Configurable {

    val propertyGraph = PropertyGraph()
    var text1 = propertyGraph.property("Text 1")
    var text2 = propertyGraph.property("Text 2")

    override fun getDisplayName(): String = "Ng Generate Settings"

    override fun getHelpTopic(): String = "Configure Ng Generate Settings"

    // TODO - CHRIS - revisit when this should and shouldn't be true
    override fun isModified(): Boolean = false

    override fun createComponent(): JComponent? {
        val splitter = OnePixelSplitter()
        splitter.firstComponent = createFirstComponent()
        splitter.secondComponent = createSecondComponent()
        return splitter
    }

    private fun createFirstComponent(): JComponent {
        val items: JBList<String> = JBList()

//        val table = TableView()

//        val toolbar = ToolbarDecorator.createDecorator(items)
//            .disableUpDownActions()
//        return toolbar.createPanel()
        return panel {
//            toolbarDecorator
            textRow("First Comp", text1)
        }
    }

    private fun createSecondComponent(): JComponent {
        return panel {
            textRow("Second Comp", text2)
        }
    }

    override fun apply() {
        
    }

    override fun reset() {

    }
}