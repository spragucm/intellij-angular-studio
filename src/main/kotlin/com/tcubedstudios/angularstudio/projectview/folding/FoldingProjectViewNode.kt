package com.tcubedstudios.angularstudio.projectview.folding

import com.intellij.icons.AllIcons.General.CollapseComponent
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.SimpleTextAttributes

class FoldingProjectViewNode(
    project: Project,
    settings: ViewSettings?,
    private val children: Set<AbstractTreeNode<*>>
): ProjectViewNode<String>(project, "ProjectView Folding", settings) {

    override fun getChildren() = children

    override fun contains(file: VirtualFile) = children.firstOrNull {
        it is ProjectViewNode && it.virtualFile == file
    } != null
    override fun update(presentationData: PresentationData) {
        presentationData.apply {
            val text = "Folded files:${children.size}"
            val toolTip = children.mapNotNull { it.name }.joinToString(", ")
            val textAttributes = SimpleTextAttributes.GRAY_SMALL_ATTRIBUTES
            addText(ColoredFragment(text, toolTip, textAttributes))
            setIcon(CollapseComponent)
        }
    }
}