package com.tcubedstudios.angularstudio.angular.navigation.projectview

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class NamedFileNode(
    project: Project,
    psiFile: PsiFile,
    viewSettings: ViewSettings,
    val nodeName: String,
    val originalNode: AbstractTreeNode<*>
): PsiFileNode(project, psiFile, viewSettings) {

    override fun update(presentationData: PresentationData) {
        super.update(presentationData)
        presentationData.presentableText = nodeName
    }

}