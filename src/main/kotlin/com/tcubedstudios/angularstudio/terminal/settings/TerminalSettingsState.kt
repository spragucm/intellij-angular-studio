package com.tcubedstudios.angularstudio.terminal.settings

import java.util.*

class TerminalSettingsState {

    var favoriteTerminal: String = ""
        get() {
            return when {
                field.isNotEmpty() -> field
                else -> System.getenv("FAVORITE_TERMINAL") ?: ""// to be compatible with old versions (prior to v0.2)
            }
        }

    override fun equals(other: Any?) = when {
        other is TerminalSettingsState && favoriteTerminal == other.favoriteTerminal -> true
        else -> false
    }

    override fun hashCode() = Objects.hash(favoriteTerminal)
}