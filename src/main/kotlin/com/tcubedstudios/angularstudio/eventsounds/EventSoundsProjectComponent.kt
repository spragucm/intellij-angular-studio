package com.tcubedstudios.angularstudio.eventsounds

import com.intellij.openapi.components.ProjectComponent

class EventSoundsProjectComponent: ProjectComponent {

    val DEFAULT_BREAKPOINT_SOUND = "/audio/notifications/breakpoint.wav"
    val DEFAULT_BUILD_ERROR_SOUND = "/audio/notifications/buildError.wav"
    val DEFAULT_BUILD_SUCCESS_SOUND = "/audio/notifications/buildSuccess.wav"
    val DEFAULT_BUILD_WARNING_SOUND = "/audio/notifications/buildWarning.wav"
    val DEFAULT_EXCEPTION_SOUND = "/audio/notifications/exception.wav"
    val DEFAULT_FIND_FAILED_SOUND = "/audio/notifications/findFailed.wav"
    val DEFAULT_FIND_SUCCESS_SOUND = "/audio/notifications/findSuccess.wav"
    val DEFAULT_PROCESS_STOPPED_SOUND = "/audio/notifications/processStopped.wav"

    //TODO - CHRIS - Import EventSoundsPlugin while migrating away from ProjectComponent

}