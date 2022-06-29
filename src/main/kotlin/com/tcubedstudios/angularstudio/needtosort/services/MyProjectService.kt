package com.tcubedstudios.angularstudio.needtosort.services

import com.intellij.openapi.project.Project
import com.tcubedstudios.angularstudio.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
