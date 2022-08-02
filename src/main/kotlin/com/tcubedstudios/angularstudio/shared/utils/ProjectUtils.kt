package com.tcubedstudios.angularstudio.shared.utils

import com.intellij.ide.projectView.ProjectView

fun ProjectView.refresh(restoreExpandedPaths: Boolean = true) = currentProjectViewPane?.updateFromRoot(restoreExpandedPaths)