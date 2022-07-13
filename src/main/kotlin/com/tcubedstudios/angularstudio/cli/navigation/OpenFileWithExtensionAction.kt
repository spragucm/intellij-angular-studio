package com.tcubedstudios.angularstudio.cli.navigation

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
//import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.tcubedstudios.angularstudio.shared.util.FileUtils.fileManager
import com.tcubedstudios.angularstudio.shared.util.FileUtils.getNextSiblingWithExtension
import com.tcubedstudios.angularstudio.shared.util.FileUtils.hasMatchingExtension
import com.tcubedstudios.angularstudio.shared.util.WindowUtils.hasEditor
import com.tcubedstudios.angularstudio.shared.util.WindowUtils.hasFile
//import com.intellij.psi.PsiFile
//import icons.JavaScriptPsiIcons
//import icons.SassIcons
import javax.swing.Icon
import javax.swing.JSplitPane.HORIZONTAL_SPLIT

//val bla = TypeScriptFile
abstract class OpenFileWithExtensionAction(title: String, icon: Icon): AnAction({ title }, icon) {

//    val bla = AllIcons.Actions.Checked_selected
//    val tsIcon = JavaScriptLanguageIcons.Icons.Typescript.TypeScript_Compiler

    abstract val extensions: Map<String, Icon>//TODO - CHRIS - these should go in settings so that it's easy to enable
                                            //file extensions that we most often switch through and disable the others

    override fun update(event: AnActionEvent) {
        val file = event.getData(PSI_FILE)
        val nextSibling = file?.getNextSiblingWithExtension(extensions)

        event.presentation.isEnabledAndVisible = file != null && nextSibling != null && !file.hasMatchingExtension(extensions)
        event.presentation.text = "Open ${ nextSibling?.name } 2"
        event.presentation.icon = nextSibling?.name?.substringAfterLast(".")?.let {
            extensions[it.lowercase()]
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(PSI_FILE) ?: return
        val editor = event.getData(EDITOR) ?: return
        val nextSiblingFile = file.getNextSiblingWithExtension(extensions)?.virtualFile ?: return

        val fileEditorManager = file.fileManager()

        // TODO - CHRIS - Settings should be "open in new window" and "toggle in place"
        //                This could also be a right click left click situation, where clicking toggles in place and right click
        //                says open in new window or next tab
        // Find existing window where the next sibling file is opened and which is not the window where the action was performed
        var window = fileEditorManager.splitters.windows.firstOrNull {
            !it.hasEditor(editor) && it.hasFile(nextSiblingFile)
        }

        if (window != null) {
            // If it exists, open the respective editor and focus it
            window.setSelectedComposite(window.allComposites.first { it.file == nextSiblingFile }, true)
        } else {
            // Otherwise, split the current window and open the desired file
            window = (fileEditorManager.splitters.windows.firstOrNull { it.hasEditor(editor) } ?: fileEditorManager.currentWindow)
            window?.split(HORIZONTAL_SPLIT, false, nextSiblingFile, true)
        }
    }
}