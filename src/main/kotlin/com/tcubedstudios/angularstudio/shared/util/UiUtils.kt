package com.tcubedstudios.angularstudio.shared.util

import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import kotlin.reflect.KMutableProperty0

fun Panel.checkBoxRow(text: String, onClick: (() -> Unit)? = null, horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT): Row {
    return row(text) {
        checkBox("")
            .horizontalAlign(horizontalAlign)
//            .onClick(onClick)
    }
}

fun Panel.textRow(text: String, property: GraphProperty<String>, onModified: ((GraphProperty<String>) -> Boolean)? = null): Row {
    return row(text) {
        textField()
            .bindText(property)
            .onIsModified {
                onModified?.invoke(property) ?: true
            }
            .horizontalAlign(HorizontalAlign.FILL)
    }
}

fun Panel.textRow(text: String, property: KMutableProperty0<String>, onModified: ((KMutableProperty0<String>) -> Boolean)? = null): Row {
    return row(text) {
        textField()
            .bindText(property)
            .onIsModified {
                onModified?.invoke(property) ?: true
            }
            .horizontalAlign(HorizontalAlign.FILL)
    }
}