package com.tcubedstudios.angularstudio.angular.generation.settings

import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener
import com.intellij.ui.tabs.impl.SingleHeightTabs
import com.tcubedstudios.angularstudio.shared.utils.textRow
import javax.swing.JComponent

//TODO - CHRIS - Would a BoundSearchableConfigurable be better?
class NgGenerateSettingsConfigurable(private val project: Project): Configurable { //BoundConfigurable("Angular Studio Settings :)") {

    val propertyGraph = PropertyGraph()
    var text1 = propertyGraph.property("Text 1")
    var text2 = propertyGraph.property("Text 2")

    override fun getDisplayName(): String = "Ng Generate Settings"

    override fun getHelpTopic(): String = "Configure Ng Generate Settings"

    // TODO - CHRIS - revisit when this should and shouldn't be true
    override fun isModified(): Boolean = false

    //TODO - CHRIS - this should be handled somewhere smart
    private val uiDisposable = Disposer.newDisposable()

    override fun createComponent(): JComponent {
        val splitter1 = OnePixelSplitter()
        splitter1.firstComponent = createFirstComponent()
        splitter1.secondComponent = createSecondComponent()
        val splitter1Tab = TabInfo(splitter1).apply {
            text = "Schemas"
            sideComponent = splitter1
        }

        val splitter2 = OnePixelSplitter()
        splitter2.firstComponent = createFirstComponent()
        splitter2.secondComponent = createSecondComponent()
        val splitter2Tab = TabInfo(splitter2).apply {
            text = "NgGenerate Settings"
            sideComponent = splitter2
        }

        val splitter3 = OnePixelSplitter()
        splitter3.firstComponent = createFirstComponent()
        splitter3.secondComponent = createSecondComponent()
        val splitter3Tab = TabInfo(splitter3).apply {
            text = "Bulk Generator"
            sideComponent = splitter3
        }

        val tabs = object : SingleHeightTabs(project, uiDisposable) {
            override fun adjust(each: TabInfo?) = Unit
        }.apply {
            addTab(splitter1Tab)
            addTab(splitter2Tab)
            addTab(splitter3Tab)
        }


        val listener = object : TabsListener {
            override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
                println("Changed tab from ${oldSelection?.text} to ${newSelection?.text}")
            }
        }
        tabs.addListener(listener)

        return tabs
        /*return panel {
            row {
                Cell
                splitter1
            }
        }*/
    }

    private fun createFirstComponent(): JComponent {
        val splitter = OnePixelSplitter()
        splitter.firstComponent = createProjectViewComponent()
        splitter.secondComponent = panel {
            textRow("Second Comp project:${project.name}", text2)
        }
        return splitter
    }

    private fun createSecondComponent(): JComponent {
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

    override fun apply() {
        
    }

    override fun reset() {

    }

    //TODO - CHRIS - it would be nice to have this as a generic tree component that
    //can have items selected and then the selected item passed to some other logic
    //for processing
    private fun createProjectViewComponent(): JComponent {
        return ProjectViewPane(project).createComponent()
    }
}