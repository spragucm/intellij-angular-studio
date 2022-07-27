package com.tcubedstudios.angularstudio.angular.projectview.scope

import com.intellij.ide.IdeBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.search.scope.packageSet.AbstractPackageSet
import com.intellij.psi.search.scope.packageSet.NamedScope
import com.intellij.psi.search.scope.packageSet.NamedScopesHolder
import icons.AngularJSIcons

object AngularProjectViewNamedScope: NamedScope(
    "Angular Studio",
    AngularJSIcons.Angular2,
    object : AbstractPackageSet(IdeBundle.message("angular.studio.scope.title")) {
        override fun contains(file: VirtualFile, project: Project, holder: NamedScopesHolder?): Boolean {
            return true
        }

        override fun contains(file: PsiFile, holder: NamedScopesHolder?): Boolean {
            return true
        }
    }
)