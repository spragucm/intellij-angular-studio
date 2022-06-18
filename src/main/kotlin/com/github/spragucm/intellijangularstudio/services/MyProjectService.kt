package com.github.spragucm.intellijangularstudio.services

import com.intellij.openapi.project.Project
import com.github.spragucm.intellijangularstudio.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
