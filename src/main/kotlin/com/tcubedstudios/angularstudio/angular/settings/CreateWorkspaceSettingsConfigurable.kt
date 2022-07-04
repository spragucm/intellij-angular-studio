package com.tcubedstudios.angularstudio.angular.settings

import javax.swing.JComponent
import javax.swing.JPanel
import com.intellij.diff.impl.DiffSettingsHolder.DiffSettings
import com.intellij.diff.tools.util.base.TextDiffSettingsHolder
import com.intellij.diff.tools.util.base.TextDiffSettingsHolder.TextDiffSettings
import com.intellij.openapi.diff.DiffBundle.message
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.*
import com.tcubedstudios.angularstudio.shared.intellijcompat.openapi.observable.properties.property
import com.intellij.ui.dsl.builder.Cell
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.tcubedstudios.angularstudio.shared.intellijcompat.openapi.observable.util.toUiPathProperty

class CreateWorkspaceSettingsConfigurable: Configurable {

    companion object {
        private const val COLUMN1 = "column1"
        private const val COLUMN2 = "column2"
    }

    private val propertyGraph = PropertyGraph()

    private val nameProperty = propertyGraph.property("Some name property")
    private var projectName by nameProperty

    private val compilerOutputProperty = propertyGraph.property("")
    private val projectCompilerOutput by compilerOutputProperty

    private var sourceRootPath: String = ""
    private lateinit var pathField: TextFieldWithBrowseButton

    override fun getDisplayName(): String  = "New Angular Workspace Settings"

    override fun getHelpTopic(): String = "Configure New Angular Workspace Settings"

    override fun createComponent(): JComponent? {
        return panel {
            group("Required") {
//                label("Workspace Path")
                row("Workspace") {
                    textField()
                        .bindText(nameProperty)
//                        .widthGroup(COLUMN1)
                        .columns(28)
                    textFieldWithBrowseButton(
                        null,
                        null,
                        FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                        null
                    )
                        .horizontalAlign(HorizontalAlign.FILL)
                        .bindText(compilerOutputProperty)//.toUiPathProperty()

                }
                row("Something in a row 2") {
                    checkBox("Some check")
                }
            }
            collapsibleGroup("Another Group") {
                row {
                    label("Some Label")
                        .bold()
                        .comment("Let's see some comment")
                    checkBox("Checkbox 3")
                    checkBox("Checkbox 4")
                }.bottomGap(BottomGap.SMALL)
            }
        }
    }

    override fun isModified(): Boolean= false

    override fun apply() {

    }

}