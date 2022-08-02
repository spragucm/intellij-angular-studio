package com.tcubedstudios.angularstudio.projectview.folding

import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.components.service
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.FileStatusListener
import com.intellij.openapi.vcs.FileStatusManager
import com.intellij.openapi.vcs.changes.ignore.cache.PatternCache
import com.intellij.openapi.vcs.changes.ignore.lang.Syntax
import com.tcubedstudios.angularstudio.projectview.folding.settings.IProjectViewFoldingSettingsListener
import com.tcubedstudios.angularstudio.projectview.folding.settings.ProjectViewFoldingSettings
import com.tcubedstudios.angularstudio.projectview.folding.settings.ProjectViewFoldingSettingsState

class FoldingTreeStructureProvider(project: Project): TreeStructureProvider {

    private val settings = project.service<ProjectViewFoldingSettings>()
    private val state: ProjectViewFoldingSettingsState
        get() {
            return settings.state
        }
    private val patternCache = PatternCache.getInstance(project)
    private val projectView = ProjectView.getInstance(project)
//    private val state
    //TODO - CHRIS - Preview state must be getting used in the settings project tree
//        get() {
//            previewState ?: settings //TODO
//        }

    init {
        project.messageBus
            .connect(project)
            .subscribe(IProjectViewFoldingSettingsListener.TOPIC, object: IProjectViewFoldingSettingsListener {
                override fun settingsChanged(settings: ProjectViewFoldingSettings) {
                    projectView.refresh()
                }
            })

        FileStatusManager.getInstance(project).addFileStatusListener(object: FileStatusListener {
            override fun fileStatusesChanged() {
                if (settings.state.hideIgnoredFiles) {
                    projectView.refresh()
                }
            }
        }, project)
    }

    override fun modify (
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        viewSettings: ViewSettings?
    ): Collection<AbstractTreeNode<*>> {
        val project = parent.project ?: return children

        return when {
            !state.foldingEnabled -> children
            //TODO - CHRIS - I commented this out so the patterns would work recursively, not just at top node
//            parent !is PsiDirectoryNode -> children
//            !isModule(parent, project) -> children
            else -> children.match().toSet().let { matched ->
                when {
                    state.hideAllGroups -> children - matched
                    state.hideEmptyGroups && matched.isEmpty() -> children
                    else -> children - matched + FoldingProjectViewNode(project, viewSettings, matched)
                }
            }
        }
    }

    private fun isModule(node: PsiDirectoryNode, project: Project) = node.virtualFile?.let {
        ModuleUtil.findModuleForFile(it, project)?.guessModuleDir() == it
    } ?: false

    private fun MutableCollection<AbstractTreeNode<*>>.match() = this
        .filter {
            when(it) {
                is PsiDirectoryNode -> state.foldDirectories
                is PsiFileNode -> true
                else -> false
            }
        }
        .filter {
            when(it) {
                is ProjectViewNode -> it.virtualFile?.name ?: it.name
                else -> it.name
            }.caseInsensitive().let { name ->
                state.patterns
                    .caseInsensitive()
                    .split(' ')
                    .any { pattern -> patternCache?.createPattern(pattern, Syntax.GLOB)?.matcher(name)?.matches() ?: false }
            }.or(state.hideIgnoredFiles and(it.fileStatus.equals(FileStatus.IGNORED)))
        }

    @OptIn(ExperimentalStdlibApi::class)
    private fun String?.caseInsensitive() = when {
        this == null -> ""
        state.caseInsensitive -> lowercase()
        else -> this
    }
}