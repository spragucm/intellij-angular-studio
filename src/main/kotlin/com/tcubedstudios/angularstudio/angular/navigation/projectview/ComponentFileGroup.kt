package com.tcubedstudios.angularstudio.angular.navigation.projectview

import com.intellij.icons.AllIcons.Nodes.Class
import com.intellij.icons.AllIcons.Nodes.Static
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.tcubedstudios.angularstudio.shared.Constants.ICON_TYPE_SERVICE

class ComponentFileGroup(
    project: Project,
    baseFile: PsiFile,
    viewSettings: ViewSettings,
    private val groupName: String,
    private val iconType: String
): ProjectViewNode<PsiFile>(project, baseFile, viewSettings) {

    private val children: MutableList<AbstractTreeNode<*>> = mutableListOf()

    val childCount: Int
        get() = children.size

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> = children

    override fun update(presentationData: PresentationData) {
        presentationData.presentableText = groupName

        val icon = if (iconType == ICON_TYPE_SERVICE) Static else Class
        presentationData.setIcon(icon)
    }

    override fun contains(file: VirtualFile): Boolean {
        children.forEach { childNode ->
            val treeNode = childNode as? ProjectViewNode ?: return@forEach
            if (treeNode.contains(file)) {
                return true
            }
        }
        return false
    }

    fun addChild(node: AbstractTreeNode<*>, extension: String) {
        (node as? PsiFileNode)?.let {
            children.add(NamedFileNode(node.project, node.value, node.settings, ".$extension", node))
        }
    }

    fun getOriginalFirstChild(): AbstractTreeNode<*>? {
        return when(val first = children.firstOrNull()) {
            is NamedFileNode -> first.originalNode
            else -> first
        }
    }
}