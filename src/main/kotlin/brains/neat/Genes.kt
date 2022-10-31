package brains.neat

class Node(val x: Double, val y: Double, val type: Type) {
    enum class Type { INPUT, OUTPUT, FIRST_HIDDEN, SECOND_HIDDEN }
}

class Connection(val weight: Double, val inverted: Boolean)