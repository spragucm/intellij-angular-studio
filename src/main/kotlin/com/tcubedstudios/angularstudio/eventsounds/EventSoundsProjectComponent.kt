package com.tcubedstudios.angularstudio.eventsounds

import com.intellij.openapi.components.ProjectComponent

class EventSoundsProjectComponent: ProjectComponent {

    val DEFAULT_BREAKPOINT_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/breakpoint.wav"
    val DEFAULT_BUILD_ERROR_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/buildError.wav"
    val DEFAULT_BUILD_SUCCESS_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/buildSuccess.wav"
    val DEFAULT_BUILD_WARNING_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/buildWarning.wav"
    val DEFAULT_EXCEPTION_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/exception.wav"
    val DEFAULT_FIND_FAILED_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/findFailed.wav"
    val DEFAULT_FIND_SUCCESS_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/findSuccess.wav"
    val DEFAULT_PROCESS_STOPPED_SOUND = "/com/tcubedstudios/angularstudio/resources/sounds/processStopped.wav"

    //TODO - CHRIS - Import EventSoundsPlugin while migrating away from ProjectComponent

}