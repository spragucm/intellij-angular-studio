package com.tcubedstudios.angularstudio.angular.navigation.hoverbuttons

import com.intellij.icons.AllIcons.FileTypes.Html
import com.tcubedstudios.angularstudio.ide.navigation.actions.OpenFileWithExtensionAction

class OpenAngularTemplateAction: OpenFileWithExtensionAction("Open Component Template 2", Html) {
    override val extensions = mapOf("html" to Html)
}