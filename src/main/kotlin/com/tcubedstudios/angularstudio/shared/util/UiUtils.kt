package com.tcubedstudios.angularstudio.shared.util

import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign

fun Panel.checkBoxRow(text: String, onClick: (() -> Unit)? = null, horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT): Row {
    return row(text) {
        checkBox("")
            .horizontalAlign(horizontalAlign)
//            .onClick(onClick)
    }
}

fun Panel.textRow(text: String): Row {
    return row(text) {
        textField()
//            .bindText(workspaceNameProperty)
            .horizontalAlign(HorizontalAlign.FILL)
    }
}