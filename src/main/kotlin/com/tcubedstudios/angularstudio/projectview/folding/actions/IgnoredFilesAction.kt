package com.tcubedstudios.angularstudio.projectview.folding.actions

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ToggleOptionAction
import com.intellij.openapi.components.service
import com.tcubedstudios.angularstudio.projectview.folding.settings.ProjectViewFoldingSettings
import java.util.function.Function

class IgnoredFilesAction: ToggleOptionAction(Function {
    object: Option {
        private val settings = it.project?.service<ProjectViewFoldingSettings>()

        override fun isSelected() = settings?.state?.foldingEnabled ?: false

        override fun setSelected(selected: Boolean) {
            val updated = selected != isSelected
            settings?.state?.hideIgnoredFiles = selected

            if (updated) {
                it.project?.let { project ->
                    val view = ProjectView.getInstance(project)
                    view.currentProjectViewPane?.updateFromRoot(true)
                }
            }
        }
    }
})