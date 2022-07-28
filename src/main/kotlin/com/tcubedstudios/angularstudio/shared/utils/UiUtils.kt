package com.tcubedstudios.angularstudio.shared.utils

import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.layout.ComponentPredicate
import com.intellij.ui.layout.toBinding
import com.tcubedstudios.angularstudio.MyBundle
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
    text: String,
    property: KMutableProperty0<Boolean>,
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
    return row(text) {
        checkBox("").apply {
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

        helpTitle?.let { title -> helpDescription?.let { description ->
            ContextHelpLabel.create(
                MyBundle.message(title),
                MyBundle.message(description)
            )
        }}
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