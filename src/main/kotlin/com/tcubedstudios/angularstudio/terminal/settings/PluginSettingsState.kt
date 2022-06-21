package com.tcubedstudios.angularstudio.terminal.settings

import java.util.*

class PluginSettingsState {

    var favoriteTerminal: String = ""
        get() {
            return when {
                favoriteTerminal.isNotEmpty() -> field
                else -> System.getenv("FAVORITE_TERMINAL")// to be compatible with old versions (prior to v0.2)
            }
        }

    override fun equals(other: Any?) = when {
        other is PluginSettingsState && favoriteTerminal == other.favoriteTerminal -> true
        else -> false
    }

    override fun hashCode() = Objects.hash(favoriteTerminal)
}