package com.tcubedstudios.angularstudio.angular.navigation.hoverbuttons

import com.tcubedstudios.angularstudio.ide.navigation.actions.OpenFileWithExtensionAction
import icons.JavaScriptPsiIcons.FileTypes.TypeScriptFile

class OpenAngularCodeAction: OpenFileWithExtensionAction("Open Component Code 2", TypeScriptFile) {
    override val extensions = mapOf("ts" to TypeScriptFile)
}