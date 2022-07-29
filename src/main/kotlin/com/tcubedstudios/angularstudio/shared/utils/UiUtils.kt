package com.tcubedstudios.angularstudio.shared.utils

import com.intellij.application.options.CodeStyleConfigurableWrapper
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.icons.AllIcons
import com.intellij.ide.DataManager
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.options.ex.Settings
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.layout.ComponentPredicate
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import kotlin.reflect.KMutableProperty0

//TODO - CHRIS - investigate the following
//checkboxRowsWithBigComponents()

/*fun Panel.checkBoxRow(
    text: String,
    property: GraphProperty<Boolean>,
    onModified: ((GraphProperty<Boolean>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT
): Row {
    return row(text) {
        val checkBox = checkBox("")
        checkBox.bindSelected(property)

        onModified?.let {
            checkBox.onIsModified { onModified.invoke(property) }
        }

        checkBox.horizontalAlign(horizontalAlign)
    }
}*/

fun Panel.checkBoxRow(
    property: KMutableProperty0<Boolean>,
    labelText: String? = null,
    checkBoxText: String? = null,
    comment: String? = null,
    enableIf: ComponentPredicate? = null,
    mnemonic: Char? = null,
    commentMaxLineLength: Int = -1,
    onModified: ((KMutableProperty0<Boolean>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
    componentPredicate: ((ComponentPredicate) -> Unit)? = null,
    helpTitle: String? = null,
    helpDescription: String? = null
): Row {
    val text = checkBoxText ?: ""

    //The following is necessary because passing empty into row results in IDE warnings as well as significantly more left padding
    return when {
        labelText != null -> {
            row(labelText) {
                checkBoxExt(text, property, comment, enableIf, mnemonic, commentMaxLineLength, onModified, horizontalAlign, componentPredicate)
                helpIconWithPopup(helpTitle, helpDescription)
            }
        }
        else -> {
            row {
                checkBoxExt(text, property, comment, enableIf, mnemonic, commentMaxLineLength, onModified, horizontalAlign, componentPredicate)
                helpIconWithPopup(helpTitle, helpDescription)
            }
        }
    }
}

fun Row.checkBoxExt(
    text: String,
    property: KMutableProperty0<Boolean>,
    comment: String? = null,
    enableIf: ComponentPredicate? = null,
    mnemonic: Char? = null,
    commentMaxLineLength: Int = -1,
    onModified: ((KMutableProperty0<Boolean>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
    componentPredicate: ((ComponentPredicate) -> Unit)? = null
): Cell<JBCheckBox> {
    return checkBox(text).apply {
        //TODO - CHRIS - this is from a migration and might not be useful anymore
        //with kotlin dsl components
        //withSelectedBinding(property.toBinding())
        bindSelected(property)
        onModified?.let { onIsModified { onModified.invoke(property) } }
        horizontalAlign(horizontalAlign)
        comment(comment, commentMaxLineLength)
        mnemonic?.let { applyToComponent { setMnemonic(it) } }
        componentPredicate?.invoke(selected)
        enableIf?.let { enabledIf(it) }
    }
}

fun Panel.textRow(
    text: String,
    property: GraphProperty<String>,
    onModified: ((GraphProperty<String>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val textField = textField()
        textField.bindText(property)

        onModified?.let {
            textField.onIsModified { onModified.invoke(property) }
        }

        textField.horizontalAlign(horizontalAlign)
    }
}

fun Panel.textRow(
    text: String,
    property: KMutableProperty0<String>,
    onModified: ((KMutableProperty0<String>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val textField = textField()
        textField.bindText(property)

        onModified?.let {
            textField.onIsModified { onModified.invoke(property) }
        }

        textField.horizontalAlign(horizontalAlign)
    }
}

fun Panel.expandableTextRow(
    text: String,
    property: KMutableProperty0<String>,
    onModified: ((KMutableProperty0<String>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row {
//        expandableTextField()
//            .label("Expandable text")
//            .applyToComponent {
//                text = "some tset alksdjflak sjdfla jskdjfalksdjfas jdkfja"
//                /*document.addDocumentListener(object: DocumentAdapter() {
//                    override fun textChanged(e: DocumentEvent) {
//                        settings.ADDITIONAL_TAGS = text.trim()
//                    }
//                })*/
//            }
    }

//    /**/return row(text) {
//        val textField = textField()
//        textField.bindText(property)
//
//        onModified?.let {
//            textField.onIsModified { onModified.invoke(property) }
//        }
//
//        textField.horizontalAlign(horizontalAlign)
//    }
}

fun <T> Panel.comboBoxRow(
    text: String,
    property: GraphProperty<T>,
    items: Collection<T>,
    onModified: ((GraphProperty<T>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val comboBox = comboBox(items)
        comboBox.bindItem(property)

        onModified?.let {
            comboBox.onIsModified { onModified.invoke(property) }
        }

        comboBox.horizontalAlign(horizontalAlign)
    }
}

fun <E: Enum<E>> Panel.comboBoxRow(
    text: String,
    property: GraphProperty<E>,
    enumClass: Class<E>,
    onModified: ((GraphProperty<E>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val items = enumClass.enumConstants.toList()
        val comboBox = comboBox(items)
        comboBox.bindItem(property)

        onModified?.let {
            comboBox.onIsModified { onModified.invoke(property) }
        }

        comboBox.horizontalAlign(horizontalAlign)
    }
}

fun <T> Panel.comboBoxRow(
    text: String,
    property: KMutableProperty0<T>,
    items: Collection<T>,
    onModified: ((KMutableProperty0<T>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val comboBox = comboBox(items)
        comboBox.bindItem(property)

        onModified?.let {
            comboBox.onIsModified { onModified.invoke(property) }
        }

        comboBox.horizontalAlign(horizontalAlign)
    }
}

fun <E: Enum<E>> Panel.comboBoxRow(
    text: String,
    property: KMutableProperty0<E>,
    enumClass: Class<E>,
    onModified: ((KMutableProperty0<E>) -> Boolean)? = null,
    horizontalAlign: HorizontalAlign = HorizontalAlign.FILL
): Row {
    return row(text) {
        val items = enumClass.enumConstants.toList()
        val comboBox = comboBox(items)
        comboBox.bindItem(property)

        onModified?.let {
            comboBox.onIsModified { onModified.invoke(property) }
        }

        comboBox.horizontalAlign(horizontalAlign)
    }
}

// Popups show above the icon whereas tooltips show below the icons
fun Row.helpIconWithPopup (
    title: String? = null,
    description: String? = null
): Cell<JLabel>? {
    // WARNING: When first attempting to use contextHelp, the icon was red; the color changed after a theme
    // change, but I thought the color was set based on the title provided (e.g. warning). It's not. It's
    // whatever the theme sets it to be.

    // Also the following appear to have the same results, but indeed have different types:
    //      cell(ContextHelpLabel.create(helpTitle, helpDescription))       Cell<ContextHelpLabel>
    //      contextHelp(helpTitle, helpDescription)                         Cell<JLabel>
    return when {
        title != null && description != null -> contextHelp(description, title)
        description != null -> contextHelp(description)
        else -> null
    }
}

fun Row.helpIconWithPopupAndLink(
    description: String,
    linkText: String,
    onLinkClicked: () -> Unit,
    title: String? = null,
    linkIsExternal: Boolean = false,//Setting this to true will add a link arrow to the right
    rightGap: RightGap? = RightGap.SMALL
): Cell<JLabel> {
    val cell = cell(
        ContextHelpLabel.createWithLink(title, description, linkText, linkIsExternal) {
            onLinkClicked.invoke()
    })
    rightGap?.let { cell.gap(it) }
    return cell
}
//TODO
/*fun Row.helpIconWithPopupAndBrowserLink(): Cell<JLabel> {

}
//TODO
fun Row.helpIconWithPopupAndInternalLink(): Cell<JLabel> {

}*/

// Tooltips show below the icon whereas popups show above the icons
fun Row.iconWithTooltip(text: String, icon: Icon = AllIcons.General.ProjectConfigurable): Cell<JLabel> {
    return icon(icon).applyToComponent { toolTipText = text }
}


fun Panel.refreshEditorBackgroundHighlightingAndAutoImporters() {
    onApply {
        for (project in ProjectManager.getInstance().openProjects) {
            DaemonCodeAnalyzer.getInstance(project).restart()
        }
    }
}

// TODO - CHRIS - Useful snippet from JavaAutoImportOptions that we need to investigate further
private fun openConfigurableTab(dataContextOwner: JComponent, configurableId: String, tabName: String) {
    Settings.KEY.getData(DataManager.getInstance().getDataContext(dataContextOwner))?.let { settings ->
        (settings.find(configurableId) as? CodeStyleConfigurableWrapper)?.let { configurable ->
            settings.select(configurable).doWhenDone {
                configurable.selectTab(ApplicationBundle.message(tabName))
            }
        }
    }
}

