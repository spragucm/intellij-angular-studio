package com.tcubedstudios.angularstudio.angular.projectview.scope

import com.intellij.openapi.extensions.ProjectExtensionPointName
import com.intellij.psi.search.scope.packageSet.CustomScopesFilter
import com.intellij.psi.search.scope.packageSet.NamedScope
import com.intellij.util.containers.ContainerUtil

fun interface IAngularStudioScopesProvider {

    companion object {
        //NOTE: The extension point name doesn't have to match the interface name
        val ANGULAR_STUDIO_SCOPES_PROVIDER = ProjectExtensionPointName<IAngularStudioScopesProvider>("angularStudioScopesProvider")
    }

    fun getNamedScopes(): List<NamedScope>

    val filteredScopes: List<NamedScope>
        get() {
            return getNamedScopes()
        }
        /*get() = ContainerUtil.filter(getNamedScopes()) { scope: NamedScope? ->
            for (filter in CustomScopesFilter.EP_NAME.iterable) {
                if (filter.excludeScope(scope!!)) {
                    return@filter false
                }
            }
            true
        }*/
}