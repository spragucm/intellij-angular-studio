package com.tcubedstudios.angularstudio.angular.projectview.scope

import com.intellij.psi.search.scope.packageSet.CustomScopesProviderEx
import com.intellij.psi.search.scope.packageSet.NamedScope

class AngularStudioScopesProvider : CustomScopesProviderEx() {
    override fun getCustomScopes(): MutableList<NamedScope> {
        return mutableListOf(AngularProjectViewNamedScope)
    }
}