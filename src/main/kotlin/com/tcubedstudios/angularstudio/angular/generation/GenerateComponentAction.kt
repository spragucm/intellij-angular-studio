package com.tcubedstudios.angularstudio.angular.generation

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys.FILE_EDITOR
import com.intellij.openapi.project.DumbAwareAction
import org.angular2.cli.AngularCliUtil

class GenerateComponentAction: DumbAwareAction() {

    // References:
    //      Anuglar Plugin for IntelliJ
    //      - AngularCliGenerateACtion
    //      - AngularCliGenerateAction
    override fun actionPerformed(event: AnActionEvent) {
        //Determine the directory
        //fetch all of the schematic options for generating a command from preferences
        //Call the command in the angular angular terminal

        // TODO - CHRIS - consider the following ...
        //only show for angular projects
        //should this only show when in a project?
        //for example, if there are multiple projects, then where to add a component?
        //if there is one project, then maybe place the component in the given project, top most folder
        //if the
        //

        val project = event.project ?: return
        val file = event.getData(VIRTUAL_FILE) ?: return
        val cli = AngularCliUtil.findAngularCliFolder(project, file) ?: return
        val editor = event.getData(FILE_EDITOR) ?: return

    }

    override fun update(event: AnActionEvent) {
        super.update(event)
        val project = event.project
        val file = event.getData(VIRTUAL_FILE)
        val presentation = event.presentation
        presentation.isEnabledAndVisible = true//project != null && file != null && AngularCliUtil.findAngularCliFolder(project, file) != null
    }
}