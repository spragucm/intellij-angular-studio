package com.tcubedstudios.angularstudio.projectview.folding.settings

import com.intellij.util.messages.Topic
import java.util.EventListener

@FunctionalInterface
interface IProjectViewFoldingSettingsListener: EventListener {

    companion object {
        @Topic.ProjectLevel
        val TOPIC = Topic(IProjectViewFoldingSettingsListener::class.java)
    }

    fun settingsChanged(settings: ProjectViewFoldingSettings)
}