package com.tcubedstudios.angularstudio.shared

import com.intellij.collaboration.util.ProgressIndicatorsProvider
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.UIUtil

/*row {
           // need some pushx/grow component to test label cell grow policy if there is cell with several components
           scrollPane(JTextArea())
       }*/

/*link(JavaRefactoringBundle.message("extract.method.link.label.more.options"), null) { showDialogAction(null) }
    .applyToComponent { isFocusable = true }
comment(KeymapUtil.getFirstKeyboardShortcutText(ACTION_EXTRACT_METHOD))*/


/*return panel {
    row {
        Cell
        splitter1
    }
}*/

/*
val indicatorsProvider = ProgressIndicatorsProvider().also {
    Disposer.register(disposable!!, it)
}*/

/*
Extension Points
    <extensionPoints>
        <extensionPoint name="someExtensionPointName" interface="com.tcubedstudios.angularstudio.angular.projectview.scope.IAngularStudioScopesProvider" area="IDEA_PROJECT" dynamic="true"/>
    </extensionPoints>
    <extensions>
        <someExtensionPointName implementation="the class package"/>
    </extensions>

Open Files
    - look at OpenFilesScope


UI Elements
    - Create invisible box of specified size
        Box.createRigidArea(JBUI.size(5, 0))

    - Add help icon with popup (doesn't seem to work) wit kotlin dsl
        add(ContextHelpLabel.create("errrrrrr"))

    - applyToComponent vs apply: apply is native kotlin for applying to the same object it's called on.
        I think applyToComponent is the same idea, but it's called on a Cell containing the component that
        is the actual taret of the apply call

    - Row dsl class has a ton of useful builder methods




 */
