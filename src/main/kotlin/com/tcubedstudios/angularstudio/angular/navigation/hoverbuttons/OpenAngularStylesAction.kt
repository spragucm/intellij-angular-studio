package com.tcubedstudios.angularstudio.angular.navigation.hoverbuttons

import com.intellij.icons.AllIcons.FileTypes.Css
import com.tcubedstudios.angularstudio.cli.navigation.OpenFileWithExtensionAction
import icons.SassIcons.Sass

class OpenAngularStylesAction: OpenFileWithExtensionAction("Open Component Styles 2", Css) {
    override val extensions = mapOf(
        "css" to Css,
        "scss" to Sass
    )
}