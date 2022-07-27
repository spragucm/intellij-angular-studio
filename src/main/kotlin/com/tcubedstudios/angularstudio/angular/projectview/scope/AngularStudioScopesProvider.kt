package com.tcubedstudios.angularstudio.angular.projectview.scope

import com.intellij.psi.search.scope.packageSet.CustomScopesProviderEx
import com.intellij.psi.search.scope.packageSet.NamedScope

class AngularStudioScopesProvider : CustomScopesProviderEx() {
    //    open fun getCustomScope(name: String): NamedScope? {
//        val predefinedScopes = filteredScopes
//        return findPredefinedScope(name, predefinedScopes)
//    }
//
//    fun isVetoed(scope: NamedScope?, place: ScopePlace?): Boolean {
//        return false
//    }
//
//    enum class ScopePlace {
//        SETTING, ACTION
//    }
//
//    private object AllScopeHolder {
//        private const val TEXT = FilePatternPackageSet.SCOPE_FILE + ":*//*"
//        val allScope = NamedScope("All",
//            { AnalysisBundle.message("all.scope.name") }, AllIcons.Ide.LocalScope, object : AbstractPackageSet(TEXT, 0) {
//                override fun contains(file: VirtualFile, project: Project, holder: NamedScopesHolder?): Boolean {
//                    return true
//                }
//            })
//            get() = AllScopeHolder.field
//    }
//
//    companion object {
//        fun findPredefinedScope(scopeId: String, predefinedScopes: List<NamedScope>): NamedScope? {
//            for (scope in predefinedScopes) {
//                if (scopeId == scope.scopeId) return scope
//            }
//            return null
//        }
//
//        fun filterNoSettingsScopes(project: Project?, scopes: MutableList<NamedScope?>) {
//            val iterator = scopes.iterator()
//            while (iterator.hasNext()) {
//                val scope = iterator.next()
//                for (provider in CustomScopesProvider.CUSTOM_SCOPES_PROVIDER.getExtensions(
//                    project!!
//                )) {
//                    if (provider is CustomScopesProviderEx && provider.isVetoed(scope, ScopePlace.SETTING)) {
//                        iterator.remove()
//                        break
//                    }
//                }
//            }
//        }
//    }
    override fun getCustomScopes(): MutableList<NamedScope> {
        return mutableListOf(AngularProjectViewNamedScope)
    }
}