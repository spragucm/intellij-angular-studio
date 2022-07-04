package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.ide.IdeBundle
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.TitledSeparator
import com.intellij.ui.dsl.builder.impl.CollapsibleTitledSeparator
import javax.swing.*

class NewWorkspaceSettingsForm {
    var settingsPanel: JPanel? = null
    var workspaceNameLabel: JLabel? = null
    var workspaceName: JTextField? = null
    var workspaceTemplateLabel: JLabel? = null
    var templateWorkspacePackageJsonPathLabel: JLabel? = null
    var workspaceTemplate: JSpinner? = null
    var templateWorkspacePackageJsonPath: JSpinner? = null
    var replaceEntirePackageJson: JCheckBox? = null
    var createWorkspaceInWorking: JCheckBox? = null
    var collection: JTextField? = null
    var commitCheckBox: JCheckBox? = null
    var isMonoRepo: JCheckBox? = null
    var defaults: JCheckBox? = null
    var textField2: JTextField? = null
    var dryRun: JCheckBox? = null
    var force: JCheckBox? = null
    var inlineStyle: JCheckBox? = null
    var inlineTemplate: JCheckBox? = null
    var directoryNameLabel: JLabel? = null
    var interactive: JCheckBox? = null
    var minimal: JCheckBox? = null
    var newProjectRootLabel: JLabel? = null
    var textField3: JTextField? = null
    var packageManagerLabel: JLabel? = null
    var comboBox1: JComboBox<*>? = null
    var prefixLabel: JLabel? = null
    var textField4: JTextField? = null
    var checkBox1: JCheckBox? = null
    var checkBox2: JCheckBox? = null
    var checkBox3: JCheckBox? = null
    var checkBox4: JCheckBox? = null
    var checkBox5: JCheckBox? = null
    var checkBox6: JCheckBox? = null
    var checkBox7: JCheckBox? = null
    var checkBox8: JCheckBox? = null
    var checkBox9: JCheckBox? = null
    var checkBox10: JCheckBox? = null
    var OIVeyCheckBox: JCheckBox? = null
    var alsoOiVeyCheckBox: JCheckBox? = null
    var comboBox2: JComboBox<*>? = null
    var comboBox3: JComboBox<*>? = null
    var workspacePathTextView: JTextField? = null
    var workspacePathLabel: JLabel? = null
    var workspacePathButton: JButton? = null
    var fileInput: TextFieldWithBrowseButton? = null
    var workspacePathView: TextFieldWithBrowseButton? = null

    // Update all fields at once using the save state object
    var newWorkspaceSettingsState: NewWorkspaceSettingsState = NewWorkspaceSettingsState()

    init {
        workspacePathView?.addBrowseFolderListener(
            IdeBundle.message("chooser.title.select.user.data.directory"), IdeBundle
            .message("chooser.description.specifies.user.data.directory"),
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
    }
}