package com.tcubedstudios.angularstudio.angular.navigation.projectview

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.util.treeView.AbstractTreeNode
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE
import com.intellij.ide.projectView.ViewSettings
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

class AngularTreeStructureProvider: TreeStructureProvider {
    companion object {
        // TODO - CHRIS - there are now multiple approaches to the same problem of determining what file names to use
        // when grouping angular components. These should be consolidated into a single pattern if possible
        private val namePattern = Pattern.compile(
            "(.*)\\.(component|service|pipe|guard|directive|actions|effects|reducer|selectors|state|resolver)\\.(css|sass|scss|stylus|styl|less|html|svg|haml|pug|spec\\.ts|ts)",
            CASE_INSENSITIVE)
    }

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: Collection<AbstractTreeNode<*>>,
        viewSettings: ViewSettings
    ): Collection<AbstractTreeNode<*>> {
        if (parent.value !is PsiDirectory) return children

        val treeNodes = mutableListOf<AbstractTreeNode<*>>()
        val componentFileGroups = mutableMapOf<String, ComponentFileGroup>()

        children.forEach { child ->
            val psiFile = child.value as? PsiFile
            val fileName = psiFile?.name
            val matcher = if(fileName != null) namePattern.matcher(fileName) else null

            if (psiFile == null || fileName == null || matcher == null || !matcher.find()) {
                treeNodes.add(child)
            } else {
                val prefix = matcher.group(1)
                val classType = matcher.group(2)
                val extension = matcher.group(3)
                val groupKey = String.format("%s.%s", prefix, classType)

                var group = componentFileGroups[groupKey]
                if (group == null) {
                    group = ComponentFileGroup(child.project, psiFile, viewSettings, groupKey, classType)
                    componentFileGroups[groupKey] = group
                    treeNodes.add(group)
                }
                group.addChild(child, extension)
            }
        }

        // Undo grouping that has only one child
        treeNodes.forEachIndexed { index, treeNode ->
            if (treeNode is ComponentFileGroup && treeNode.childCount <= 1) {
                treeNode.getOriginalFirstChild()?.let { treeNodes.set(index, it) }
            }
        }

        return treeNodes
    }

    override fun getData(selected: Collection<AbstractTreeNode<*>>, dataId: String): Any? {
        return super.getData(selected, dataId)
    }
}