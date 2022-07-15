package com.tcubedstudios.angularstudio.angular.generation.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class GenerateComponentDialogAction: DumbAwareAction() {

    override fun actionPerformed(event: AnActionEvent) {

    }

    override fun update(event: AnActionEvent) {
        super.update(event)
        //A new action is required, even if the gen call is identical except for a diff argument being passed,
        //because the logic for displaying the action in a menu needs to be based off of a preference
    }
}