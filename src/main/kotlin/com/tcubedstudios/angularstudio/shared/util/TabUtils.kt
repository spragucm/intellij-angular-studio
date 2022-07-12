package com.tcubedstudios.angularstudio.shared.util

import com.tcubedstudios.angularstudio.shared.Direction
import com.tcubedstudios.angularstudio.shared.Direction.LEFT

object TabUtils {
    internal infix fun Int.isToThe(direction: Direction) = IsToThe(this, direction)
}

internal class IsToThe(private val index: Int, private val direction: Direction) {
    infix fun of(other: Int): Boolean {
        return when (direction) {
            LEFT -> index < other
            else -> index > other
        }
    }
}